package presentation.cli.helper

import com.google.common.truth.Truth.assertThat
import domain.usecases.project.GetAllProjectsUseCase
import dummyData.createDummyProject
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController
import presentation.cli.helper.ProjectCliHelper.Companion.INVALID_INPUT_MESSAGE
import java.util.*

class ProjectCliHelperTest {
    private lateinit var projectCliHelper: ProjectCliHelper
    private val getAllProjectsUseCase: GetAllProjectsUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)

    private val sampleProjects = listOf(
        createDummyProject(id = UUID.randomUUID(), name = "Project1", description = "Desc1"),
        createDummyProject(id = UUID.randomUUID(), name = "Project2", description = "Desc2")
    )

    @BeforeEach
    fun setup() {
        projectCliHelper = ProjectCliHelper(getAllProjectsUseCase, uiController)
    }

    @Test
    fun `getProjects should returns list when use case succeeds`() = runTest {
        coEvery { getAllProjectsUseCase.execute() } returns sampleProjects

        val result = projectCliHelper.getProjects()

        assertThat(result).isEqualTo(sampleProjects)
        verify(exactly = 0) { uiController.printMessage(any()) }
    }

//    @Test
//    fun `getProjects should returns empty list and prints message on NoProjectsFoundException`() = runTest {
//        val exceptionMessage = "No projects found"
//        coEvery { getAllProjectsUseCase.execute() } throws NoProjectsFoundException(exceptionMessage)
//
//        val result = projectCliHelper.getProjects()
//
//        assertThat(result).isEmpty()
//        verify { uiController.printMessage(EXCEPTION_MESSAGE.format(exceptionMessage)) }
//    }

    @Test
    fun `selectProject should returns null when project list is empty`() {
        val result = projectCliHelper.selectProject(emptyList())

        assertThat(result).isNull()
        verify(exactly = 0) { uiController.printMessage(any()) }
    }

    @Test
    fun `selectProject prints list and returns selected project on valid input`() {
        every { uiController.readInput() } returns "2"

        val result = projectCliHelper.selectProject(sampleProjects)

        verify {
            uiController.printMessage(ProjectCliHelper.SELECT_PROJECT_MESSAGE)
            uiController.printMessage("1. Project1 - Desc1")
            uiController.printMessage("2. Project2 - Desc2")
            uiController.printMessage(ProjectCliHelper.ENTER_PROJECT_MESSAGE, true)
        }
        assertThat(result).isEqualTo(sampleProjects[1])
    }

    @Test
    fun `selectProject retries on invalid and empty inputs then returns project`() {
        every { uiController.readInput() } returns "" andThen "x" andThen "1"

        projectCliHelper.selectProject(sampleProjects)

        verify {
            uiController.printMessage(ProjectCliHelper.SELECT_PROJECT_MESSAGE)
            uiController.printMessage("1. Project1 - Desc1")
            uiController.printMessage("2. Project2 - Desc2")
            uiController.printMessage(ProjectCliHelper.ENTER_PROJECT_MESSAGE, true)
            uiController.printMessage(ProjectCliHelper.INVALID_INPUT_MESSAGE)
            uiController.printMessage(ProjectCliHelper.ENTER_PROJECT_MESSAGE, true)
            uiController.printMessage(ProjectCliHelper.INVALID_INPUT_MESSAGE)
            uiController.printMessage(ProjectCliHelper.ENTER_PROJECT_MESSAGE, true)
        }
    }

    @Test
    fun `selectProject handles out-of-range numeric input then returns project`() {
        // user enters 0 (minus 1 = -1) then 3 (minus1 =2) both invalid, then valid 2
        every { uiController.readInput() } returns "0" andThen "3" andThen "2"

        val result = projectCliHelper.selectProject(sampleProjects)

        verify {
            uiController.printMessage(ProjectCliHelper.SELECT_PROJECT_MESSAGE)
            uiController.printMessage("1. Project1 - Desc1")
            uiController.printMessage("2. Project2 - Desc2")
            // first invalid numeric below range
            uiController.printMessage(ProjectCliHelper.ENTER_PROJECT_MESSAGE, true)
            uiController.printMessage("$INVALID_INPUT_MESSAGE from the menu.")
            // second invalid numeric above range
            uiController.printMessage(ProjectCliHelper.ENTER_PROJECT_MESSAGE, true)
            uiController.printMessage("$INVALID_INPUT_MESSAGE from the menu.")
            // finally valid
            uiController.printMessage(ProjectCliHelper.ENTER_PROJECT_MESSAGE, true)
        }
        assertThat(result).isEqualTo(sampleProjects[1])
    }

}