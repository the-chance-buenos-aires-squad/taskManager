package domain.usecases.project

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.InvalidCredentialsException
import domain.customeExceptions.NoProjectsFoundException
import domain.repositories.ProjectRepository
import dummyData.createDummyProject
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetAllProjectsUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        getAllProjectsUseCase = GetAllProjectsUseCase(projectRepository)
    }

    @Test
    fun `should return list of all projects success`() {
        every { projectRepository.getAllProjects() } returns listOf(createDummyProject())

        val result = getAllProjectsUseCase.execute()

        assertThat(result).isNotEmpty()
    }

    @Test
    fun `should return false if project don't created`() {
        every { projectRepository.getAllProjects() } returns emptyList()


        assertThrows<NoProjectsFoundException> {
            getAllProjectsUseCase.execute()
        }
    }

}