package com.example.jajaying.kotlinproject.act

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsProvider
import com.example.jajaying.kotlinproject.R
import android.support.v4.app.NotificationCompat.getCategory
import android.util.Log

import org.apache.poi.hwpf.extractor.WordExtractor
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream


class DocActivity : AppCompatActivity() {
/**
 * 读doc文件有两种方式
(a)通过WordExtractor读文件
(b)通过HWPFDocument读文件
 * */

//在日常应用中,我们从word文件里面读取信息的情况非常少见,更多的还是把内容写入到word文件中。
// 使用POI从word doc文件读取数据时主要有两种方式:通过WordExtractor读和通过HWPFDocument读。
// 在WordExtractor内部进行信息读取时还是通过HWPFDocument来获取的

//在使用WordExtractor读文件时我们只能读到文件的文本内容和基于文档的一些属性,至于文档内容的属性等是无法读到的。
// 如果要读到文档内容的属性则需要使用HWPFDocument来读取了。
private val PATH = Environment.getExternalStorageDirectory().absolutePath + "/" + "test.doc"
    private val TAG = "DocActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc)
    }


    @Throws(Exception::class)
    fun testReadByExtractor() {
        val `is` = FileInputStream(PATH)
        val extractor = WordExtractor(`is`)
        //输出word文档所有的文本
        log(extractor.getText())
        log(extractor.getTextFromPieces())
        //输出页眉的内容
        log("页眉:" + extractor.getHeaderText())

        //输出页脚的内容
        log("页脚:" + extractor.getFooterText())

        //获取各个段落的文本
        val paraTexts = extractor.getParagraphText()
        for (i in paraTexts.indices) {
            log("Paragraph " + (i + 1) + " : " + paraTexts[i])
        }

        this.closeStream(`is`)
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

    fun log(text:String){
        Log.d(TAG,text)
    }
}
