package presentation

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test


class UiControllerTest {


    private val uiController = UiController()
    private lateinit var outputStream: ByteArrayOutputStream

    @BeforeEach
    fun setUp() {
        outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
    }


    @Test
    fun `should print message with new line`() {
        uiController.printMessage("hello")

        val expectedOutput = "hello\n"
        assertThat(outputStream.toString()).isEqualTo(expectedOutput)
    }

    @Test
    fun `should print message without new line `() {
        uiController.printMessage("hello", isInline = true)

        assertThat(outputStream.toString()).isEqualTo("hello")
    }

    @Test
    fun `readInput should return input from System in`() {
        val input = "my input"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        val result = uiController.readInput()

        assertThat(result).isEqualTo(input)
    }


}