package domain.usecases

import domain.repositories.ProjectRepository
import org.buinos.domain.entities.Project

class EditProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun execute(project: Project): Boolean {
        return false
    }

}