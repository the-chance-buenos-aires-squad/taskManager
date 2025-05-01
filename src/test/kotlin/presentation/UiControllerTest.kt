package presentation

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.Scanner
import kotlin.test.Test


class UiControllerTest {

    private val mockedPrinterStream = mockk<PrintStream>(relaxed = true)
    private val mockedScanner = mockk<Scanner>(relaxed = true)
    private lateinit var  uiController : UiController

    @BeforeEach
    fun setUp() {
        uiController = UiController(mockedPrinterStream,mockedScanner)
    }

    @Test
    fun `should print message with new line when isInline false `() {
        //printing message in new line
        uiController.printMessage(message = "hello", isInline = false)

        verify { mockedPrinterStream.println("hello") }
    }


    @Test
    fun `should print message in same line when isInline true `() {
        //printing message in the same line
        uiController.printMessage(message = "hello", isInline = true)

        verify { mockedPrinterStream.print("hello") }
    }


    @Test
    fun `readInput should return input from System in`() {
        val input = "my input"
        every { mockedScanner.nextLine() } returns input

        val result = uiController.readInput()

        assertThat(result).isEqualTo(input)
    }

}