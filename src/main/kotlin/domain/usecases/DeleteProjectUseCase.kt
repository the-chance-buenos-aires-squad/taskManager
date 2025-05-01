package domain.usecases

import domain.repositories.ProjectRepository

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun execute(projectId: String): Boolean {
        return projectRepository.deleteProject(projectId)
    }

}