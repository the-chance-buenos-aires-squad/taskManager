package data.repositories

import com.google.common.truth.Truth.assertThat
import domain.entities.Task
import domain.repositories.TaskRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class TaskRepositoryImplTest {
    
    private lateinit var taskRepository: TaskRepository
    
    @BeforeEach
    fun setUp() {
        taskRepository = TaskRepositoryImpl()
    }
    
    @Test
    fun `should generate new id when creating a task`() {
        // Given
        val initialId = UUID.randomUUID()
        val initialTask = createSampleTask(id = initialId)
        
        // When
        val createdTask = taskRepository.createTask(initialTask)
        
        // Then
        assertThat(createdTask.id).isNotEqualTo(initialId)
    }
    
    @Test
    fun `should set current time as createdAt when creating a task`() {
        // Given
        val pastDateTime = LocalDateTime.now().minusDays(5)
        val initialTask = createSampleTask(createdAt = pastDateTime)
        
        // When
        val createdTask = taskRepository.createTask(initialTask)
        
        // Then
        assertThat(createdTask.createdAt).isGreaterThan(pastDateTime)
    }
    
    @Test
    fun `should set current time as updatedAt when creating a task`() {
        // Given
        val pastDateTime = LocalDateTime.now().minusDays(5)
        val initialTask = createSampleTask(updatedAt = pastDateTime)
        
        // When
        val createdTask = taskRepository.createTask(initialTask)
        
        // Then
        assertThat(createdTask.updatedAt).isGreaterThan(pastDateTime)
    }
    
    @Test
    fun `should preserve title when creating a task`() {
        // Given
        val initialTask = createSampleTask(title = "Specific Title")
        
        // When
        val createdTask = taskRepository.createTask(initialTask)
        
        // Then
        assertThat(createdTask.title).isEqualTo(initialTask.title)
    }
    
    @Test
    fun `should preserve description when creating a task`() {
        // Given
        val initialTask = createSampleTask(description = "Specific Description")
        
        // When
        val createdTask = taskRepository.createTask(initialTask)
        
        // Then
        assertThat(createdTask.description).isEqualTo(initialTask.description)
    }
    
    @Test
    fun `should preserve projectId when creating a task`() {
        // Given
        val initialTask = createSampleTask(projectId = "specific-project-id")
        
        // When
        val createdTask = taskRepository.createTask(initialTask)
        
        // Then
        assertThat(createdTask.projectId).isEqualTo(initialTask.projectId)
    }
    
    @Test
    fun `should preserve stateId when creating a task`() {
        // Given
        val initialTask = createSampleTask(stateId = "specific-state-id")
        
        // When
        val createdTask = taskRepository.createTask(initialTask)
        
        // Then
        assertThat(createdTask.stateId).isEqualTo(initialTask.stateId)
    }
    
    @Test
    fun `should preserve assignedTo when creating a task`() {
        // Given
        val initialTask = createSampleTask(assignedTo = "specific-user-id")
        
        // When
        val createdTask = taskRepository.createTask(initialTask)
        
        // Then
        assertThat(createdTask.assignedTo).isEqualTo(initialTask.assignedTo)
    }
    
    @Test
    fun `should preserve createdBy when creating a task`() {
        // Given
        val initialTask = createSampleTask(createdBy = "specific-creator-id")
        
        // When
        val createdTask = taskRepository.createTask(initialTask)
        
        // Then
        assertThat(createdTask.createdBy).isEqualTo(initialTask.createdBy)
    }
    
    private fun createSampleTask(
        id: UUID = UUID.randomUUID(),
        title: String = "Test Task",
        description: String = "Test Description",
        projectId: String = "project-123",
        stateId: String = "state-456",
        assignedTo: String? = "user-789",
        createdBy: String = "admin-012",
        createdAt: LocalDateTime = LocalDateTime.now().minusDays(1),
        updatedAt: LocalDateTime = LocalDateTime.now().minusDays(1)
    ): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            projectId = projectId,
            stateId = stateId,
            assignedTo = assignedTo,
            createdBy = createdBy,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}