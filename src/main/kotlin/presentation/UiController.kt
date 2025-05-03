package presentation

import java.io.PrintStream
import java.util.*


class UiController(
    private val printer: PrintStream,
    private val scanner: Scanner,
) {

    fun printMessage(
        message: String,
        isInline: Boolean = false
    ) {
        if (isInline) {
            printer.print(message)
        } else {
            printer.println(message)
        }
    }

    fun readInput(): String {
        return scanner.nextLine()
    }

}