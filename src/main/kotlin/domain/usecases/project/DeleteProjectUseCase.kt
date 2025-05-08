package domain.usecases.project

import domain.repositories.ProjectRepository
import java.util.*

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun execute(projectId: UUID): Boolean {
        return projectRepository.deleteProject(projectId)
    }

}