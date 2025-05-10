package domain.usecases.taskState

import com.google.common.truth.Truth.assertThat
import domain.repositories.TaskStateRepository
import dummyData.dummyStateData.DummyTaskState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class DeleteTaskStateUseCaseTest {
    private var repository: TaskStateRepository = mockk()
    private lateinit var deleteTaskStateUseCase: DeleteTaskStateUseCase

    @BeforeEach
    fun setUp() {
        deleteTaskStateUseCase = DeleteTaskStateUseCase(repository)
    }

    @Test
    fun `should edit state successfully when repository returns true`() = runTest {
        val deletedTaskState = DummyTaskState.todo.id

        coEvery { repository.deleteTaskState(deletedTaskState) } returns true

        val result = deleteTaskStateUseCase.execute(deletedTaskState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should fail to edit state when repository returns false`() = runTest {
        val deletedTaskState = DummyTaskState.blocked.id

        coEvery { repository.deleteTaskState(deletedTaskState) } returns false

        val result = deleteTaskStateUseCase.execute(deletedTaskState)

        assertThat(result).isFalse()
    }
}