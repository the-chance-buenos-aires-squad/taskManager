package presentation.Cli.TaskState

import com.google.common.truth.Truth.assertThat
import domain.usecases.GetAllTaskStatesUseCase
import dummyStateData.DummyTaskState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class GetAllTaskStatesCliTest {
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase = mockk()
    private lateinit var getAllTaskStatesCli: GetAllTaskStatesCli

    @BeforeEach
    fun setup() {
        getAllTaskStatesCli = GetAllTaskStatesCli(getAllTaskStatesUseCase)
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
        assertThat(result).isEmpty()
    }

}