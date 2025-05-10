package domain.usecases.taskState

import com.google.common.truth.Truth.assertThat
import domain.repositories.TaskStateRepository
import dummyData.dummyStateData.DummyTaskState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class CreateTaskStateUseCaseTest {

    private var repository: TaskStateRepository = mockk()
    private lateinit var createTaskStateUseCase: CreateTaskStateUseCase

    @BeforeTest
    fun setUp() {
        createTaskStateUseCase = CreateTaskStateUseCase(repository)
    }

    @Test
    fun `should create state successfully when repository returns true`() = runTest{
        val newState = DummyTaskState.inProgress

        coEvery { repository.createTaskState(newState) } returns true

        val result = createTaskStateUseCase.CreateTask(newState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should fail to create state when repository returns false`() = runTest{
        val newState = DummyTaskState.inProgress


        coEvery { repository.createTaskState(newState) } returns false

        val result = createTaskStateUseCase.CreateTask(newState)

        assertThat(result).isFalse()
    }
}