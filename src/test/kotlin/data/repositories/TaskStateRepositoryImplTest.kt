import com.google.common.truth.Truth.assertThat
import data.dataSource.TaskStateCSVDataSource
import data.repositories.TaskStateRepositoryImpl
import domain.entities.State
import dummyStateData.DummyState
import io.mockk.*
import org.junit.jupiter.api.*

class TaskStateRepositoryImplTest {

    private val mockCSVDataSource = mockk<TaskStateCSVDataSource>(relaxed = true)
    private lateinit var stateRepository: TaskStateRepositoryImpl

    @BeforeEach
    fun setUp() {
        stateRepository = TaskStateRepositoryImpl(mockCSVDataSource)
    }

    // TODO: Write below all test for createState fun. (write here shahed)



    @Test
    fun `should edit state successfully when this state is existing`() {
        val todoState = DummyState.todo
        val updatedToDoState = State("1", "In Progress", "P001")

        every { mockCSVDataSource.getAllTaskStates() } returns listOf(todoState)
        every { mockCSVDataSource.editTaskState(updatedToDoState) } returns true

        val result = stateRepository.editTaskState(updatedToDoState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when editing non existent state`() {
        val nonExistentState =  State("99", "Non-existent State", "P12")
        every { mockCSVDataSource.getAllTaskStates() } returns emptyList()

        val result = stateRepository.editTaskState(nonExistentState)

        assertThat(result).isFalse()
    }

    @Test
    fun `should not update state when projectId does not match`() {
        val todoState = DummyState.done
        val updatedDoState =  State("3", "In Progress", "P012")

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
        every { mockCSVDataSource.getAllTaskStates() } returns listOf(DummyState.readyForReview)

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
                listOf(DummyState.readyForReview, DummyState.blocked)

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
        every { mockCSVDataSource.existsTaskState(DummyState.todo.id) } returns true

        val result = stateRepository.existsTaskState(DummyState.todo.id)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when the state does not exist`() {
        every { mockCSVDataSource.existsTaskState("99") } returns false

        val result = stateRepository.existsTaskState("99")

        assertThat(result).isFalse()
    }


}