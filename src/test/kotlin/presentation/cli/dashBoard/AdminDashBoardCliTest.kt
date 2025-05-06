package presentation.cli.dashBoard

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.UiController
import presentation.cli.GetAllAuditsCli
import presentation.cli.TaskState.TaskStateCliController
import presentation.cli.auth.CreateUserCli
import presentation.cli.project.ProjectScreenController

class AdminDashBoardCliTest {

    private val uiController: UiController = mockk(relaxed = true)
    private val createUserCli: CreateUserCli = mockk(relaxed = true)
    private val projectScreenController: ProjectScreenController = mockk(relaxed = true)
    private val taskStateCliController: TaskStateCliController = mockk(relaxed = true)
    private val auditsCli: GetAllAuditsCli = mockk(relaxed = true)

    private var adminDashBoardCli: AdminDashBoardCli =
        AdminDashBoardCli(uiController, createUserCli, projectScreenController, taskStateCliController, auditsCli)


    @Test
    fun `should display message when start admin cli`() {
        // given:
        every { uiController.readInput() } returns "1" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // then
        verify {
            uiController.printMessage(
                " === Admin Dashboard ===\n" +
                        " 1. Create User Mate\n" +
                        " 2. Manage Project\n" +
                        " 3. Manage task States\n" +
                        " 4. View Audit Logs\n" +
                        " 5. Logout\n"
            )
        }
    }

    @Test
    fun `should start createUserCli when user choose option 1`() {
        // given
        every { uiController.readInput() } returns "1" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // then
        verify { createUserCli.start() }
    }

    @Test
    fun `should start manage project cli when user choose option 2`() {
        // given
        every { uiController.readInput() } returns "2" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // then
        verify { projectScreenController.show() }
    }

    @Test
    fun `should start manageTaskStatesCli when user choose option 3`() {
        // given
        every { uiController.readInput() } returns "3" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // then
        verify { taskStateCliController.start() }
    }

    @Test
    fun `should start ViewAuditLogsCli when user choose option 4`() {
        // given
        every { uiController.readInput() } returns "4" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // then
        verify { auditsCli.displayAllAudits() }
    }

    @Test
    fun `should start Logout when user choose option 5`() {
        // given
        every { uiController.readInput() } returns "5"

        // when
        adminDashBoardCli.start()

        // then
        verify { uiController.printMessage("Logout") }
    }

    @Test
    fun `should restart adminDashBoardCli when invalid input`() {
        // given
        every { uiController.readInput() } returns "99" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // then
        verify { uiController.printMessage("Invalid option!") }
    }

    @Test
    fun `should restart adminDashBoardCli when empty input`() {
        // given
        every { uiController.readInput() } returns "" andThenThrows RuntimeException("Exit loop")

        // when
        assertThrows<RuntimeException> { adminDashBoardCli.start() }

        // then
        verify { uiController.printMessage("Invalid option!") }
    }
}