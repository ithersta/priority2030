package email

import org.koin.core.annotation.Single
import java.io.FileInputStream
import java.util.Properties

data class EmailSecrets(
    val hostname: String,
    val port: String,
    val username: String,
    val password: String,
    val from: String
)

fun readEmailSecrets(properties: Properties): EmailSecrets{
    return EmailSecrets(
        properties.getProperty("EMAIL_HOSTNAME"),
        properties.getProperty("EMAIL_PORT"),
        properties.getProperty("EMAIL_USERNAME"),
        properties.getProperty("EMAIL_PASSWORD"),
        properties.getProperty("EMAIL_FROM")
    )
}