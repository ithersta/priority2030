import domain.entities.Email
import email.EmailSecrets
import java.io.FileInputStream
import java.util.*

data class MainProperties(
    val token: String,
    val morpherToken: String,
    val emailSecrets: EmailSecrets,
    val emailTo: Email
)

fun readMainProperties(): MainProperties {
    val properties = Properties()
    properties.load(FileInputStream(System.getenv("CONFIG_FILE")))
    return MainProperties(
        token = properties.getProperty("TOKEN"),
        morpherToken = properties.getProperty("MORPHER_TOKEN"),
        emailSecrets = EmailSecrets(
            properties.getProperty("EMAIL_HOSTNAME"),
            properties.getProperty("EMAIL_PORT"),
            properties.getProperty("EMAIL_USERNAME"),
            properties.getProperty("EMAIL_PASSWORD"),
            properties.getProperty("EMAIL_FROM")
        ),
        emailTo = Email.of(properties.getProperty("EMAIL_TO")) ?: error("Malformed EMAIL_TO")
    )
}
