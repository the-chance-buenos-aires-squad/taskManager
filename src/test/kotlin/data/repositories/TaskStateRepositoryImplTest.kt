import auth.UserSession
import com.google.common.truth.Truth.assertThat
import data.dataSource.taskState.TaskStateCSVDataSource
import data.dto.TaskStateDto
import data.exceptions.TaskStateNameException
import data.repositories.TaskStateRepositoryImpl
import data.repositories.dataSource.TaskStateDataSource
import data.repositories.mappers.TaskStateDtoMapper
import domain.entities.TaskState
import domain.entities.User
import domain.entities.UserRole
import domain.repositories.AuditRepository
import domain.repositories.AuthRepository
import dummyData.DummyUser.dummyUserOne
import dummyData.createDummyProject
import dummyData.dummyStateData.DummyTaskState
import io.mockk.MockKException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.exceptions.UserNotLoggedInException
import java.time.LocalDateTime
import java.util.*

class TaskStateRepositoryImplTest {

    private val mockCSVDataSource: TaskStateDataSource = mockk(relaxed = true)
    private lateinit var taskStateDtoMapper: TaskStateDtoMapper
    private lateinit var stateRepository: TaskStateRepositoryImpl
    private val auditRepository: AuditRepository = mockk()
    private val session: UserSession = mockk()


    @BeforeEach
    fun setUp() {
        taskStateDtoMapper = mockk(relaxed = true)
        stateRepository = TaskStateRepositoryImpl(
            taskStateDataSource = mockCSVDataSource,
            taskStateDtoMapper = taskStateDtoMapper,
            userSession = session,
            auditRepository = auditRepository
        )
    }

    @Test
    fun `should return true when creating a new state`() = runTest {
        initUser()
        val newState = DummyTaskState.readyForReview
        val stateRow = DummyTaskState.readyForReviewDto
        every { taskStateDtoMapper.toEntity(stateRow) } returns newState
        every { taskStateDtoMapper.fromEntity(newState) } returns stateRow
        coEvery { mockCSVDataSource.getTaskStates() } returns emptyList()
        coEvery { mockCSVDataSource.createTaskState(stateRow) } returns true
        coEvery { auditRepository.addAudit(any()) } returns true

        val result = stateRepository.createTaskState(newState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should not allow any repository action if user not logged in`() = runTest {
        //given
        every { session.setCurrentUser(null) }

        assertThrows<MockKException> { stateRepository.createTaskState(DummyTaskState.readyForReview) }
        coVerify(exactly = 0) { mockCSVDataSource.createTaskState(any()) }
    }


    @Test
    fun `should return false when state already exists`() = runTest {
        //given
        initUser()
        val newState = DummyTaskState.readyForReview
        val stateDto = DummyTaskState.readyForReviewDto
        every { taskStateDtoMapper.toEntity(stateDto) } returns newState
        every { taskStateDtoMapper.fromEntity(newState) } returns stateDto
        coEvery { mockCSVDataSource.getTaskStates() } returns listOf(stateDto)
        coEvery { mockCSVDataSource.createTaskState(stateDto) } returns true

        //when && then
        assertThrows<TaskStateNameException> {
            stateRepository.createTaskState(newState)
        }
    }

    @Test
    fun `should create when existing states but none match projectId and name`() = runTest {
        initUser()
        val otherDto = DummyTaskState.todoDto.copy(projectId = UUID.randomUUID().toString())
        coEvery { mockCSVDataSource.getTaskStates() } returns listOf(otherDto)
        coEvery { mockCSVDataSource.createTaskState(any()) } returns true
        coEvery { auditRepository.addAudit(any()) } returns true

        val result = stateRepository.createTaskState(DummyTaskState.readyForReview)

        assertThat(result).isTrue()
    }

    @Test
    fun `should edit state successfully when this state is existing`() = runTest {
        initUser()
        val todoState = DummyTaskState.todoDto
        val updatedToDoState = TaskState(UUID.randomUUID(), "In Progress", UUID.randomUUID())
        val updatedStateRow =
            TaskStateDto(updatedToDoState.id.toString(), updatedToDoState.title, updatedToDoState.projectId.toString())
        every { taskStateDtoMapper.toEntity(updatedStateRow) } returns updatedToDoState
        every { taskStateDtoMapper.fromEntity(updatedToDoState) } returns updatedStateRow
        coEvery { mockCSVDataSource.getTaskStates() } returns listOf<TaskStateDto>(todoState)
        coEvery { mockCSVDataSource.editTaskState(updatedStateRow) } returns true
        coEvery { auditRepository.addAudit(any()) } returns true

        val result = stateRepository.editTaskState(updatedToDoState)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when editing non existent state`() = runTest {
        initUser()
        val nonExistentState = TaskState(UUID.randomUUID(), "Non-existent State", UUID.randomUUID())
        coEvery { mockCSVDataSource.getTaskStates() } returns emptyList()

        val result = stateRepository.editTaskState(nonExistentState)

        assertThat(result).isFalse()
    }

    @Test
    fun `should not update state when projectId does not match`() = runTest {
        initUser()
        val todoState = DummyTaskState.todoDto
        val updatedDoState = TaskState(UUID.randomUUID(), "In Progress", UUID.randomUUID())

        coEvery { mockCSVDataSource.getTaskStates() } returns listOf(todoState)

        val result = stateRepository.editTaskState(updatedDoState)

        assertThat(result).isFalse()
    }

    @Test
    fun `should delete state successfully when state exists`() = runTest {
        initUser()
        coEvery { mockCSVDataSource.deleteTaskState(DummyTaskState.done.id.toString()) } returns true
        coEvery { auditRepository.addAudit(any()) } returns true

        val result = stateRepository.deleteTaskState(DummyTaskState.done.id)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when delete non-existing state`() = runTest {
        initUser()
        coEvery { mockCSVDataSource.getTaskStates() } returns listOf(DummyTaskState.readyForReviewDto)
        coEvery { auditRepository.addAudit(any()) } returns true

        val result = stateRepository.deleteTaskState(UUID.randomUUID())

        assertThat(result).isFalse()
    }

    @Test
    fun `should return false when states list is empty`() = runTest {
        initUser()
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


    private fun initUser() {
        every { session.setCurrentUser(adminUser) }
        coEvery { session.runIfLoggedIn(any<suspend (User) -> Boolean>()) } coAnswers {
            val action = firstArg<suspend (User) -> Boolean>()
            action(adminUser)
        }
    }

    private val adminUser = User(
        id = UUID.randomUUID(),
        username = "admin",
        role = UserRole.ADMIN,
        createdAt = LocalDateTime.now()
    )
}