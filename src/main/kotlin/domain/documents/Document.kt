package domain.documents

import kotlinx.serialization.Serializable

@Serializable
class Document(
    val templatePath: String,
    val replacements: List<Pair<String, String>>
)
