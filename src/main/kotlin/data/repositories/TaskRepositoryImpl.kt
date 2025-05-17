package data.repositories

import auth.UserSession
import data.dto.TaskDto
import data.repositories.dataSource.TaskDataSource
import data.repositories.mappers.TaskDtoMapper
import domain.entities.*
import domain.repositories.AuditRepository
import domain.repositories.TaskRepository
import java.time.LocalDateTime
import java.util.*

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
    private val taskMapper: TaskDtoMapper,
    private val userSession: UserSession,
    private val auditRepository: AuditRepository
) : TaskRepository {

    override suspend fun addTask(task: Task): Boolean {
        return userSession.runIfLoggedIn { currentUser ->
            val taskDto:TaskDto = taskMapper.fromType(task).also { it.createdBy = currentUser.id.toString() }
            taskDataSource.addTask(taskDto).also { result ->
                if (result) {
                    recordTaskAudit(task.id, currentUser,action = ActionType.CREATE)
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

    override suspend fun updateTask(task: Task): Boolean {
        return userSession.runIfLoggedIn { currentUser ->
            val taskDto:TaskDto = taskMapper.fromType(task).also { it.createdBy = currentUser.id.toString() }
            taskDataSource.updateTask(taskDto).also { result ->
                if (result) {
                    recordTaskAudit(task.id, currentUser,action = ActionType.UPDATE)
                }
            }
        }
    }

    override suspend fun deleteTask(id: UUID): Boolean {
        return userSession.runIfLoggedIn { currentUser ->
            taskDataSource.deleteTask(id.toString()).also { result ->
                if (result) {
                    recordTaskAudit(id, currentUser,action = ActionType.DELETE)
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