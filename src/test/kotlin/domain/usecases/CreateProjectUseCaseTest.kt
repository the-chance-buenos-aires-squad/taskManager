package domain.usecases

import domain.repositories.ProjectRepository
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach

class CreateProjectUseCaseTest{
  private lateinit var projectRepository: ProjectRepository
  private lateinit var createProjectUseCase:CreateProjectUseCase
  @BeforeEach
  fun setup(){
   projectRepository= mockk()
   createProjectUseCase=CreateProjectUseCase(projectRepository)
  }
 }