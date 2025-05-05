package domain.usecases.taskState

import com.google.common.truth.Truth.assertThat
import domain.repositories.TaskStateRepository
import domain.usecases.taskState.ExistsTaskStateUseCase
import dummyData.dummyStateData.DummyTaskState
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
        val nameState = DummyTaskState.todo.name
        val projectIdState = DummyTaskState.todo.projectId
        every { repository.existsTaskState(nameState,projectIdState) } returns true

        val result = existsTaskStateUseCase.execute(nameState,projectIdState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when state with given id does not exist`() {
        val nonExistName = "nonExistName"
        val nonExitProjectId = "nonExitProjectId"
        every { repository.existsTaskState(nonExistName,nonExitProjectId) } returns false

        val result = existsTaskStateUseCase.execute(nonExistName,nonExitProjectId)

        assertThat(result).isFalse()
    }
}

