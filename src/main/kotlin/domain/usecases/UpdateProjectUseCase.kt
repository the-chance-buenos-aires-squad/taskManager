package domain.usecases

import domain.entities.Project
import domain.repositories.ProjectRepository


class UpdateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun execute(project: Project): Boolean {
        return projectRepository.updateProject(project)
    }

}