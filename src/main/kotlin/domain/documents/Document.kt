package domain.documents

class Document(
    val templatePath: String,
    val replacements: List<Pair<String, String>>
)
