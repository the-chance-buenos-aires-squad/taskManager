package data.repositories

import data.repositories.dataSource.ProjectDataSource
import data.repositories.mappers.ProjectDtoMapper
import domain.customeExceptions.UserEnterInvalidValueException
import domain.entities.ActionType
import domain.entities.Audit
import domain.entities.EntityType
import domain.entities.Project
import domain.repositories.AuditRepository
import domain.repositories.AuthRepository
import domain.repositories.ProjectRepository
import java.time.LocalDateTime
import java.util.*

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource,
    private val projectMapper: ProjectDtoMapper,
    private val authRepository: AuthRepository,
    private val auditRepository: AuditRepository
) : ProjectRepository {

    override suspend fun createProject(project: Project): Boolean {
        return authRepository.runIfLoggedIn {currentUser->
            //Todo remove this to cli
            when {
                project.title.isEmpty() -> throw UserEnterInvalidValueException("name can't be empty")
                project.description.isEmpty() -> throw UserEnterInvalidValueException("description can't be empty")
            }
             projectDataSource.addProject(projectMapper.fromEntity(project)).also { result->
                 if (result){
                     auditRepository.addAudit(
                         Audit(
                             id = UUID.randomUUID(),
                             entityId = project.id.toString(),
                             entityType = EntityType.PROJECT,
                             action = ActionType.CREATE,
                             field = "",
                             originalValue = "",
                             modifiedValue = "new project",
                             userId = currentUser.id.toString(),
                             timestamp = LocalDateTime.now()
                         )
                     )
                 }
             }
        }
    }

    override suspend fun updateProject(project: Project): Boolean {
        return authRepository.runIfLoggedIn {currentUser->
            projectDataSource.updateProject(projectMapper.fromEntity(project)).also { result->
                if (result){
                    auditRepository.addAudit(
                        Audit(
                            id = UUID.randomUUID(),
                            entityId = project.id.toString(),
                            entityType = EntityType.PROJECT,
                            action = ActionType.UPDATE,
                            field = "",
                            originalValue = "",
                            modifiedValue = "",
                            userId = currentUser.id.toString(),
                            timestamp = LocalDateTime.now()
                        )
                    )
                }
            }
        }
    }

    override suspend fun deleteProject(projectId: UUID): Boolean {
        return authRepository.runIfLoggedIn {currentUser->
            projectDataSource.deleteProject(projectId.toString()).also { result->
                if (result){
                    auditRepository.addAudit(
                        Audit(
                            id = UUID.randomUUID(),
                            entityId = projectId.toString(),
                            entityType = EntityType.PROJECT,
                            action = ActionType.DELETE,
                            field = "",
                            originalValue = "",
                            modifiedValue = "",
                            userId = currentUser.id.toString(),
                            timestamp = LocalDateTime.now()
                        )
                    )
                }
            }
        }
    }

    override suspend fun getAllProjects(): List<Project> {
        return authRepository.runIfLoggedIn {
            projectDataSource.getAllProjects()
            .map { projectRow ->
                projectMapper.toEntity(projectRow)
            }
        }
    }
}