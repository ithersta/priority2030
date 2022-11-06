package email

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.MultiPartEmail
import javax.mail.util.ByteArrayDataSource

private const val DOCX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"

class EmailSender(private val emailSecrets: EmailSecrets) {
    fun sendFiles(to: String, files: List<Attachment>) {
        val mail: MultiPartEmail = MultiPartEmail().apply {
            hostName = emailSecrets.hostname
            sslSmtpPort = emailSecrets.port
            isSSLOnConnect = true
            setAuthenticator(DefaultAuthenticator(emailSecrets.username, emailSecrets.password))
            setFrom(emailSecrets.from)

            subject = Strings.SendFilesSubject
            setMsg(Strings.SendFilesMessage)
            addTo(to)

            files.forEach {
                attach(
                    ByteArrayDataSource(it.file, DOCX_MIME_TYPE),
                    it.name,
                    it.discription
                )
            }
        }
        mail.send()
    }
}