package data.dataSource.util

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController

class CheckValidationInputsTest {
    private var string: String = ""
    private val uiController: UiController = mockk(relaxed = true)
    private lateinit var checkValidationInputs: CheckValidationInputs

    @BeforeEach
    fun setup() {
        checkValidationInputs = CheckValidationInputs()
    }

    @Test
    fun `should return project id if not null`() {
        string = "12"
        every { uiController.readInput() } returns string

        checkValidationInputs.checkValidation(string, uiController)

        assertThat(uiController.readInput()).isEqualTo("12")
    }

    @Test
    fun `should re input project id if is empty`() {
        every { uiController.readInput() } returns string

        checkValidationInputs.checkValidation(string, uiController)

        verify { uiController.printMessage("Enter Valid project ID:") }
    }
}