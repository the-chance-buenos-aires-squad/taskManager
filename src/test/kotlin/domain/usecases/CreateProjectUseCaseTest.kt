package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.repositories.ProjectRepository
import dummyData.createDummyProject
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateProjectUseCaseTest{
  private lateinit var projectRepository: ProjectRepository
  private lateinit var createProjectUseCase:CreateProjectUseCase
  @BeforeEach
  fun setup(){
   projectRepository= mockk()
   createProjectUseCase=CreateProjectUseCase(projectRepository)
  }
    @Test
    fun `should return true if project created`(){
        val result=createProjectUseCase.execute(createDummyProject())
        assertThat(result).isTrue()
    }
 }