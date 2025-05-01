package domain.usecases

import domain.entities.Project
import domain.repositories.ProjectRepository


class CreateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun execute(project: Project): Boolean {
        return projectRepository.createProject(project)
    }
}