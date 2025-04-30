package presentation.Cli

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.*
import java.io.*

class UiControllerTest {

    private val uiController = UiController()



    @Test
    fun `test if printLine is called with a message`(){
        uiController.printMessage("hello")

        assertThat(uiController.printedMessages).containsExactly("hello")
    }



    @Test
    fun `readInput should return input from System in`() {
        val input = "my input\n"
        val inputStream = ByteArrayInputStream(input.toByteArray())
        System.setIn(inputStream)

        val result = uiController.readInput()

        assertThat(result).isEqualTo("my input")
    }


}