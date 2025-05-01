package presentation.Cli

class InputHandler(private val reader: () -> String = { readln() }) {
    fun readLine(prompt: String): String {
        print("$prompt ")
        return reader()
    }

    fun readInt(prompt: String): Int {
        print("$prompt ")
        return reader().toIntOrNull() ?: -1
    }
}
