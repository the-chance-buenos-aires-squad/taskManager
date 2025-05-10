package domain.usecases.project

import domain.customeExceptions.NoProjectsFoundException
import domain.entities.Project
import domain.repositories.ProjectRepository

class GetAllProjectsUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun execute(): List<Project> {
        val projects = projectRepository.getAllProjects()

        if (projects.isEmpty()) throw NoProjectsFoundException()

        return projects
    }
}