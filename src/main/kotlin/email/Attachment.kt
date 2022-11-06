package email

data class Attachment(
    val file: ByteArray,
    val name: String,
    val discription: String
)
