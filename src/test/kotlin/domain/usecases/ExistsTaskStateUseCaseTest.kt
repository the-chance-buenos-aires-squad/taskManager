package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.repositories.TaskStateRepository
import dummyStateData.DummyTaskState
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class ExistsTaskStateUseCaseTest {
    private var repository: TaskStateRepository = mockk()
    private lateinit var existsTaskStateUseCase: ExistsTaskStateUseCase

    @BeforeEach
    fun setUp() {
        existsTaskStateUseCase = ExistsTaskStateUseCase(repository)
    }

    @Test
    fun `should return true when state with given id exists`() {
        val stateId = DummyTaskState.todo.id
        every { repository.existsTaskState(stateId) } returns true

        val result = existsTaskStateUseCase.execute(stateId)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when state with given id does not exist`() {
        val nonExistId = "9"
        every { repository.existsTaskState(nonExistId) } returns false

        val result = existsTaskStateUseCase.execute(nonExistId)

        assertThat(result).isFalse()
    }
}

