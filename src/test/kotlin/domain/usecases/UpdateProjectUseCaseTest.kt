package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.repositories.ProjectRepository
import dummyData.createDummyProject
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var updateProjectUseCase: UpdateProjectUseCase

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        updateProjectUseCase = UpdateProjectUseCase(projectRepository)
    }

    @Test
    fun `should return true if project updated success`() {
        every { projectRepository.updateProject(any()) } returns true

        val result = updateProjectUseCase.execute(createDummyProject())

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false if project don't update project`() {
        every { projectRepository.updateProject(any()) } returns false

        val result = updateProjectUseCase.execute(createDummyProject())

        assertThat(result).isFalse()
    }
}