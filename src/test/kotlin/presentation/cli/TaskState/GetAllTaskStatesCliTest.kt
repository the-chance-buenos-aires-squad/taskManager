package presentation.cli.TaskState

import GetAllTaskStatesCli
import domain.usecases.taskState.GetAllTaskStatesUseCase
import dummyData.dummyStateData.DummyTaskState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import presentation.UiController
import kotlin.test.Test

class GetAllTaskStatesCliTest {
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase = mockk()
    private val uiController: UiController = mockk(relaxed = true)
    private lateinit var getAllTaskStatesCli: GetAllTaskStatesCli

    @BeforeEach
    fun setup() {
        getAllTaskStatesCli = GetAllTaskStatesCli(getAllTaskStatesUseCase, uiController)
    }


    @Test
    fun `should return list of task states when available`() {
        every { getAllTaskStatesUseCase.execute() } returns listOf(
            DummyTaskState.todo,
            DummyTaskState.blocked
        )

        getAllTaskStatesCli.getAllTaskStates()

        verify { getAllTaskStatesUseCase.execute() }
    }

    @Test
    fun `should return empty list when no task states exist`() {
        every { getAllTaskStatesUseCase.execute() } returns emptyList()

        val result = getAllTaskStatesCli.getAllTaskStates()

        verify { getAllTaskStatesUseCase.execute() }
    }

}