package presentation.cli.project

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.NoProjectsFoundException
import domain.customeExceptions.UserEnterInvalidValueException
import domain.usecases.project.GetAllProjectsUseCase
import domain.usecases.project.UpdateProjectUseCase
import dummyData.createDummyProject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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
    fun `should throw exception if no projects`() = runTest {
        val exception = assertThrows<NoProjectsFoundException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("Not projects found")
    }

    @Test
    fun `should throw exception if user input is null`() = runTest {
        coEvery { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())
        every { uiController.readInput() } returns ""

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("ID can't be empty")
    }

    @Test
    fun `should throw exception if user input Invalid value`() = runTest {
        coEvery { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())
        every { uiController.readInput() } returns "a"

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("Should enter valid value")
    }

    @Test
    fun `should throw exception if user input name is empty`() = runTest {
        coEvery { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())
        every { uiController.readInput() } returnsMany listOf("1", "")

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("New name can't be empty")
    }

    @Test
    fun `should throw exception if user input description is empty`() = runTest {
        coEvery { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())
        every { uiController.readInput() } returnsMany listOf("1", "ahmed", "")

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("description can't be empty")
    }

    @Test
    fun `should throw exception if user input is zero`() = runTest {
        coEvery { getAllProjectsUseCase.execute() } returns listOf(createDummyProject(), createDummyProject())
        every { uiController.readInput() } returns "0"

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("should enter found id")
    }

    @Test
    fun `should throw exception if user input greater than number of projects`() = runTest {
        coEvery { getAllProjectsUseCase.execute() } returns listOf(createDummyProject(), createDummyProject())
        every { uiController.readInput() } returns "3"

        val exception = assertThrows<UserEnterInvalidValueException> {
            updateProjectCli.update()
        }
        assertThat(exception.message).isEqualTo("should enter found id")
    }

    @Test
    fun `should call execute function in update project use case when I call update function and success to create project`() =
        runTest {
            coEvery { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())
            every { uiController.readInput() } returns "1"
            coEvery { updateProjectUseCase.execute(any()) } returns true

            updateProjectCli.update()

            coVerify { updateProjectUseCase.execute(any()) }
        }

    @Test
    fun `should call execute function in update project use case when I call update function and failed to create project`() =
        runTest {
            coEvery { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())
            every { uiController.readInput() } returns "1"
            coEvery { updateProjectUseCase.execute(any()) } returns false

            updateProjectCli.update()

            coVerify { updateProjectUseCase.execute(any()) }
        }

}