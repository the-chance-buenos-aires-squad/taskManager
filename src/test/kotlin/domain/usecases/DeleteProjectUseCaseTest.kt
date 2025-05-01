package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.repositories.ProjectRepository
import dummyData.createDummyProject
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var deleteProjectUseCase: DeleteProjectUseCase

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        deleteProjectUseCase = DeleteProjectUseCase(projectRepository)
    }

    @Test
    fun `should return true if project deleted success`() {
        every { projectRepository.deleteProject(any()) } returns true

        val result = deleteProjectUseCase.execute(createDummyProject().id)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false if project don't deleted`() {
        every { projectRepository.deleteProject(any()) } returns false

        val result = deleteProjectUseCase.execute(createDummyProject().id)

        assertThat(result).isFalse()
    }
}