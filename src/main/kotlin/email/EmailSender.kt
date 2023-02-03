package email

import domain.entities.Email
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.MultiPartEmail
import org.koin.core.annotation.Single
import javax.mail.internet.InternetAddress
import javax.mail.util.ByteArrayDataSource

private const val DOCX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"

@Single
class EmailSender(private val emailSecrets: EmailSecrets) {
    init {
        System.setProperty("mail.mime.encodefilename", "true")
    }

    fun sendFiles(
        to: Email,
        attachments: List<Attachment>,
        subject: String,
        replyTo: List<Email> = emptyList(),
        message: String? = null
    ) {
        val mail: MultiPartEmail = MultiPartEmail().apply {
            hostName = emailSecrets.hostname
            sslSmtpPort = emailSecrets.port
            isSSLOnConnect = true
            setAuthenticator(DefaultAuthenticator(emailSecrets.username, emailSecrets.password))
            addTo(to.email)
            setFrom(emailSecrets.from)
            this.subject = subject
            if (replyTo.isNotEmpty()) {
                setReplyTo(replyTo.map { InternetAddress(it.email) })
            }
            if (message != null) {
                setMsg(message)
            }
            attachments.forEach {
                attach(ByteArrayDataSource(it.file, DOCX_MIME_TYPE), it.name, it.description)
            }
        }
        mail.send()
    }
}
