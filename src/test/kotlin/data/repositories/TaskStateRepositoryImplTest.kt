import com.google.common.truth.Truth.assertThat
import data.dataSource.taskState.TaskStateCSVDataSource
import data.dto.TaskStateDto
import data.exceptions.TaskStateNameException
import data.repositories.TaskStateRepositoryImpl
import data.repositories.mappers.TaskStateDtoMapper
import domain.entities.TaskState
import dummyData.dummyStateData.DummyTaskState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class TaskStateRepositoryImplTest {

    private val mockCSVDataSource = mockk<TaskStateCSVDataSource>(relaxed = true)
    private lateinit var taskStateDtoMapper: TaskStateDtoMapper
    private lateinit var stateRepository: TaskStateRepositoryImpl

    @BeforeEach
    fun setUp() {
        taskStateDtoMapper = TaskStateDtoMapper()
        stateRepository = TaskStateRepositoryImpl(
            taskStateCSVDataSource = mockCSVDataSource,
            taskStateDtoMapper = taskStateDtoMapper
        )
    }

    @Test
    fun `should return true when creating a new state`() = runTest {
        val newState = DummyTaskState.readyForReview
        val stateRow = DummyTaskState.readyForReviewDto
        coEvery { mockCSVDataSource.getTaskStates() } returns emptyList()
        coEvery { mockCSVDataSource.createTaskState(stateRow) } returns true

        val result = stateRepository.createTaskState(newState)

        assertThat(result).isTrue()
    }


    @Test
    fun `should return false when state already exists`() = runTest {
        //given
        val newState = DummyTaskState.readyForReview
        val stateDto = DummyTaskState.readyForReviewDto
        coEvery { mockCSVDataSource.getTaskStates() } returns listOf(stateDto)
        coEvery { mockCSVDataSource.createTaskState(stateDto) } returns true

        //when && then
        assertThrows<TaskStateNameException> {
            stateRepository.createTaskState(newState)
        }
    }

    @Test
    fun `should create when existing states but none match projectId and name`() = runTest {
        val otherDto = DummyTaskState.todoDto.copy(projectId = UUID.randomUUID().toString())
        coEvery { mockCSVDataSource.getTaskStates() } returns listOf(otherDto)
        coEvery { mockCSVDataSource.createTaskState(any()) } returns true

        val result = stateRepository.createTaskState(DummyTaskState.readyForReview)

        assertThat(result).isTrue()
    }

    @Test
    fun `should edit state successfully when this state is existing`() = runTest {
        val todoState = DummyTaskState.todoDto
        val updatedToDoState = TaskState(UUID.randomUUID(), "In Progress", UUID.randomUUID())
        val updatedStateRow =
            TaskStateDto(updatedToDoState.id.toString(), updatedToDoState.name, updatedToDoState.projectId.toString())

        coEvery { mockCSVDataSource.getTaskStates() } returns listOf<TaskStateDto>(todoState)
        coEvery { mockCSVDataSource.editTaskState(updatedStateRow) } returns true

        val result = stateRepository.editTaskState(updatedToDoState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when editing non existent state`() = runTest {
        val nonExistentState = TaskState(UUID.randomUUID(), "Non-existent State", UUID.randomUUID())
        coEvery { mockCSVDataSource.getTaskStates() } returns emptyList()

        val result = stateRepository.editTaskState(nonExistentState)

        assertThat(result).isFalse()
    }

    @Test
    fun `should not update state when projectId does not match`() = runTest {
        val todoState = DummyTaskState.todoDto
        val updatedDoState = TaskState(UUID.randomUUID(), "In Progress", UUID.randomUUID())

        coEvery { mockCSVDataSource.getTaskStates() } returns listOf(todoState)

        val result = stateRepository.editTaskState(updatedDoState)

        assertThat(result).isFalse()
    }

    @Test
    fun `should delete state successfully when state exists`() = runTest {
        coEvery { mockCSVDataSource.deleteTaskState(DummyTaskState.done.id.toString()) } returns true

        val result = stateRepository.deleteTaskState(DummyTaskState.done.id)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when delete non-existing state`() = runTest {
        coEvery { mockCSVDataSource.getTaskStates() } returns listOf(DummyTaskState.readyForReviewDto)

        val result = stateRepository.deleteTaskState(UUID.randomUUID())

        assertThat(result).isFalse()
    }

    @Test
    fun `should return false when states list is empty`() = runTest {
        coEvery { mockCSVDataSource.getTaskStates() } returns emptyList()

        val result = stateRepository.deleteTaskState(UUID.randomUUID())

        assertThat(result).isFalse()
    }

    @Test
    fun `should return all states when they exist`() = runTest {
        coEvery { mockCSVDataSource.getTaskStates() } returns
                listOf(DummyTaskState.readyForReviewDto, DummyTaskState.blockedDto)

        val result = stateRepository.getAllTaskStates()

        assertThat(result).hasSize(2)
    }

    @Test
    fun `should return an empty list when no states exist`() = runTest {
        coEvery { mockCSVDataSource.getTaskStates() } returns emptyList()

        val result = stateRepository.getAllTaskStates()

        assertThat(result).isEmpty()
    }


}