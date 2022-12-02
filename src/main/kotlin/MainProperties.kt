import email.EmailSecrets
import java.io.FileInputStream
import java.util.*

data class MainProperties(
    val token: String,
    val emailSecrets: EmailSecrets
)

fun readMainProperties(): MainProperties {
    val properties = Properties()
    properties.load(FileInputStream(System.getenv("CONFIG_FILE")))
    return MainProperties(
        token = properties.getProperty("TOKEN"),
        emailSecrets = EmailSecrets(
            properties.getProperty("EMAIL_HOSTNAME"),
            properties.getProperty("EMAIL_PORT"),
            properties.getProperty("EMAIL_USERNAME"),
            properties.getProperty("EMAIL_PASSWORD"),
            properties.getProperty("EMAIL_FROM")
        )
    )
}
