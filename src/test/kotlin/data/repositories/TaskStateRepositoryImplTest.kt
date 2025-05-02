import com.google.common.truth.Truth.assertThat
import data.dataSource.TaskStateCSVDataSource
import data.repositories.TaskStateRepositoryImpl
import domain.entities.TaskState
import dummyStateData.DummyTaskState
import io.mockk.*
import org.junit.jupiter.api.*

class TaskStateRepositoryImplTest {

    private val mockCSVDataSource = mockk<TaskStateCSVDataSource>(relaxed = true)
    private lateinit var stateRepository: TaskStateRepositoryImpl

    @BeforeEach
    fun setUp() {
        stateRepository = TaskStateRepositoryImpl(mockCSVDataSource)
    }

    @Test
    fun `should return true when creating a new state`() {
        val newState = DummyTaskState.readyForReview
        every { mockCSVDataSource.createTaskState(newState) } returns true

        val result = stateRepository.createTaskState(newState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when state already exists`() {
        val existingState = DummyTaskState.todo
        every { mockCSVDataSource.createTaskState(existingState) } returns false

        val result = stateRepository.createTaskState(existingState)

        assertThat(result).isFalse()
    }

    @Test
    fun `should edit state successfully when this state is existing`() {
        val todoState = DummyTaskState.todo
        val updatedToDoState = TaskState("1", "In Progress", "P001")

        every { mockCSVDataSource.getAllTaskStates() } returns listOf(todoState)
        every { mockCSVDataSource.editTaskState(updatedToDoState) } returns true

        val result = stateRepository.editTaskState(updatedToDoState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when editing non existent state`() {
        val nonExistentState = TaskState("99", "Non-existent State", "P12")
        every { mockCSVDataSource.getAllTaskStates() } returns emptyList()

        val result = stateRepository.editTaskState(nonExistentState)

        assertThat(result).isFalse()
    }

    @Test
    fun `should not update state when projectId does not match`() {
        val todoState = DummyTaskState.done
        val updatedDoState = TaskState("3", "In Progress", "P012")

        every { mockCSVDataSource.getAllTaskStates() } returns listOf(todoState)

        val result = stateRepository.editTaskState(updatedDoState)

        assertThat(result).isFalse()
    }

    @Test
    fun `should delete state successfully when state exists`() {
        every { mockCSVDataSource.deleteTaskState("4") } returns true

        val result = stateRepository.deleteTaskState("4")

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when delete non-existing state`() {
        every { mockCSVDataSource.getAllTaskStates() } returns listOf(DummyTaskState.readyForReview)

        val result = stateRepository.deleteTaskState("99")

        assertThat(result).isFalse()
    }

    @Test
    fun `should return false when states list is empty`() {
        every { mockCSVDataSource.getAllTaskStates() } returns emptyList()

        val result = stateRepository.deleteTaskState("1")

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
        every { mockCSVDataSource.existsTaskState(DummyTaskState.todo.id) } returns true

        val result = stateRepository.existsTaskState(DummyTaskState.todo.id)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when the state does not exist`() {
        every { mockCSVDataSource.existsTaskState("99") } returns false

        val result = stateRepository.existsTaskState("99")

        assertThat(result).isFalse()
    }


}