package presentation.cli.project

import domain.usecases.project.DeleteProjectUseCase
import dummyData.createDummyProject
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController
import presentation.cli.helper.ProjectCliHelper
import presentation.cli.project.DeleteProjectCli.Companion.DELETE_PROJECT_FROM

class DeleteProjectCliTest {

    private lateinit var deleteProjectUseCase: DeleteProjectUseCase
    private lateinit var projectCliHelper: ProjectCliHelper
    private lateinit var uiController: UiController
    private lateinit var deleteProjectCli: DeleteProjectCli
    private val dummyProject = createDummyProject(name = "Test Project", description = "Test Description")


    @BeforeEach
    fun setup() {
        deleteProjectUseCase = mockk()
        projectCliHelper = mockk()
        uiController = mockk(relaxed = true)
        deleteProjectCli = DeleteProjectCli(deleteProjectUseCase, projectCliHelper, uiController)
    }

    @Test
    fun `should print message if no project is selected`() = runTest {
        coEvery { projectCliHelper.getProjects() } returns listOf(dummyProject, dummyProject)
        coEvery { projectCliHelper.selectProject(any()) } returns null

        deleteProjectCli.delete()

        verify { uiController.printMessage("no project was selected") }
    }

    @Test
    fun `should print success message if project deleted`() = runTest {
        coEvery { projectCliHelper.getProjects() } returns listOf(dummyProject)
        coEvery { projectCliHelper.selectProject(any()) } returns dummyProject
        coEvery { deleteProjectUseCase.execute(dummyProject.id) } returns true

        deleteProjectCli.delete()

        verify { uiController.printMessage("Project deleted.") }
    }

    @Test
    fun `should print error message if exception is thrown`() = runTest {
        coEvery { projectCliHelper.getProjects() } returns listOf(dummyProject)
        coEvery { projectCliHelper.selectProject(any()) } returns dummyProject
        coEvery { deleteProjectUseCase.execute(dummyProject.id) } throws RuntimeException("DB error")

        deleteProjectCli.delete()

        verify { uiController.printMessage(match { it.contains("$DELETE_PROJECT_FROM DB error") }) }
    }

    @Test
    fun `should print message if no projects found`() = runTest {
        coEvery { projectCliHelper.getProjects() } returns emptyList()
        coEvery { projectCliHelper.selectProject(emptyList()) } returns null

        deleteProjectCli.delete()

        verify { uiController.printMessage("no projects found") }
    }
}