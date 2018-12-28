package com.example.jajaying.kotlinproject.act

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.example.jajaying.kotlinproject.R
import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.hwpf.usermodel.*
import org.apache.poi.hwpf.usermodel.Paragraph
import java.nio.file.Files.size
import org.apache.poi.hwpf.usermodel.PictureType
import org.apache.poi.hwpf.converter.PicturesManager
import org.apache.poi.ooxml.util.DocumentHelper.newDocumentBuilder
import org.apache.poi.hwpf.converter.WordToHtmlConverter
import java.io.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import android.webkit.WebSettings
import android.webkit.WebView
import java.nio.file.Files.exists




/**
 * HWPFDocument是当前Word文档的代表,它的功能比WordExtractor要强。
 * 通过它我们可以读取文档中的表格、列表等,还可以对文档的内容进行新增、修改和删除操作。只是在进行完这些新增、
 * 修改和删除后相关信息是保存在HWPFDocument中的,也就是说我们改变的是HWPFDocument,而不是磁盘上的文件。
 * 如果要使这些修改生效的话,我们可以调用HWPFDocument的write方法把修改后的HWPFDocument输出到指定的输出流中。
 * 这可以是原文件的输出流,也可以是新文件的输出流(相当于另存为)或其它输出流
 * */
class HWPFDocumentActivity : AppCompatActivity() {
    private val PATH = Environment.getExternalStorageDirectory().absolutePath + "/" + "test.doc"
    private val TAG = "HWPFDocumentActivity"

    private val docPath = "/mnt/sdcard/documents/"
    private val docName = "test.doc"
    private val savePath = "/mnt/sdcard/documents/"
    private fun log(o: Any) {
        Log.d(TAG, o.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hwpfdocument)

        val name = docName.substring(0, docName.indexOf("."))
        try {
            if (!File(savePath + name).exists())
                File(savePath + name).mkdirs()
            convert2Html(docPath + docName, "$savePath$name.html")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //WebView加载显示本地html文件
        val webView = this.findViewById(R.id.office) as WebView
        val webSettings = webView.settings
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webView.loadUrl("file:/$savePath$name.html")
    }


    /**
     * word文档转成html格式
     */
    @Throws(TransformerException::class, IOException::class, ParserConfigurationException::class)
    fun convert2Html(fileName: String, outPutFile: String) {
        val wordDocument = HWPFDocument(FileInputStream(fileName))
        val wordToHtmlConverter = WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument())

        //设置图片路径
        wordToHtmlConverter.setPicturesManager(PicturesManager { content, pictureType, suggestedName, widthInches, heightInches ->
            val name = docName.substring(0, docName.indexOf("."))
            name + "/" + suggestedName
        })

        //保存图片
        val pics = wordDocument.picturesTable.allPictures
        if (pics != null) {
            for (i in pics.indices) {
                val pic = pics[i] as Picture
                System.out.println(pic.suggestFullFileName())
                try {
                    val name = docName.substring(0, docName.indexOf("."))
                    pic.writeImageContent(FileOutputStream(savePath + name + "/"
                            + pic.suggestFullFileName()))
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }

            }
        }
        wordToHtmlConverter.processDocument(wordDocument)
        val htmlDocument = wordToHtmlConverter.getDocument()
        val out = ByteArrayOutputStream()
        val domSource = DOMSource(htmlDocument)
        val streamResult = StreamResult(out)

        val tf = TransformerFactory.newInstance()
        val serializer = tf.newTransformer()
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8")
        serializer.setOutputProperty(OutputKeys.INDENT, "yes")
        serializer.setOutputProperty(OutputKeys.METHOD, "html")
        serializer.transform(domSource, streamResult)
        out.close()
        //保存html文件
        writeFile(String(out.toByteArray()), outPutFile)
    }

