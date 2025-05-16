package data.repositories

import auth.UserSession
import data.repositories.dataSource.ProjectDataSource
import data.repositories.mappers.ProjectDtoMapper
import domain.customeExceptions.UserEnterInvalidValueException
import domain.entities.*
import domain.repositories.AuditRepository
import domain.repositories.ProjectRepository
import java.time.LocalDateTime
import java.util.*

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val projectMapper: ProjectDtoMapper,
    private val userSession: UserSession,
    private val auditRepository: AuditRepository
) : ProjectRepository {

    override suspend fun createProject(project: Project): Boolean {
        return userSession.runIfLoggedIn {currentUser->
            //Todo move this to cli
            when {
                project.title.isEmpty() -> throw UserEnterInvalidValueException("name can't be empty")
                project.description.isEmpty() -> throw UserEnterInvalidValueException("description can't be empty")
            }
             projectDataSource.addProject(projectMapper.fromEntity(project)).also { result->
                 if (result){
                     recordProjectAudit(project.id, currentUser,action = ActionType.CREATE)
                 }
             }
        }
    }


    override suspend fun updateProject(project: Project): Boolean {
        return userSession.runIfLoggedIn {currentUser->
            projectDataSource.updateProject(projectMapper.fromEntity(project)).also { result->
                if (result){
                    recordProjectAudit(project.id, currentUser,action = ActionType.UPDATE)
                }
            }
        }
    }

    override suspend fun deleteProject(projectId: UUID): Boolean {
        return userSession.runIfLoggedIn {currentUser->
            projectDataSource.deleteProject(projectId.toString()).also { result->
                if (result){
                    recordProjectAudit(projectId, currentUser,action = ActionType.DELETE)
                }
            }
        }
    }

    override suspend fun getAllProjects(): List<Project> {
        return userSession.runIfLoggedIn {
            projectDataSource.getAllProjects()
            .map { projectRow ->
                projectMapper.toEntity(projectRow)
            }
        }
    }

    private suspend fun recordProjectAudit(projectId: UUID, currentUser: User, action: ActionType) {
        auditRepository.addAudit(
            Audit(
                id = UUID.randomUUID(),
                entityId = projectId.toString(),
                entityType = EntityType.PROJECT,
                action = action,
                field = "",
                originalValue = "",
                modifiedValue = "new project",
                userId = currentUser.id.toString(),
                timestamp = LocalDateTime.now()
            )
        )
    }

}