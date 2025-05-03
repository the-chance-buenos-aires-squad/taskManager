package domain.usecases.taskState

import com.google.common.truth.Truth.assertThat
import kotlin.test.BeforeTest
import kotlin.test.Test
import io.mockk.every
import io.mockk.mockk
import domain.repositories.TaskStateRepository
import domain.usecases.taskState.CreateTaskStateUseCase
import dummyData.dummyStateData.DummyTaskState

class CreateTaskStateUseCaseTest {

    private var repository: TaskStateRepository = mockk()
    private lateinit var createTaskStateUseCase: CreateTaskStateUseCase

    @BeforeTest
    fun setUp() {
        createTaskStateUseCase = CreateTaskStateUseCase(repository)
    }

    @Test
    fun `should create state successfully when repository returns true`() {
        val newState = DummyTaskState.inProgress

        every { repository.createTaskState(newState) } returns true

        val result = createTaskStateUseCase.execute(newState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should fail to create state when repository returns false`() {
        val newState = DummyTaskState.inProgress


        every { repository.createTaskState(newState) } returns false

        val result = createTaskStateUseCase.execute(newState)

        assertThat(result).isFalse()
    }
}