package presentation.Cli.projectClasses

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.UserEnterEmptyValueException
import domain.usecases.project.CreateProjectUseCase
import domain.usecases.project.UpdateProjectUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.UiController

class UpdateProjectCliTest {
    private val updateProjectUseCase: UpdateProjectUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private lateinit var updateProjectCli: UpdateProjectCli

    @BeforeEach
    fun setup() {
        updateProjectCli = UpdateProjectCli(updateProjectUseCase, uiController)
    }

    @Test
    fun `should call execute function in create use case when I call create function and success to create project`() {
        every { updateProjectUseCase.execute(any()) } returns true
        every { uiController.readInput() } returnsMany listOf("1", "ahmed", "f1 project")

        updateProjectCli.update()

        verify { updateProjectUseCase.execute(any()) }
    }

    @Test
    fun `should call execute function in create use case when I call create function and failed to create project`() {
        every { updateProjectUseCase.execute(any()) } returns false
        every { uiController.readInput() } returnsMany listOf("1", "ahmed", "f1 project")

        updateProjectCli.update()

        verify { updateProjectUseCase.execute(any()) }
    }

    @Test
    fun `should throw exception when enter empty ID and failed to create project`() {
        every { uiController.readInput() } returnsMany listOf("", "ahmed", "f1 project")

        val exception = assertThrows<UserEnterEmptyValueException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("New ID can't be empty")
    }

    @Test
    fun `should throw exception when enter empty name and failed to create project`() {
        every { uiController.readInput() } returnsMany listOf("1", "", "f1 project")

        val exception = assertThrows<UserEnterEmptyValueException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("New name can't be empty")
    }

    @Test
    fun `should throw exception when enter empty description and failed to create project`() {
        every { uiController.readInput() } returnsMany listOf("1", "ahmed", "")

        val exception = assertThrows<UserEnterEmptyValueException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("description can't be empty")
    }
}