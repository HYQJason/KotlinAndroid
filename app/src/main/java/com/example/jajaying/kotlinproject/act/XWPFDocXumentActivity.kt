/*
package com.example.jajaying.kotlinproject.act

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.jajaying.kotlinproject.R
import com.example.jajaying.kotlinproject.poi.xwpf.converter.xhtml.XHTMLConverter
import com.example.jajaying.kotlinproject.poi.xwpf.converter.xhtml.XHTMLOptions
import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.hwpf.converter.PicturesManager
import org.apache.poi.hwpf.converter.WordToHtmlConverter
import org.apache.poi.hwpf.usermodel.Picture
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.*
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.TransformerException
import org.apache.poi.util.IOUtils
import java.util.logging.Logger


*/
/**
 * POI在读写word docx文件时是通过xwpf模块来进行的,其核心是XWPFDocument。
 * 一个XWPFDocument代表一个docx文档,其可以用来读docx文档,也可以用来写docx文档
 * *//*

class XWPFDocXumentActivity : AppCompatActivity() {
    */
/**
     * XWPFParagraph	代表一个段落
    XWPFRun	代表具有相同属性的一段文本
    XWPFTable	代表一个表格
    XWPFTableRow	表格的一行
    XWPFTableCell	表格对应的一个单元格
     * *//*


    private val logger = Logger.getLogger(ReadWordUtils::class.java)
    protected val CHARSET_UTF8 = "UTF-8"
    private val tempImagePath = ""

//同时XWPFDocument可以直接new一个docx文件出来而不需要像HWPFDocument一样需要一个模板存在。

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xwpfdoc_xument)
    }

    */
/**
     * 将docx格式的word转换为html格式的文档
     *
     * @param filePath 原始的docx文件路径存储位置
     * @param outPutFile html输出文件路径
     * @return
     * @throws TransformerException
     * @throws IOException
     * @throws ParserConfigurationException
     *//*

    @Throws(TransformerException::class, IOException::class, ParserConfigurationException::class)
    fun docx2Html(filePath: String,
                  outPutFilePath: String): String {
        //String fileOutName = outPutFile;
        val wordDocument = XWPFDocument(FileInputStream(filePath))
        val options = XHTMLOptions.create().indent(4)

        // 导出图片
        val imageInfoMap = gainTempImagePath(outPutFilePath)
        val imageFolder = File(imageInfoMap.get("imageStoredPath"))
        options.setExtractor(FileImageExtractor(imageFolder))
        // URI resolver
        //这种方式获得word中的图片地址是绝对地址
        //options.URIResolver(new FileURIResolver(imageFolder));
        //设置生成的html中的img src中的地址是相对路径
        options.URIResolver(BasicURIResolver(imageInfoMap.get("imageFolder")))

        val outFile = File(outPutFilePath)
        outFile.parentFile.mkdirs()
        val out = FileOutputStream(outFile)
        XHTMLConverter.getInstance().convert(wordDocument, out, options)

        return gainRelativePathByOutputPath(outPutFilePath)
        //System.out.println("Generate " + fileOutName + " with " + (System.currentTimeMillis() - startTime) + " ms.");
    }

    */
/**
     * \brief 将内容写到path路径下面
     * @param content            :文档内容
     * @param path               :最终的文件存储路径
     * @attention 方法的使用注意事项
     * @author toto
     * @date 2017年2月27日
     * @note  begin modify by 涂作权 2017年2月27日   修改输出的文件名称
     *//*

    fun writeFile(docContent: String, path: String) {
        var outDocFos: FileOutputStream? = null
        try {
            //判断文件是否为空的
            if (StringUtils.isNotBlank(path)) {
                val file = File(path)
                if (!file.exists()) {
                    FileUtils.forceMkdir(file.parentFile)
                }

                outDocFos = FileOutputStream(path)
                IOUtils.write(docContent, outDocFos, CHARSET_UTF8)
            }
        } catch (fnfe: FileNotFoundException) {
            fnfe.printStackTrace()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        } finally {
            try {
                if (outDocFos != null)
                    outDocFos.close()
            } catch (ie: IOException) {
            }

        }
    }

    */
/**
     * 关闭输入流
     *
     * @param is
     *//*

    private fun close(`is`: InputStream?) {
        if (`is` != null) {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    */
/**
     * \brief 通过文档输出路径获得图片存储路径
     * @param outPutFile             :文档输出路径
     * @return
     * @attention 方法的使用注意事项
     * @author toto
     * @date 2017年2月28日
     * @note  begin modify by 修改人 修改时间   修改内容摘要说明
     *//*

    private fun gainTempImagePath(outPutFilePath: String): Map<String, String>? {
        val imageInfoMap = HashMap<String, String>()
        try {
            //File file = new File(outPutFilePath);
            tempImagePath = outPutFilePath.substring(0, outPutFilePath.indexOf(".html")) + File.separator

            val imageFolder = File(tempImagePath)
            if (!imageFolder.exists()) {
                try {
                    FileUtils.forceMkdir(imageFolder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            //System.out.println(imageFolder.getPath().replace(imageFolder.getParentFile().getPath() + File.separator, ""));
            //return imageFolder.getPath().replace(imageFolder.getParentFile().getPath() + File.separator, "");

            imageInfoMap["imageStoredPath"] = imageFolder.getPath()
            imageInfoMap["imageFolder"] = imageFolder.getPath().replace(imageFolder.getParentFile().getPath(), "").replace(File.separator, "")

            return imageInfoMap
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private fun gainRelativePathByOutputPath(outPutFilePath: String): String {
        //用于预览的存储路径
        val docsPreviewPath = ExtendedServerConfig.getInstance().getStringProp("DOCS_PREVIEW_PREFIX")
        return outPutFilePath.split(docsPreviewPath.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
    }
    @Throws(IOException::class)
    fun readAndWriterTest4() {
        val file = File("C:\\Users\\tuzongxun123\\Desktop\\aa.docx")
        val str = ""
        try {
            val fis = FileInputStream(file)
            val xdoc = XWPFDocument(fis)
            val extractor = XWPFWordExtractor(xdoc)
            val doc1 = extractor.getText()
            println(doc1)
            fis.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
*/
