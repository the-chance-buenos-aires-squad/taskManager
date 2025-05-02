package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.repositories.TaskStateRepository
import dummyStateData.DummyTaskState
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class DeleteTaskStateUseCaseTest{
  private var repository: TaskStateRepository = mockk()
  private lateinit var deleteTaskStateUseCase: DeleteTaskStateUseCase

  @BeforeEach
  fun setUp() {
   deleteTaskStateUseCase = DeleteTaskStateUseCase(repository)
  }

  @Test
  fun `should edit state successfully when repository returns true`() {
   val deletedTaskState = DummyTaskState.todo.id

   every { repository.deleteTaskState(deletedTaskState) } returns true

   val result = deleteTaskStateUseCase.execute(deletedTaskState)

   assertThat(result).isTrue()
  }

  @Test
  fun `should fail to edit state when repository returns false`() {
   val deletedTaskState = DummyTaskState.blocked.id

   every { repository.deleteTaskState(deletedTaskState) } returns false

   val result = deleteTaskStateUseCase.execute(deletedTaskState)

   assertThat(result).isFalse()
  }
 }