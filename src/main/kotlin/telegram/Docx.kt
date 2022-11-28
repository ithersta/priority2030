package telegram

import com.xandryex.WordReplacer
import domain.documents.Document
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.ByteArrayOutputStream
import java.io.InputStream

object Docx {
    fun load(document: Document): ByteArray {
        val inputStream = this::class.java.getResourceAsStream(document.templatePath)
        return replace(inputStream, document.replacements)
    }

    private fun replace(inputStream: InputStream, replacements: Collection<Pair<String, String>>): ByteArray {
        return XWPFDocument(inputStream).use { document ->
            val wordReplacer = WordReplacer(document)
            replacements.forEach {
                wordReplacer.replaceWordsInText(it.first, it.second)
                wordReplacer.replaceWordsInTables(it.first, it.second)
            }
            wordReplacer.moddedXWPFDoc.use { moddedDocument ->
                ByteArrayOutputStream().also {
                    moddedDocument.write(it)
                }.toByteArray()
            }
        }
    }
}
