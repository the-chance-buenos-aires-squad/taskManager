package presentation.cli.project

import domain.customeExceptions.NoProjectsFoundException
import domain.customeExceptions.UserEnterInvalidValueException
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController

class ProjectScreenControllerTest {
    private lateinit var projectShowMenu: ProjectShowMenu
    private lateinit var projectScreenController: ProjectScreenController
    private lateinit var createProjectCli: CreateProjectCli
    private lateinit var updateProjectCli: UpdateProjectCli
    private lateinit var deleteProjectCli: DeleteProjectCli
    private lateinit var uiController: UiController
    private lateinit var getAllProjectsCli: GetAllProjectsCli

    @BeforeEach
    fun setup() {
        getAllProjectsCli = mockk(relaxed = true)
        projectShowMenu = mockk(relaxed = true)
        createProjectCli = mockk(relaxed = true)
        updateProjectCli = mockk(relaxed = true)
        deleteProjectCli = mockk(relaxed = true)
        uiController = mockk(relaxed = true)
        projectScreenController = ProjectScreenController(
            projectShowMenu,
            createProjectCli,
            updateProjectCli,
            deleteProjectCli,
            getAllProjectsCli,
            uiController
        )
    }

    @Test
    fun `should call create project function when user input number one`() =runTest{
        every { uiController.readInput() } returns "1" andThen "5"

        projectScreenController.show()

        coVerify { createProjectCli.create() }
    }

    @Test
    fun `should handle createProjectCli with UserEnterInvalidValueException`() = runTest{
        every { uiController.readInput() } returns "1" andThen "5"
        coEvery { createProjectCli.create() } throws UserEnterInvalidValueException("Invalid Input")

        projectScreenController.show()

        verify { uiController.printMessage("Invalid Input") }
    }

    @Test
    fun `should call edit project function when user input number tow`() = runTest{
        every { uiController.readInput() } returns "2" andThen "5"

        projectScreenController.show()

        coVerify { updateProjectCli.update() }
    }

    @Test
    fun `should handle updateProjectCli with UserEnterInvalidValueException`() =runTest{
        every { uiController.readInput() } returns "2" andThen "5"
        coEvery { updateProjectCli.update() } throws UserEnterInvalidValueException("Invalid update")

        projectScreenController.show()

        verify { uiController.printMessage("Invalid update") }
    }

    @Test
    fun `should handle updateProjectCli with NoProjectsFoundException`() =runTest{
        every { uiController.readInput() } returns "2" andThen "5"
        coEvery { updateProjectCli.update() } throws NoProjectsFoundException("No projects")

        projectScreenController.show()

        verify { uiController.printMessage("No projects") }
    }

    @Test
    fun `should call delete project function when user input number three`() = runTest{
        every { uiController.readInput() } returns "3" andThen "5"

        projectScreenController.show()

        coVerify { deleteProjectCli.delete() }
    }

    @Test
    fun `should handle deleteProjectCli with UserEnterInvalidValueException`() =runTest{
        every { uiController.readInput() } returns "3" andThen "5"
        coEvery { deleteProjectCli.delete() } throws UserEnterInvalidValueException("Can't delete")

        projectScreenController.show()

        verify { uiController.printMessage("Can't delete") }
    }

    @Test
    fun `should handle deleteProjectCli with NoProjectsFoundException`() = runTest{
        every { uiController.readInput() } returns "3" andThen "5"
        coEvery { deleteProjectCli.delete() } throws NoProjectsFoundException("Empty list")

        projectScreenController.show()

        verify { uiController.printMessage("Empty list") }
    }

    @Test
    fun `should handle getAllProjectsCli with NoProjectsFoundException`() = runTest{
        every { uiController.readInput() } returns "4" andThen "5"
        coEvery { getAllProjectsCli.getAll() } throws NoProjectsFoundException("Empty list")

        projectScreenController.show()

        verify { uiController.printMessage("Empty list") }
    }

    @Test
    fun `should getAllProjectsCli when projects found`() = runTest{
        every { uiController.readInput() } returns "4" andThen "5"

        projectScreenController.show()

        coVerify { getAllProjectsCli.getAll() }
    }

    @Test
    fun `should handel Invalid Input when user input empty value`() = runTest{
        every { uiController.readInput() } returns "" andThen "5"

        projectScreenController.show()

        verify { uiController.printMessage("Invalid Input should enter number.") }
    }

    @Test
    fun `should handel Invalid Input when user input invalid value`() = runTest{
        every { uiController.readInput() } returns "7" andThen "5"

        projectScreenController.show()

        verify { uiController.printMessage("Invalid Input should enter number in menu.") }
    }

    @Test
    fun `should handel Invalid Input when user input is return null`() = runTest{
        every { uiController.readInput() } returns "j" andThen "5"

        projectScreenController.show()

        verify { uiController.printMessage("Invalid Input should enter number.") }
    }
}