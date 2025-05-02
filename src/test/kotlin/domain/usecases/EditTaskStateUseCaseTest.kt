package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.repositories.TaskStateRepository
import dummyStateData.DummyTaskState
import io.mockk.every
import io.mockk.mockk
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
    fun `should edit state successfully when repository returns true`() {
        val updatedState = DummyTaskState.inProgress

        every { repository.editTaskState(updatedState) } returns true

        val result = editTaskStateUseCase.execute(updatedState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should fail to edit state when repository returns false`() {
        val updatedState = DummyTaskState.inProgress

        every { repository.editTaskState(updatedState) } returns false

        val result = editTaskStateUseCase.execute(updatedState)

        assertThat(result).isFalse()
    }
}
