package presentation.cli.taskState

import domain.usecases.taskState.GetAllTaskStatesUseCase
import dummyData.dummyStateData.DummyTaskState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.UiController
import java.util.*
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
    fun `should return list of task states when available`() = runTest {
        coEvery { getAllTaskStatesUseCase.execute(any()) } returns listOf(
            DummyTaskState.todo,
            DummyTaskState.blocked
        )

        getAllTaskStatesCli.getAllTaskStates(UUID.randomUUID())

        coVerify { getAllTaskStatesUseCase.execute(any()) }
    }

    @Test
    fun `should return empty list when no task states exist`() = runTest {
        coEvery { getAllTaskStatesUseCase.execute(any()) } returns emptyList()

        val result = getAllTaskStatesCli.getAllTaskStates(UUID.randomUUID())

        coEvery { getAllTaskStatesUseCase.execute(any()) }
    }

}