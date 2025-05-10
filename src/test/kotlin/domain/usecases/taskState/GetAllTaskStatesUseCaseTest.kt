package domain.usecases.taskState

import com.google.common.truth.Truth.assertThat
import domain.repositories.TaskStateRepository
import dummyData.dummyStateData.DummyTaskState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import java.util.*
import kotlin.test.Test

class GetAllTaskStatesUseCaseTest {
    private var repository: TaskStateRepository = mockk()
    private lateinit var getAllTaskStatesUseCase: GetAllTaskStatesUseCase

    @BeforeEach
    fun setUp() {
        getAllTaskStatesUseCase = GetAllTaskStatesUseCase(repository)
    }

    @Test
    fun `should return all task states from repository when they exist`() = runTest{
        val projectId = UUID.randomUUID()
        val allTaskStates = listOf(
            DummyTaskState.inProgress.copy(projectId = projectId),
            DummyTaskState.readyForReview.copy(projectId = projectId)
        )
        coEvery { repository.getAllTaskStates() } returns allTaskStates

        val result = getAllTaskStatesUseCase.execute(projectId)

        assertThat(result).hasSize(2)
    }

    @Test
    fun `should return an empty list when no task states exist`() = runTest {
        coEvery { repository.getAllTaskStates() } returns emptyList()

        val result = getAllTaskStatesUseCase.execute(UUID.randomUUID())

        assertThat(result).isEmpty()
    }


}
