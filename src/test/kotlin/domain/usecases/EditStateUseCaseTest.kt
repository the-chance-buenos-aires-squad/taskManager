package domain.usecases

import domain.repositories.StateRepository
import dummyStateData.DummyState
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class EditStateUseCaseTest {
    private var repository: StateRepository = mockk()
    private lateinit var editStateUseCase: EditStateUseCase

    @BeforeEach
    fun setUp() {
        editStateUseCase = EditStateUseCase(repository)
    }

    @Test
    fun `should edit state successfully when repository returns true`() {
        val updatedState = DummyState.inProgress

        every { repository.editState(updatedState) } returns true

        val result = editStateUseCase.execute(updatedState)

        assertTrue(result)
    }

    @Test
    fun `should fail to edit state when repository returns false`() {
        val updatedState = DummyState.inProgress

        every { repository.editState(updatedState) } returns false

        val result = editStateUseCase.execute(updatedState)

        assertFalse(result)
    }
}
