package email

import domain.entities.Email
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.MultiPartEmail
import org.koin.core.annotation.Single
import javax.mail.util.ByteArrayDataSource

private const val DOCX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"

@Single
class EmailSender(private val emailSecrets: EmailSecrets) {
    fun sendFiles(to: Email, attachments: List<Attachment>, subject: String, message: String) {
        val mail: MultiPartEmail = MultiPartEmail().apply {
            hostName = emailSecrets.hostname
            sslSmtpPort = emailSecrets.port
            isSSLOnConnect = true
            setAuthenticator(DefaultAuthenticator(emailSecrets.username, emailSecrets.password))
            setFrom(emailSecrets.from)
            this.subject = subject
            setMsg(message)
            addTo(to.email)

            attachments.forEach {
                attach(
                    ByteArrayDataSource(it.file, DOCX_MIME_TYPE),
                    it.name,
                    it.description
                )
            }
        }
        mail.send()
    }
}
