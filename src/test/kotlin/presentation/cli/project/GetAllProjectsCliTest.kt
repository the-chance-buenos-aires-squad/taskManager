package presentation.cli.project

import com.google.common.truth.Truth.assertThat
import domain.customeExceptions.NoProjectsFoundException
import domain.usecases.project.GetAllProjectsUseCase
import dummyData.createDummyProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.UiController

class GetAllProjectsCliTest {
    private val getAllProjectsUseCase: GetAllProjectsUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private lateinit var getAllProjectsCli: GetAllProjectsCli

    @BeforeEach
    fun setup() {
        getAllProjectsCli = GetAllProjectsCli(getAllProjectsUseCase, uiController)
    }

    @Test
    fun `should throw exception if no projects`() {
        val exception = assertThrows<NoProjectsFoundException> {
            getAllProjectsCli.getAll()
        }
        assertThat(exception.message).isEqualTo("Not projects found")
    }

    @Test
    fun `should call execute function in create use case when I call create function and success to create project`() {
        every { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())

        getAllProjectsCli.getAll()

        verify { getAllProjectsUseCase.execute() }
    }
}