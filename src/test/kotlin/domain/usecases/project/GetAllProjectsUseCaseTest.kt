package domain.usecases.project

import com.google.common.truth.Truth.assertThat
import data.exceptions.NoProjectsFoundException
import domain.repositories.ProjectRepository
import dummyData.createDummyProject
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.builtins.NothingSerializer
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
    fun `should return list of all projects success`() = runTest {
        coEvery { projectRepository.getAllProjects() } returns listOf(createDummyProject())

        val result = getAllProjectsUseCase.execute()

        assertThat(result).isNotEmpty()
    }

}