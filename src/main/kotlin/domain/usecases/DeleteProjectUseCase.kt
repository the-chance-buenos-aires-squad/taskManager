package domain.usecases

import domain.repositories.ProjectRepository

class DeleteProjectUseCase(
   private val projectRepository: ProjectRepository
) {

}