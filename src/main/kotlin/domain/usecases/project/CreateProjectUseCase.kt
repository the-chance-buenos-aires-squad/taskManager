package domain.usecases.project

import domain.entities.Project
import domain.repositories.ProjectRepository


class CreateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun execute(project: Project): Boolean {
        return projectRepository.createProject(project)
    }
}