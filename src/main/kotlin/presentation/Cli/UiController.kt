package presentation.Cli

class UiController {
    val printedMessages = mutableListOf<String>()
    fun printMessage(message: String, isInline: Boolean = false) {
        printedMessages.add(message)
        if (isInline) {
            print(message)
        } else {
            println(message)
        }
    }

    fun readInput(): String {
        return readlnOrNull() ?: ""
    }

}
