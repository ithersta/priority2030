@Single
class FullNameCollector : Collector {
    override val kClass = FullName::class

    override fun RequestsExecutor.ask(chat: Chat) {
        sendTextMessage(chat, "Имя???????????????????")
    }

    override fun StatefulContext.onText(message: TextMessage): List<FieldData> {
        val fullName = FullName.of(message.content.text) ?: run {
            sendTextMessage("Неправильно")
            return emptyList()
        }
        return listOf(fullName)
    }
}


