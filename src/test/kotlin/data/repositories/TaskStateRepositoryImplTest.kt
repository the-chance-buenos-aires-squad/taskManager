import com.google.common.truth.Truth.assertThat
import data.dataSource.taskState.TaskStateCSVDataSource
import data.repositories.TaskStateRepositoryImpl
import data.repositories.mappers.TaskStateMapper
import domain.entities.TaskState
import dummyData.dummyStateData.DummyTaskState
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class TaskStateRepositoryImplTest {

    private val mockCSVDataSource = mockk<TaskStateCSVDataSource>(relaxed = true)
    private lateinit var taskStateMapper: TaskStateMapper
    private lateinit var stateRepository: TaskStateRepositoryImpl

    @BeforeEach
    fun setUp() {
        taskStateMapper = TaskStateMapper()
        stateRepository = TaskStateRepositoryImpl(mockCSVDataSource, taskStateMapper)
    }

    @Test
    fun `should return true when creating a new state`() {
        val newState = DummyTaskState.readyForReview
        val stateRow = listOf(newState.id.toString(), newState.name, newState.projectId.toString())

        every { mockCSVDataSource.createTaskState(stateRow) } returns true

        val result = stateRepository.createTaskState(newState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when state already exists`() {
        val existingState = DummyTaskState.todo
        val stateRow = listOf(existingState.id.toString(), existingState.name, existingState.projectId.toString())

        every { mockCSVDataSource.createTaskState(stateRow) } returns false

        val result = stateRepository.createTaskState(existingState)

        assertThat(result).isFalse()
    }

    @Test
    fun `should edit state successfully when this state is existing`() {
        val todoState = DummyTaskState.todo
        val updatedToDoState = TaskState(UUID.randomUUID(), "In Progress", UUID.randomUUID())
        val updatedStateRow =
            listOf(updatedToDoState.id.toString(), updatedToDoState.name, updatedToDoState.projectId.toString())

        every { mockCSVDataSource.getAllTaskStates() } returns listOf(todoState)
        every { mockCSVDataSource.editTaskState(updatedStateRow) } returns true

        val result = stateRepository.editTaskState(updatedToDoState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when editing non existent state`() {
        val nonExistentState = TaskState(UUID.randomUUID(), "Non-existent State", UUID.randomUUID())
        every { mockCSVDataSource.getAllTaskStates() } returns emptyList()

        val result = stateRepository.editTaskState(nonExistentState)

        assertThat(result).isFalse()
    }

    @Test
    fun `should not update state when projectId does not match`() {
        val todoState = DummyTaskState.done
        val updatedDoState = TaskState(UUID.randomUUID(), "In Progress", UUID.randomUUID())

        every { mockCSVDataSource.getAllTaskStates() } returns listOf(todoState)

        val result = stateRepository.editTaskState(updatedDoState)

        assertThat(result).isFalse()
    }

    @Test
    fun `should delete state successfully when state exists`() {
        every { mockCSVDataSource.deleteTaskState(DummyTaskState.done.id) } returns true

        val result = stateRepository.deleteTaskState(DummyTaskState.done.id)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when delete non-existing state`() {
        every { mockCSVDataSource.getAllTaskStates() } returns listOf(DummyTaskState.readyForReview)

        val result = stateRepository.deleteTaskState(UUID.randomUUID())

        assertThat(result).isFalse()
    }

    @Test
    fun `should return false when states list is empty`() {
        every { mockCSVDataSource.getAllTaskStates() } returns emptyList()

        val result = stateRepository.deleteTaskState(UUID.randomUUID())

        assertThat(result).isFalse()
    }

    @Test
    fun `should return all states when they exist`() {
        every { mockCSVDataSource.getAllTaskStates() } returns
                listOf(DummyTaskState.readyForReview, DummyTaskState.blocked)

        val result = stateRepository.getAllTaskStates()

        assertThat(result).hasSize(2)
    }

    @Test
    fun `should return an empty list when no states exist`() {
        every { mockCSVDataSource.getAllTaskStates() } returns emptyList()

        val result = stateRepository.getAllTaskStates()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return true when the state exists`() {
        every {
            mockCSVDataSource.existsTaskState(
                DummyTaskState.todo.name.toString(),
                DummyTaskState.todo.projectId
            )
        } returns true

        val result = stateRepository.existsTaskState(DummyTaskState.todo.name, DummyTaskState.todo.projectId)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when the state does not exist`() {
        every { mockCSVDataSource.existsTaskState(any(), any()) } returns false

        val result = stateRepository.existsTaskState("NonExists", UUID.randomUUID())

        assertThat(result).isFalse()
    }


}