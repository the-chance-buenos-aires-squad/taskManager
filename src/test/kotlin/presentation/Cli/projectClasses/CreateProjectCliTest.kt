package presentation.Cli.projectClasses

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.UserEnterEmptyValueException
import domain.usecases.project.CreateProjectUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.UiController

class CreateProjectCliTest {
    private val createProjectUseCase: CreateProjectUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private lateinit var createProjectCli: CreateProjectCli

    @BeforeEach
    fun setup() {
        createProjectCli = CreateProjectCli(createProjectUseCase, uiController)
    }

    @Test
    fun `should call execute function in create use case when I call create function and success to create project`() {
        every { createProjectUseCase.execute(any()) } returns true
        every { uiController.readInput() } returnsMany listOf("1", "ahmed", "f1 project")

        createProjectCli.create()

        verify { createProjectUseCase.execute(any()) }
    }

    @Test
    fun `should call execute function in create use case when I call create function and failed to create project`() {
        every { createProjectUseCase.execute(any()) } returns false
        every { uiController.readInput() } returnsMany listOf("1", "ahmed", "f1 project")

        createProjectCli.create()

        verify { createProjectUseCase.execute(any()) }
    }

    @Test
    fun `should throw exception when enter empty ID and failed to create project`() {
        every { uiController.readInput() } returnsMany listOf("", "ahmed", "f1 project")

        val exception = assertThrows<UserEnterEmptyValueException> {
            createProjectCli.create()
        }
        assertThat(exception.message).isEqualTo("ID can't be empty")
    }

    @Test
    fun `should throw exception when enter empty name and failed to create project`() {
        every { uiController.readInput() } returnsMany listOf("1", "", "f1 project")

        val exception = assertThrows<UserEnterEmptyValueException> {
            createProjectCli.create()
        }
        assertThat(exception.message).isEqualTo("name can't be empty")
    }

    @Test
    fun `should throw exception when enter empty description and failed to create project`() {
        every { uiController.readInput() } returnsMany listOf("1", "ahmed", "")

        val exception = assertThrows<UserEnterEmptyValueException> {
            createProjectCli.create()
        }
        assertThat(exception.message).isEqualTo("description can't be empty")
    }
}