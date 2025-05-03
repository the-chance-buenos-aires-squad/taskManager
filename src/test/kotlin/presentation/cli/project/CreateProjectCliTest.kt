package presentation.cli.project

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.UserEnterInvalidValueException
import domain.usecases.project.CreateProjectUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.UiController
import java.time.LocalDateTime
import java.util.*

class CreateProjectCliTest {
    private val createProjectUseCase: CreateProjectUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private lateinit var createProjectCli: CreateProjectCli

    val project = listOf(UUID.randomUUID().toString(), "ahmed", "ahmed mohamed egypt", LocalDateTime.now().toString())
    private val project2 =
        listOf("", "ahmed mohamed egypt", LocalDateTime.now().toString())
    private val project3 =
        listOf("ahmed", "", LocalDateTime.now().toString())


    @BeforeEach
    fun setup() {
        createProjectCli = CreateProjectCli(createProjectUseCase, uiController)
    }

    @Test
    fun `should call execute function in create use case when I call create function and success to create project`() {
        every { createProjectUseCase.execute(any()) } returns true
        every { uiController.readInput() } returnsMany project

        createProjectCli.create()

        verify { createProjectUseCase.execute(any()) }
    }

    @Test
    fun `should call execute function in create use case when I call create function and failed to create project`() {
        every { createProjectUseCase.execute(any()) } returns false
        every { uiController.readInput() } returnsMany project

        createProjectCli.create()

        verify { createProjectUseCase.execute(any()) }
    }

    @Test
    fun `should throw exception when enter empty name and failed to create project`() {
        every { uiController.readInput() } returnsMany project2

        val exception = assertThrows<UserEnterInvalidValueException> {
            createProjectCli.create()
        }
        assertThat(exception.message).isEqualTo("name can't be empty")
    }

    @Test
    fun `should throw exception when enter empty description and failed to create project`() {
        every { uiController.readInput() } returnsMany project3

        val exception = assertThrows<UserEnterInvalidValueException> {
            createProjectCli.create()
        }
        assertThat(exception.message).isEqualTo("description can't be empty")
    }
}