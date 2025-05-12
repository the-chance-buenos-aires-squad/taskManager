package domain.usecases.project

import domain.entities.Project
import domain.repositories.ProjectRepository

class GetAllProjectsUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun execute(): List<Project> {
        return projectRepository.getAllProjects()
    }
}