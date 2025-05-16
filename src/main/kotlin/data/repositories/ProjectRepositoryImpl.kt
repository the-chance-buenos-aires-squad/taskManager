    package data.repositories

    import auth.UserSession
    import data.exceptions.NoProjectsFoundException
    import data.repositories.dataSource.ProjectDataSource
    import data.repositories.mappers.ProjectDtoMapper
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
            return userSession.runIfLoggedIn { currentUser ->

                projectDataSource.addProject(projectMapper.fromEntity(project)).also { result ->
                    if (result) {
                        recordProjectAudit(project.id, currentUser, action = ActionType.CREATE)
                    }
                }
            }
        }


        override suspend fun updateProject(project: Project): Boolean {
            return userSession.runIfLoggedIn { currentUser ->
                projectDataSource.updateProject(projectMapper.fromEntity(project)).also { result ->
                    if (result) {
                        recordProjectAudit(project.id, currentUser, action = ActionType.UPDATE)
                    }
                }
            }
        }

        override suspend fun deleteProject(projectId: UUID): Boolean {
            return userSession.runIfLoggedIn { currentUser ->
                projectDataSource.deleteProject(projectId.toString()).also { result ->
                    if (result) {
                        recordProjectAudit(projectId, currentUser, action = ActionType.DELETE)
                    }
                }
            }
        }

        override suspend fun getAllProjects(): List<Project> {
            return userSession.runIfLoggedIn {
                val projects = projectDataSource.getAllProjects()
                if (projects.isEmpty()) throw NoProjectsFoundException()
                projects
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