package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.repositories.TaskStateRepository
import dummyStateData.DummyState
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class EditTaskStateUseCaseTest {
    private var repository: TaskStateRepository = mockk()
    private lateinit var editStateUseCase: EditTaskStateUseCase

    @BeforeEach
    fun setUp() {
        editStateUseCase = EditTaskStateUseCase(repository)
    }

    @Test
    fun `should edit state successfully when repository returns true`() {
        val updatedState = DummyState.inProgress

        every { repository.editState(updatedState) } returns true

        val result = editStateUseCase.execute(updatedState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should fail to edit state when repository returns false`() {
        val updatedState = DummyState.inProgress

        every { repository.editState(updatedState) } returns false

        val result = editStateUseCase.execute(updatedState)

        assertThat(result).isFalse()
    }
}
