package presentation.Cli.projectClasses

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.NoProjectsFoundException
import domain.customeExceptions.UserEnterInvalidValueException
import domain.entities.Project
import domain.usecases.project.GetAllProjectsUseCase
import domain.usecases.project.UpdateProjectUseCase
import dummyData.createDummyProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.UiController
import java.time.LocalDateTime
import java.util.*

class UpdateProjectCliTest {
    private val updateProjectUseCase: UpdateProjectUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private lateinit var updateProjectCli: UpdateProjectCli
    private val getAllProjectsUseCase: GetAllProjectsUseCase = mockk(relaxed = true)

    val id: UUID = UUID.randomUUID()
    val project = listOf(id.toString(), "ahmed", "ahmed mohamed egypt", LocalDateTime.now().toString())

    @BeforeEach
    fun setup() {
        updateProjectCli = UpdateProjectCli(getAllProjectsUseCase, updateProjectUseCase, uiController)
    }

    @Test
    fun `should throw exception if no projects`() {
        val exception = assertThrows<NoProjectsFoundException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("Not projects found")
    }

    @Test
    fun `should throw exception if user input is null`() {
        every { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())
        every { uiController.readInput() } returns ""

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("ID can't be empty")
    }

    @Test
    fun `should throw exception if user input Invalid value`() {
        every { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())
        every { uiController.readInput() } returns "a"

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("Should enter valid value")
    }

    @Test
    fun `should throw exception if user input name is empty`() {
        every { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())
        every { uiController.readInput() } returnsMany listOf("1", "")

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("New name can't be empty")
    }

    @Test
    fun `should throw exception if user input description is empty`() {
        every { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())
        every { uiController.readInput() } returnsMany listOf("1", "ahmed", "")

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("description can't be empty")
    }

    @Test
    fun `should throw exception if user input is zero`() {
        every { getAllProjectsUseCase.execute() } returns listOf(createDummyProject(), createDummyProject())
        every { uiController.readInput() } returns "0"

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("should enter found id")
    }

    @Test
    fun `should throw exception if user input greater than number of projects`() {
        every { getAllProjectsUseCase.execute() } returns listOf(createDummyProject(), createDummyProject())
        every { uiController.readInput() } returns "3"

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("should enter found id")
    }

    @Test
    fun `should call execute function in update project use case when I call update function and success to create project`() {
        every { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())
        every { uiController.readInput() } returns "1"
        every { updateProjectUseCase.execute(any()) } returns true

        updateProjectCli.update()

        verify { updateProjectUseCase.execute(any()) }
    }

    @Test
    fun `should call execute function in update project use case when I call update function and failed to create project`() {
        every { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())
        every { uiController.readInput() } returns "1"
        every { updateProjectUseCase.execute(any()) } returns false

        updateProjectCli.update()

        verify { updateProjectUseCase.execute(any()) }
    }

}