import java.io.FileInputStream
import java.util.*

fun readPropertiesFile(): Properties {
    val properties = Properties()
    properties.load(FileInputStream(System.getenv()["PROPERTIES_FILE"]))
    return properties
}
