package presentation.cli.project

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.NoProjectsFoundException
import domain.customeExceptions.UserEnterInvalidValueException
import domain.usecases.project.DeleteProjectUseCase
import domain.usecases.project.GetAllProjectsUseCase
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

class DeleteProjectCliTest {
    private val deleteProjectUseCase: DeleteProjectUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private lateinit var deleteProjectCli: DeleteProjectCli
    private val getAllProjectsUseCase: GetAllProjectsUseCase = mockk(relaxed = true)

    val id: UUID = UUID.randomUUID()
    val project = listOf(id.toString(), "ahmed", "ahmed mohamed egypt", LocalDateTime.now().toString())
    private val project2 =
        listOf("", "ahmed mohamed egypt", LocalDateTime.now().toString())

    @BeforeEach
    fun setup() {
        deleteProjectCli = DeleteProjectCli(getAllProjectsUseCase, deleteProjectUseCase, uiController)
    }

    @Test
    fun `should throw exception if no projects`() = runTest {
        val exception = assertThrows<NoProjectsFoundException> {
            deleteProjectCli.delete()
        }
        assertThat(exception.message).isEqualTo("Not projects found")
    }

    @Test
    fun `should throw exception if user input is null`() = runTest {
        coEvery { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())
        every { uiController.readInput() } returnsMany project2

        val exception = assertThrows<UserEnterInvalidValueException> {
            deleteProjectCli.delete()
        }
        assertThat(exception.message).isEqualTo("Should enter valid value")
    }

    @Test
    fun `should throw exception if user input is zero`() = runTest {
        coEvery { getAllProjectsUseCase.execute() } returns listOf(createDummyProject(), createDummyProject())
        every { uiController.readInput() } returns "0"

        val exception = assertThrows<UserEnterInvalidValueException> {
            deleteProjectCli.delete()
        }
        assertThat(exception.message).isEqualTo("should enter found id")
    }

    @Test
    fun `should throw exception if user input greater than number of projects`() = runTest {
        coEvery { getAllProjectsUseCase.execute() } returns listOf(createDummyProject(), createDummyProject())
        every { uiController.readInput() } returns "3"

        val exception = assertThrows<UserEnterInvalidValueException> {
            deleteProjectCli.delete()
        }
        assertThat(exception.message).isEqualTo("should enter found id")
    }

    @Test
    fun `should call execute function in create use case when I call create function and success to create project`() =
        runTest {
            coEvery { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())
            every { uiController.readInput() } returns "1"
            coEvery { deleteProjectUseCase.execute(any()) } returns true

            deleteProjectCli.delete()

            coVerify { deleteProjectUseCase.execute(any()) }
        }

    @Test
    fun `should call execute function in create use case when I call create function but failed to create project`() =
        runTest {
            coEvery { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())
            every { uiController.readInput() } returns "1"
            coEvery { deleteProjectUseCase.execute(any()) } returns false

            deleteProjectCli.delete()

            coVerify { deleteProjectUseCase.execute(any()) }
        }
}