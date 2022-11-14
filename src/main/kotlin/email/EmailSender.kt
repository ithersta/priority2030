package email

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.MultiPartEmail
import javax.mail.util.ByteArrayDataSource

private const val DOCX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"

object EmailSender {
     fun sendFiles(to: String, files: List<Attachment>) {
        val mail: MultiPartEmail = MultiPartEmail().apply {
            hostName = EmailSecrets.hostname
            sslSmtpPort = EmailSecrets.port
            isSSLOnConnect = true
            setAuthenticator(DefaultAuthenticator(EmailSecrets.username, EmailSecrets.password))
            setFrom(EmailSecrets.from)
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
