package data.repositories

import auth.UserSession
import com.google.common.truth.Truth.assertThat
import data.repositories.dataSource.TaskDataSource
import data.repositories.mappers.TaskDtoMapper
import domain.entities.User
import domain.entities.UserRole
import domain.repositories.AuditRepository
import domain.repositories.UserRepository
import dummyData.DummyTasks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class TaskRepositoryImplTest {

    private val mockTaskDataSource: TaskDataSource = mockk(relaxed = true)
    private val mockTaskMapper: TaskDtoMapper = mockk(relaxed = true)
    private val auditRepository: AuditRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val session: UserSession = mockk()
    private lateinit var taskRepository: TaskRepositoryImpl


    @BeforeEach
    fun setUp() {
        taskRepository = TaskRepositoryImpl(
            taskDataSource = mockTaskDataSource,
            taskMapper = mockTaskMapper,
            userSession = session,
            auditRepository = auditRepository,
            userRepository = userRepository
        )

        initUser()

        // Mock mapper and data source to return expected values
        coEvery { mockTaskDataSource.addTask(any()) } returns true
        coEvery { mockTaskMapper.fromType(any()) } returns DummyTasks.validTaskDto
    }

    @Test
    fun `should return true when task is successfully created`() = runTest {
        // Given
        coEvery { mockTaskDataSource.addTask(any()) } returns true
        coEvery { auditRepository.addAudit(any()) } returns true


        // When
        val result = taskRepository.addTask(
            title = DummyTasks.validTask.title,
            description = DummyTasks.validTask.description,
            projectId = DummyTasks.validTask.projectId,
            stateId = DummyTasks.validTask.stateId,
            assignedTo = DummyTasks.validTask.assignedTo.toString()
        )

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should generate new id when creating a task`() = runTest {
        // Given
        val initialId = UUID.randomUUID()
        val initialTask = DummyTasks.validTask.copy(id = initialId)
        coEvery { mockTaskDataSource.addTask(any()) } returns true
        coEvery { auditRepository.addAudit(any()) } returns true


        // When
        val result = taskRepository.addTask(
            title = initialTask.title,
            description = initialTask.description,
            projectId = initialTask.projectId,
            stateId = initialTask.stateId,
            assignedTo = initialTask.assignedTo.toString()
        )

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should set current time as createdAt when creating a task`() = runTest {
        // Given
        val initialTask = DummyTasks.validTask.copy(createdAt = LocalDateTime.now().minusDays(5))
        coEvery { mockTaskDataSource.addTask(any()) } returns true
        coEvery { auditRepository.addAudit(any()) } returns true

        // When
        val result = taskRepository.addTask(
            title = initialTask.title,
            description = initialTask.description,
            projectId = initialTask.projectId,
            stateId = initialTask.stateId,
            assignedTo = initialTask.assignedTo.toString()
        )

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should set current time as updatedAt when creating a task`() = runTest {
        // Given
        val initialTask = DummyTasks.validTask.copy(updatedAt = LocalDateTime.now().minusDays(5))
        coEvery { mockTaskDataSource.addTask(any()) } returns true
        coEvery { auditRepository.addAudit(any()) } returns true


        // When
        val result = taskRepository.addTask(
            title = initialTask.title,
            description = initialTask.description,
            projectId = initialTask.projectId,
            stateId = initialTask.stateId,
            assignedTo = initialTask.assignedTo.toString()
        )

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should preserve task properties when creating a task`() = runTest {
        // Given
        val initialTask = DummyTasks.validTask.copy(title = "Specific Title", description = "Specific Description")
        coEvery { mockTaskDataSource.addTask(any()) } returns true
        coEvery { auditRepository.addAudit(any()) } returns true


        // When
        val result = taskRepository.addTask(
            title = initialTask.title,
            description = initialTask.description,
            projectId = initialTask.projectId,
            stateId = initialTask.stateId,
            assignedTo = initialTask.assignedTo.toString()
        )

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should call mapper to map task entity to row`() = runTest {
        // Given
        val initialTask = DummyTasks.validTask.copy(title = "Specific Title", description = "Specific Description")
        coEvery { mockTaskDataSource.addTask(any()) } returns true
        coEvery { auditRepository.addAudit(any()) } returns true


        // When
        val result = taskRepository.addTask(
            title = initialTask.title,
            description = initialTask.description,
            projectId = initialTask.projectId,
            stateId = initialTask.stateId,
            assignedTo = initialTask.assignedTo.toString()
        )
        // Then
        coVerify(exactly = 1) { mockTaskMapper.fromType(any()) }
    }

    @Test
    fun `should call data source to add the mapped task`() = runTest {
        // Given
        val task = DummyTasks.validTask
        val mappedTask = DummyTasks.validTaskDto
        coEvery { mockTaskMapper.fromType(any()) } returns mappedTask
        coEvery { auditRepository.addAudit(any()) } returns true

        // When
        taskRepository.addTask(
            title = task.title,
            description = task.description,
            projectId = task.projectId,
            stateId = task.stateId,
            assignedTo = task.assignedTo.toString()
        )

        // Then
        coVerify(exactly = 1) { mockTaskDataSource.addTask(mappedTask) }
    }

    @Test
    fun `should return true when task is added to data source`() = runTest {
        // Given
        val task = DummyTasks.validTask
        coEvery { mockTaskDataSource.addTask(any()) } returns true
        coEvery { auditRepository.addAudit(any()) } returns true


        // When
        val result = taskRepository.addTask(
            title = task.title,
            description = task.description,
            projectId = task.projectId,
            stateId = task.stateId,
            assignedTo = task.assignedTo.toString()
        )

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return all mapped tasks from data source`() = runTest {
        // Given
        val taskDto = DummyTasks.validTaskDto
        val mappedTask = DummyTasks.validTask

        coEvery { mockTaskDataSource.getTasks() } returns listOf(taskDto)
        coEvery { mockTaskMapper.toType(taskDto) } returns mappedTask

        // When
        val result = taskRepository.getAllTasks()

        // Then
        coVerify(exactly = 1) { mockTaskDataSource.getTasks() }
        coVerify(exactly = 1) { mockTaskMapper.toType(taskDto) }

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(mappedTask)
    }

    @Test
    fun `should return true when deleting task`() = runTest {
        // Given
        val task = DummyTasks.validTask
        coEvery { mockTaskDataSource.deleteTask(any()) } returns true
        coEvery { auditRepository.addAudit(any()) } returns true


        // When
        val result = taskRepository.deleteTask(task.id)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when deleting task failed`() = runTest {
        // Given
        val task = DummyTasks.validTask
        coEvery { mockTaskDataSource.deleteTask(any()) } returns false

        // When
        val result = taskRepository.deleteTask(task.id)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `should return true when updating task successfully`() = runTest {
        // Given
        val task = DummyTasks.validTask
        val taskDto = DummyTasks.validTaskDto
        coEvery { mockTaskMapper.fromType(task) } returns taskDto
        coEvery { mockTaskDataSource.updateTask(taskDto) } returns true
        coEvery { auditRepository.addAudit(any()) } returns true


        // When
        val result = taskRepository.updateTask(
            id = task.id, title = task.title,
            description = task.description,
            projectId = task.projectId,
            stateId = task.stateId,
            assignedTo = task.assignedTo.toString(),
            craetedAt= task.createdAt
        )

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when updating task unsuccessfully`() = runTest {
        // Given
        val task = DummyTasks.validTask
        val taskDto = DummyTasks.validTaskDto
        coEvery { mockTaskMapper.fromType(task) } returns taskDto
        coEvery { mockTaskDataSource.updateTask(taskDto) } returns false

        // When
        val result = taskRepository.updateTask(   id = task.id, title = task.title,
            description = task.description,
            projectId = task.projectId,
            stateId = task.stateId,
            assignedTo = task.assignedTo.toString(),
            craetedAt= task.createdAt)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `should return non-null task object when getting by id successfully`() = runTest {
        // Given
        val task = DummyTasks.validTask
        val taskDto = DummyTasks.validTaskDto
        coEvery { mockTaskDataSource.getTaskById(task.id.toString()) } returns taskDto
        coEvery { mockTaskMapper.toType(taskDto) } returns task

        // When
        val result = taskRepository.getTaskById(task.id)

        // Then
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(task)
    }

    @Test
    fun `should return null when getting task by id unsuccessfully`() = runTest {
        // Given
        val task = DummyTasks.validTask
        coEvery { mockTaskDataSource.getTaskById(task.id.toString()) } returns null

        // When
        val result = taskRepository.getTaskById(task.id)

        // Then
        assertThat(result).isNull()
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