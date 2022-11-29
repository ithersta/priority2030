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
