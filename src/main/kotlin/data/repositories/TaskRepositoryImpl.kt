package data.repositories

import data.repositories.dataSource.TaskDataSource
import data.repositories.mappers.TaskDtoMapper
import domain.entities.*
import domain.repositories.AuditRepository
import domain.repositories.AuthRepository
import domain.repositories.TaskRepository
import java.time.LocalDateTime
import java.util.*

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
    private val taskMapper: TaskDtoMapper,
    private val authRepository: AuthRepository,
    private val auditRepository: AuditRepository
) : TaskRepository {

    override suspend fun addTask(task: Task): Boolean {
        return authRepository.runIfLoggedIn { currentUser ->
            taskDataSource.addTask(taskMapper.fromEntity(task)).also { result ->
                if (result) {
                    recordTaskAudit(task.id, currentUser,action = ActionType.CREATE)
                }
            }
        }
    }

    override suspend fun getAllTasks(): List<Task> {
        return authRepository.runIfLoggedIn {
            taskDataSource.getTasks().map { task ->
                taskMapper.toEntity(task)
            }
        }

    }

    override suspend fun getTaskById(id: UUID): Task? {
        return authRepository.runIfLoggedIn {
            taskDataSource.getTaskById(id.toString())?.let {
                taskMapper.toEntity(it)
            }
        }
    }

    override suspend fun updateTask(task: Task): Boolean {
        return authRepository.runIfLoggedIn { currentUser ->
            taskDataSource.updateTask(taskMapper.fromEntity(task)).also { result ->
                if (result) {
                    recordTaskAudit(task.id, currentUser,action = ActionType.UPDATE)
                }
            }
        }
    }

    override suspend fun deleteTask(id: UUID): Boolean {
        return authRepository.runIfLoggedIn { currentUser ->
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