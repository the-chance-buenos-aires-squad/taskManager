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

class ExistsTaskStateUseCaseTest {
    private var repository: TaskStateRepository = mockk()
    private lateinit var existsTaskStateUseCase: ExistsTaskStateUseCase

    @BeforeEach
    fun setUp() {
        existsTaskStateUseCase = ExistsTaskStateUseCase(repository)
    }

    @Test
    fun `should return true when state with given id exists`() = runTest {
        coEvery { repository.existsTaskState(any(), any()) } returns true

        val result = existsTaskStateUseCase.execute(DummyTaskState.todo.name, DummyTaskState.todo.projectId)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when state with given id does not exist`() = runTest {
        val nonExistName = "nonExistName"
        coEvery { repository.existsTaskState(any(), any()) } returns false

        val result = existsTaskStateUseCase.execute(nonExistName, UUID.randomUUID())

        assertThat(result).isFalse()
    }
}