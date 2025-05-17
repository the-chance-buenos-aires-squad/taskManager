package data.repositories

import auth.UserSession
import data.dto.TaskDto
import data.repositories.dataSource.TaskDataSource
import data.repositories.mappers.TaskDtoMapper
import domain.entities.*
import domain.repositories.AuditRepository
import domain.repositories.TaskRepository
import domain.repositories.UserRepository
import java.time.LocalDateTime
import java.util.*

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
    private val taskMapper: TaskDtoMapper,
    private val userSession: UserSession,
    private val auditRepository: AuditRepository,
    private val userRepository: UserRepository
) : TaskRepository {

    override suspend fun addTask(
        title: String,
        description: String,
        projectId: UUID,
        stateId: UUID,
        assignedTo: String
    ): Boolean {
        return userSession.runIfLoggedIn { currentUser ->
            val userAssignName = userRepository.getUserByUserName(assignedTo)?.id ?: ""

            val newTaskId = UUID.randomUUID()
            val newTask = TaskDto(
                id = newTaskId.toString(),
                title = title,
                description = description,
                projectId = projectId.toString(),
                stateId = stateId.toString(),
                assignedTo = userAssignName,
                createdBy = currentUser.id.toString(),
                createdAt = LocalDateTime.now().toString(),
                updatedAt = LocalDateTime.now().toString(),
            )

            taskDataSource.addTask(newTask).also { result ->
                if (result) {
                    recordTaskAudit(newTaskId, currentUser, action = ActionType.CREATE)
                }
            }
        }
    }

    override suspend fun updateTask(
        id: UUID,
        title: String,
        description: String,
        projectId: UUID,
        stateId: UUID,
        assignedTo: String,
        craetedAt: LocalDateTime
    ): Boolean {
        return userSession.runIfLoggedIn { currentUser ->
            val userAssignName = userRepository.getUserByUserName(assignedTo)?.id ?: ""

            val updatedTask = TaskDto(
                id = id.toString(),
                title = title,
                description = description,
                projectId = projectId.toString(),
                stateId = stateId.toString(),
                assignedTo = userAssignName,
                createdBy = currentUser.id.toString(),
                createdAt = craetedAt.toString(),
                updatedAt = LocalDateTime.now().toString(),
            )
            taskDataSource.updateTask(updatedTask).also { result ->
                if (result) {
                    recordTaskAudit(id, currentUser, action = ActionType.UPDATE)
                }
            }
        }
    }

    override suspend fun getAllTasks(): List<Task> {
        return userSession.runIfLoggedIn {
            taskDataSource.getTasks().map { task ->
                taskMapper.toType(task)
            }
        }

    }

    override suspend fun getTaskById(id: UUID): Task? {
        return userSession.runIfLoggedIn {
            taskDataSource.getTaskById(id.toString())?.let {
                taskMapper.toType(it)
            }
        }
    }


    override suspend fun deleteTask(id: UUID): Boolean {
        return userSession.runIfLoggedIn { currentUser ->
            taskDataSource.deleteTask(id.toString()).also { result ->
                if (result) {
                    recordTaskAudit(id, currentUser, action = ActionType.DELETE)
                }
            }
        }
    }

    private suspend fun recordTaskAudit(taskID: UUID, currentUser: User, action: ActionType) {
        auditRepository.addAudit(
            Audit(
                id = UUID.randomUUID(),
                entityId = taskID.toString(),
                entityType = EntityType.TASK,
                action = action,
                field = "",
                originalValue = "",
                modifiedValue = "new task",
                userId = currentUser.id.toString(),
                timestamp = LocalDateTime.now()
            )
        )
    }


}