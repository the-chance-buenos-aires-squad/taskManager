package presentation.cli.project

import com.google.common.truth.Truth.assertThat
import domain.usecases.project.UpdateProjectUseCase
import dummyData.createDummyProject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController
import presentation.cli.helper.ProjectCliHelper

class UpdateProjectCliTest {

    private val updateProjectUseCase: UpdateProjectUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private val projectCliHelper: ProjectCliHelper = mockk()
    private lateinit var updateProjectCli: UpdateProjectCli

    private val dummyProject = createDummyProject()

    @BeforeEach
    fun setup() {
        updateProjectCli = UpdateProjectCli(updateProjectUseCase, projectCliHelper, uiController)
    }

    @Test
    fun `should return early when no project is selected`() = runTest {
        // Arrange
        coEvery { projectCliHelper.getProjects() } returns listOf(dummyProject)
        every { projectCliHelper.selectProject(any()) } returns null

        // Act
        updateProjectCli.update()

        // Assert
        coVerify(exactly = 0) { updateProjectUseCase.execute(any()) }
    }

    @Test
    fun `should update project when valid data is entered`() = runTest {
        // Arrange
        coEvery { projectCliHelper.getProjects() } returns listOf(dummyProject)
        every { projectCliHelper.selectProject(any()) } returns dummyProject
        every { uiController.readInput() } returnsMany listOf("Updated Project", "Updated Description")
        coEvery { updateProjectUseCase.execute(any()) } returns true

        // Act
        updateProjectCli.update()

        // Assert
        coVerify {
            updateProjectUseCase.execute(withArg { updated ->
                assertThat(updated.title).isEqualTo("Updated Project")
                assertThat(updated.description).isEqualTo("Updated Description")
            })
        }
    }

    @Test
    fun `should show message when updateProjectUseCase fails`() = runTest {
        // Arrange
        coEvery { projectCliHelper.getProjects() } returns listOf(dummyProject)
        every { projectCliHelper.selectProject(any()) } returns dummyProject
        every { uiController.readInput() } returnsMany listOf("name", "desc")
        coEvery { updateProjectUseCase.execute(any()) } throws RuntimeException("DB error")

        // Act
        updateProjectCli.update()

        // Assert
        coVerify {
            uiController.printMessage("Error updating project: DB error")
        }
    }


    @Test
    fun `should print message and return early if no projects found`() = runTest {
        val projectCliHelper = mockk<ProjectCliHelper>()
        val updateProjectUseCase = mockk<UpdateProjectUseCase>(relaxed = true)
        val uiController = mockk<UiController>(relaxed = true)
        val updateProjectCli = UpdateProjectCli(updateProjectUseCase, projectCliHelper, uiController)

        coEvery { projectCliHelper.getProjects() } returns emptyList()

        updateProjectCli.update()

        coVerify { uiController.printMessage("No projects found.") }
        coVerify(exactly = 0) { updateProjectUseCase.execute(any()) }
    }

    @Test
    fun `should print message and return early if name is empty`() = runTest {
        val dummyProject = createDummyProject()
        val projectCliHelper = mockk<ProjectCliHelper>()
        val updateProjectUseCase = mockk<UpdateProjectUseCase>(relaxed = true)
        val uiController = mockk<UiController>(relaxed = true)
        val updateProjectCli = UpdateProjectCli(updateProjectUseCase, projectCliHelper, uiController)

        coEvery { projectCliHelper.getProjects() } returns listOf(dummyProject)
        coEvery { projectCliHelper.selectProject(any()) } returns dummyProject
        every { uiController.readInput() } returns ""

        updateProjectCli.update()

        coVerify { uiController.printMessage("Enter new name:") }
        coVerify { uiController.printMessage("New name can't be empty") }
        coVerify(exactly = 0) { updateProjectUseCase.execute(any()) }
    }

    @Test
    fun `should print message and return early if description is empty`() = runTest {
        val dummyProject = createDummyProject()
        val projectCliHelper = mockk<ProjectCliHelper>()
        val updateProjectUseCase = mockk<UpdateProjectUseCase>(relaxed = true)
        val uiController = mockk<UiController>(relaxed = true)
        val updateProjectCli = UpdateProjectCli(updateProjectUseCase, projectCliHelper, uiController)

        coEvery { projectCliHelper.getProjects() } returns listOf(dummyProject)
        coEvery { projectCliHelper.selectProject(any()) } returns dummyProject
        every { uiController.readInput() } returnsMany listOf("New Project", "")

        updateProjectCli.update()

        coVerify { uiController.printMessage("Enter new name:") }
        coVerify { uiController.printMessage("Enter project description:") }
        coVerify { uiController.printMessage("Description can't be empty") }
        coVerify(exactly = 0) { updateProjectUseCase.execute(any()) }
    }
}