package domain.usecases.taskState

import com.google.common.truth.Truth.assertThat
import domain.repositories.TaskStateRepository
import dummyData.dummyStateData.DummyTaskState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class EditTaskStateUseCaseTest {
    private var repository: TaskStateRepository = mockk()
    private lateinit var editTaskStateUseCase: EditTaskStateUseCase

    @BeforeEach
    fun setUp() {
        editTaskStateUseCase = EditTaskStateUseCase(repository)
    }

    @Test
    fun `should edit state successfully when repository returns true`() = runTest {
        val updatedState = DummyTaskState.inProgress

        coEvery { repository.editTaskState(updatedState) } returns true

        val result = editTaskStateUseCase.execute(updatedState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should fail to edit state when repository returns false`() = runTest {
        val updatedState = DummyTaskState.inProgress

        coEvery { repository.editTaskState(updatedState) } returns false

        val result = editTaskStateUseCase.execute(updatedState)

        assertThat(result).isFalse()
    }
}
