package domain.usecases.project

import domain.repositories.ProjectRepository
import java.util.UUID

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun execute(projectId: UUID): Boolean {
        return projectRepository.deleteProject(projectId)
    }

}