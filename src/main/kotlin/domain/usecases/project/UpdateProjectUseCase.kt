package domain.usecases.project

import domain.entities.Project
import domain.repositories.ProjectRepository


class UpdateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun execute(project: Project): Boolean {
        return projectRepository.updateProject(project)
    }

}