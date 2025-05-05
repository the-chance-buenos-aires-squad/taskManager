import com.google.common.base.Verify.verify
import domain.customeExceptions.NoTasksFoundException
import domain.usecases.task.DeleteTaskUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import presentation.UiController
import presentation.cli.task.DeleteTaskCli
import java.util.*
import kotlin.test.Test

class DeleteTaskCliTest {

    private val deleteTaskUseCase = mockk<DeleteTaskUseCase>()
    private val getAllTaskStatesUseCase = mockk<GetAllTaskStatesUseCase>()
    private val uiController = mockk<UiController>(relaxed = true)

    private lateinit var cli: DeleteTaskCli

    private val sampleTaskId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        cli = DeleteTaskCli(
            getAllTaskStatesUseCase = getAllTaskStatesUseCase,
            deleteTaskUseCase = deleteTaskUseCase,
            uiController = uiController
        )
    }

    @Test
    fun `should delete task successfully when user confirms`() {
        // given
        every { getAllTaskStatesUseCase.execute() } returns listOf()
        every { uiController.readInput() } returns "1" andThen "yes"
        every { deleteTaskUseCase.deleteTask(sampleTaskId) } returns true

        // when
        cli.delete()

        // then
        verify { deleteTaskUseCase.deleteTask(sampleTaskId) }
        verify { uiController.printMessage(match { it.contains("Task deleted successfully") }) }
    }

    @Test
    fun `should not delete task when user cancels`() {
        // given
        every { getAllTaskStatesUseCase.execute() } returns listOf()
        every { uiController.readInput() } returns "1" andThen "no"

        // when
        cli.delete()

        // then
        verify(exactly = 0) { deleteTaskUseCase.deleteTask(any()) }
        verify { uiController.printMessage(match { it.contains("Deletion cancelled") }) }
    }

    @Test
    fun `should throw NoTasksFoundException when no tasks exist`() {
        // given
        every { getAllTaskStatesUseCase.execute() } throws NoTasksFoundException()

        // when & then
        assertThrows<NoTasksFoundException> {
            cli.delete()
        }
    }
}
