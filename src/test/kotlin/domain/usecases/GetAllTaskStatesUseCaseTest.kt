package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.repositories.TaskStateRepository
import dummyStateData.DummyState
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class GetAllTaskStatesUseCaseTest {
    private var repository: TaskStateRepository = mockk()
    private lateinit var getAllTaskStatesUseCase: GetAllTaskStatesUseCase

    @BeforeEach
    fun setUp() {
        getAllTaskStatesUseCase = GetAllTaskStatesUseCase(repository)
    }

    @Test
    fun `should return all task states from repository when they exist`() {
        val allTaskStates = listOf(DummyState.inProgress, DummyState.readyForReview)
        every { repository.getAllTaskStates() } returns allTaskStates


        val result = getAllTaskStatesUseCase.execute()

        assertThat(result).hasSize(2)
    }

    @Test
    fun `should return an empty list when no task states exist`() {
        every { repository.getAllTaskStates() } returns emptyList()

        val result = getAllTaskStatesUseCase.execute()

        assertThat(result).isEmpty()
    }
}
