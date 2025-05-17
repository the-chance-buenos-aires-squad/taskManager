package data.repositories

import auth.UserSession
import data.exceptions.TaskStateNameException
import data.repositories.dataSource.TaskStateDataSource
import data.repositories.mappers.TaskStateDtoMapper
import domain.entities.*
import domain.repositories.AuditRepository
import domain.repositories.TaskStateRepository
import java.time.LocalDateTime
import java.util.*

class TaskStateRepositoryImpl(
    private val taskStateDataSource: TaskStateDataSource,
    private val taskStateDtoMapper: TaskStateDtoMapper,
    private val userSession: UserSession,
    private val auditRepository: AuditRepository
) : TaskStateRepository {
    override suspend fun createTaskState(state: TaskState): Boolean {
        return userSession.runIfLoggedIn { currentUser ->
            val taskStates =
                getAllTaskStates().filter { it.projectId == state.projectId && it.title.lowercase() == state.title.lowercase() }
            if (taskStates.isNotEmpty()) throw TaskStateNameException()
            taskStateDataSource.createTaskState(taskStateDtoMapper.fromType(state)).also { result ->
                if (result) {
                    recordTaskStateAudit(state.id, currentUser, action = ActionType.CREATE)
                }
            }
        }
    }


    override suspend fun editTaskState(state: TaskState): Boolean {
        return userSession.runIfLoggedIn { currentUser ->
            taskStateDataSource.editTaskState(taskStateDtoMapper.fromType(state)).also { result ->
                if (result) {
                    recordTaskStateAudit(state.id, currentUser, action = ActionType.UPDATE)
                }
            }
        }
    }

    override suspend fun deleteTaskState(stateId: UUID): Boolean {
        return userSession.runIfLoggedIn { currentUser ->
            taskStateDataSource.deleteTaskState(stateId.toString()).also { result ->
                if (result) {
                    recordTaskStateAudit(stateId, currentUser, action = ActionType.CREATE)
                }
            }
        }
    }

    override suspend fun getAllTaskStates(): List<TaskState> {
        val taskStateRows = taskStateDataSource.getTaskStates()
        return taskStateRows.map { taskStateRow -> taskStateDtoMapper.toType(taskStateRow) }
    }

    private suspend fun recordTaskStateAudit(stateId: UUID, currentUser: User, action: ActionType) {
        auditRepository.addAudit(
            Audit(
                id = UUID.randomUUID(),
                entityId = stateId.toString(),
                entityType = EntityType.STATE,
                action = action,
                field = "",
                originalValue = "",
                modifiedValue = "new task state",
                userId = currentUser.id.toString(),
                timestamp = LocalDateTime.now()
            )
        )
    }


}