    /**
     * 将html文件保存到sd卡
     */
    fun writeFile(content: String, path: String) {
        var fos: FileOutputStream? = null
        var bw: BufferedWriter? = null
        try {
            val file = File(path)
            if (!file.exists()) {
                file.createNewFile()
            }
            fos = FileOutputStream(file)
            bw = BufferedWriter(OutputStreamWriter(fos, "utf-8"))
            bw!!.write(content)
        } catch (fnfe: FileNotFoundException) {
            fnfe.printStackTrace()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        } finally {
            try {
                if (bw != null)
                    bw!!.close()
                if (fos != null)
                    fos!!.close()
            } catch (ie: IOException) {
            }

        }
    }









    @Throws(Exception::class)
    fun testReadByDoc() {
        val `is` = FileInputStream(PATH)
        val doc = HWPFDocument(`is`)
        //输出书签信息
        this.printInfo(doc.getBookmarks())
        //输出文本
        log(doc.getDocumentText())
        val range = doc.getRange()
        //读整体
        this.printInfo(range)
        //读表格
        this.readTable(range)
        //读列表
        this.readList(range)
        this.closeStream(`is`)
    }

    @Throws(IOException::class)
    fun readAndWriterTest3() {
        val file = File("C:\\Users\\tuzongxun123\\Desktop\\aa.doc")
        val str = ""
        try {
            val fis = FileInputStream(file)
            val doc = HWPFDocument(fis)
            val doc1 = doc.getDocumentText()
            println(doc1)
            val doc2 = doc.getText()
            println(doc2)
            val rang = doc.getRange()
            val doc3 = rang.text()
            println(doc3)
            fis.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    /**
     * 关闭输入流
     * @param is
     */
    private fun closeStream(`is`: InputStream?) {
        if (`is` != null) {
            try {
                `is`!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 输出书签信息
     * @param bookmarks
     */
    private fun printInfo(bookmarks: Bookmarks) {
        val count = bookmarks.bookmarksCount
        log("书签数量:$count")
        var bookmark: Bookmark
        for (i in 0 until count) {
            bookmark = bookmarks.getBookmark(i)
            log("书签" + (i + 1) + "的名称是:" + bookmark.name)
            log("开始位置:" + bookmark.start)
            log("结束位置:" + bookmark.end)
        }
    }


    /**
     * 读表格
     * 每一个回车符代表一个段落,所以对于表格而言,每一个单元格至少包含一个段落,每行结束都是一个段落。
     * @param range
     */
    private fun readTable(range: Range) {
        //遍历range范围内的table。
        val tableIter = TableIterator(range)
        var table: Table
        var row: TableRow
        var cell: TableCell
        while (tableIter.hasNext()) {
            table = tableIter.next()
            val rowNum = table.numRows()
            for (j in 0 until rowNum) {
                row = table.getRow(j)
                val cellNum = row.numCells()
                for (k in 0 until cellNum) {
                    cell = row.getCell(k)
                    //输出单元格的文本
                    log(cell.text().trim())
                }
            }
        }
    }

    /**
     * 读列表
     * @param range
     */
    private fun readList(range: Range) {
        val num = range.numParagraphs()
        var para: Paragraph
        for (i in 0 until num) {
            para = range.getParagraph(i)
            if (para.isInList) {
                log("list: " + para.text())
            }
        }
    }

    /**
     * 输出Range
     * @param range
     */
    private fun printInfo(range: Range) {
        //获取段落数
        val paraNum = range.numParagraphs()
        log(paraNum)
        for (i in 0 until paraNum) {
            log("段落" + (i + 1) + ":" + range.getParagraph(i).text())
            if (i == paraNum - 1) {
                this.insertInfo(range.getParagraph(i))
            }
        }
        val secNum = range.numSections()
        log(secNum)
        var section: Section
        for (i in 0 until secNum) {
            section = range.getSection(i)
            log(section.marginLeft)
            log(section.marginRight)
            log(section.marginTop)
            log(section.marginBottom)
            log(section.pageHeight)
            log(section.text())
        }
    }

    /**
     * 插入内容到Range,这里只会写到内存中
     * @param range
     */
    private fun insertInfo( range:Range) {
        range.insertAfter("Hello")

}

}
