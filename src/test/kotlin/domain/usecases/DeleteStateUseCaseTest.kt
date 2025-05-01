package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.repositories.StateRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test


class DeleteStateUseCaseTest {
    private var repository: StateRepository = mockk()
    private lateinit var deleteStateUseCase: DeleteStateUseCase

    @BeforeEach
    fun setUp() {
        deleteStateUseCase = DeleteStateUseCase(repository)
    }

    @Test
    fun `should delete state successfully when repository returns true`() {
        val stateId = "1"
        every { repository.deleteState(stateId) } returns true

        val result = deleteStateUseCase.execute(stateId)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when repository fails to delete the state`() {
        val stateId = "99"
        every { repository.deleteState(stateId) } returns false

        val result = deleteStateUseCase.execute(stateId)

        assertThat(result).isFalse()
    }
}