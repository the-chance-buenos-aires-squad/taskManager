package presentation.cli.project

import com.google.common.truth.Truth.assertThat
import domain.usecases.project.GetAllProjectsUseCase
import dummyData.createDummyProject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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

//    @Test
//    fun `should throw exception if no projects`() = runTest {
//        val exception = assertThrows<NoProjectsFoundException> {
//            getAllProjectsCli.getAll()
//        }
//        assertThat(exception.message).isEqualTo("Not projects found")
//    }

    @Test
    fun `should call execute function in create use case when I call create function and success to create project`() =
        runTest {
            coEvery { getAllProjectsUseCase.execute() } returns listOf(createDummyProject())

            getAllProjectsCli.getAll()

            coVerify { getAllProjectsUseCase.execute() }
        }
}