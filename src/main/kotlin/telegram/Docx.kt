package telegram

import com.deepoove.poi.XWPFTemplate
import com.deepoove.poi.config.Configure
import com.deepoove.poi.config.Configure.AbortHandler
import domain.documents.Document
import java.io.ByteArrayOutputStream
import java.io.InputStream

object Docx {
    fun load(document: Document): ByteArray {
        val inputStream = this::class.java.getResourceAsStream(document.templatePath)
        return replace(inputStream, document.replacements)
    }

    private fun replace(inputStream: InputStream, replacements: Collection<Pair<String, String>>): ByteArray {
        return XWPFTemplate.compile(
            inputStream,
            Configure.builder()
                .setValidErrorHandler(AbortHandler())
                .build()
        ).render(replacements.toMap()).use { document ->
            ByteArrayOutputStream().also {
                document.write(it)
            }.toByteArray()
        }
    }
}
