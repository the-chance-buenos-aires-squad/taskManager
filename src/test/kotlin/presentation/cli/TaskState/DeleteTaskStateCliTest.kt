package presentation.cli.TaskState

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.InvalidIdException
import domain.usecases.taskState.DeleteTaskStateUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import presentation.UiController
import kotlin.test.Test
import java.util.UUID


class DeleteTaskStateCliTest {
    private val deleteTaskStateUseCase: DeleteTaskStateUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private lateinit var deleteTaskStateCli: DeleteTaskStateCli


    @BeforeEach
    fun setup() {
        deleteTaskStateCli = DeleteTaskStateCli(deleteTaskStateUseCase, uiController)
    }


    @Test
    fun `should call execute when delete task state successfully`() {
        every { uiController.readInput() } returns "1"
        every { deleteTaskStateUseCase.execute(UUID.fromString("00000000-1000-0000-0000-000000000000")) } returns true

        deleteTaskStateCli.deleteTaskState(UUID.fromString("00000000-1000-1000-0000-000000000000"),)

        verify { deleteTaskStateUseCase.execute(any()) }
    }

    @Test
    fun `should call execute when failed to delete task state`() {
        every { uiController.readInput() } returns "2"
        every { deleteTaskStateUseCase.execute(UUID.fromString("00000000-1000-0000-0000-000000000000"),) } returns false

        deleteTaskStateCli.deleteTaskState(UUID.fromString("11000000-1000-0000-0000-000000000000"))

        verify { deleteTaskStateUseCase.execute(any()) }
    }

    @Test
    fun `should throw exception when ID is empty`() {
        every { uiController.readInput() } returns ""

        val exception = assertThrows<InvalidIdException> {
            deleteTaskStateCli.deleteTaskState(UUID.randomUUID())
        }

        assertThat(exception.message).contains("ID can't be empty")
    }
}