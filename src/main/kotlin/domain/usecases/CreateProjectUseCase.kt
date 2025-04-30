package domain.usecases

import domain.repositories.ProjectRepository
import org.buinos.domain.entities.Project

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun execute(project: Project):Boolean{
        return false
    }
}