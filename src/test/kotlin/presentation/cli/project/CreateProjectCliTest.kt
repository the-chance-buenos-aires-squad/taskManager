package presentation.cli.project

import com.google.common.truth.Truth.assertThat
import data.repositories.ProjectRepositoryImpl
import data.repositories.dataSource.ProjectDataSource
import domain.customeExceptions.UserEnterInvalidValueException
import domain.repositories.AuthRepository
import domain.repositories.ProjectRepository
import domain.usecases.project.CreateProjectUseCase
import dummyData.DummyProjects
import dummyData.DummyUser
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.UiController
import presentation.cli.project.CreateProjectCli.Companion.DESCRIPTION_EMPTY_WARNING_MESSAGE
import presentation.cli.project.CreateProjectCli.Companion.FIRST_DESCRIPTION_PROMPT_MESSAGE
import presentation.cli.project.CreateProjectCli.Companion.FIRST_TITLE_PROMPT_MESSAGE
import presentation.cli.project.CreateProjectCli.Companion.PROJECT_CREATED_MESSAGE
import presentation.cli.project.CreateProjectCli.Companion.TITLE_EMPTY_WARNING_MESSAGE
import java.time.LocalDateTime
import java.util.*

class CreateProjectCliTest {
    private val createProjectUseCase: CreateProjectUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private lateinit var createProjectCli: CreateProjectCli

    val project = DummyProjects.validProject


    @BeforeEach
    fun setup() {
        createProjectCli = CreateProjectCli(createProjectUseCase, uiController)
    }

    @Test
    fun `should print two prompt messages and read inputs`() = runTest{
        //given
        coEvery { createProjectUseCase.execute(project) } returns true
        coEvery { uiController.readInput() } returnsMany listOf("title","description")

        //when
        createProjectCli.create()

        //then
        verifySequence {
            uiController.printMessage(FIRST_TITLE_PROMPT_MESSAGE)
            uiController.readInput()
            uiController.printMessage(FIRST_DESCRIPTION_PROMPT_MESSAGE)
            uiController.readInput()
        }
    }

    @Test
    fun `should print warning message on empty title`() = runTest{
        //given
        coEvery { createProjectUseCase.execute(project) } returns true
        coEvery { uiController.readInput() } returnsMany listOf("","description")

        //when
        createProjectCli.create()

        //then
        verifySequence {
            uiController.printMessage(FIRST_TITLE_PROMPT_MESSAGE)
            uiController.readInput()
            uiController.printMessage(TITLE_EMPTY_WARNING_MESSAGE,true)
            uiController.readInput()
            uiController.printMessage(FIRST_DESCRIPTION_PROMPT_MESSAGE)
            uiController.readInput()
        }
    }
    @Test
    fun `should print warning message on empty description`() = runTest{
        //given
        coEvery { createProjectUseCase.execute(project) } returns true
        coEvery { uiController.readInput() } returnsMany listOf("title","")

        //when
        createProjectCli.create()

        //then
        verifySequence {
            uiController.printMessage(FIRST_TITLE_PROMPT_MESSAGE)
            uiController.readInput()
            uiController.printMessage(FIRST_DESCRIPTION_PROMPT_MESSAGE)
            uiController.readInput()
            uiController.printMessage(DESCRIPTION_EMPTY_WARNING_MESSAGE,true)
            uiController.readInput()
        }
    }

    @Test
    fun `should print read tow input for empty title and description`() = runTest{
        //given
        coEvery { createProjectUseCase.execute(project) } returns true
        coEvery { uiController.readInput() } returnsMany listOf("","2title","","2description")

        //when
        createProjectCli.create()

        //then
        coVerifySequence {
            uiController.printMessage(FIRST_TITLE_PROMPT_MESSAGE)
            uiController.readInput()
            uiController.printMessage(TITLE_EMPTY_WARNING_MESSAGE,true)
            uiController.readInput()
            uiController.printMessage(FIRST_DESCRIPTION_PROMPT_MESSAGE)
            uiController.readInput()
            uiController.printMessage(DESCRIPTION_EMPTY_WARNING_MESSAGE,true)
            uiController.readInput()
        }
    }

    @Test
    fun `should print creation successful message when all input are valid and creation successful`() = runTest{
        //given
        coEvery { createProjectUseCase.execute(any()) } returns true
        coEvery { uiController.readInput() } returnsMany listOf("title","description")

        //when
        val result = createProjectCli.create()

        //then
        coVerify {
            createProjectUseCase.execute(any())
            uiController.printMessage(PROJECT_CREATED_MESSAGE)
        }

    }

    @Test
    fun `should not print creation successful message when all input are valid but creation unSuccessful`() = runTest{
        //given
        coEvery { createProjectUseCase.execute(any()) } returns false
        coEvery { uiController.readInput() } returnsMany listOf("title","description")

        //when
        createProjectCli.create()

        //then
        coVerify {
            createProjectUseCase.execute(any())
        }

    }

    @Test
    fun `should handle any exception thrown from repository`() = runTest {
        // given
        val exceptionMessage = "Something went wrong"
        coEvery { createProjectUseCase.execute(any()) } throws Exception(exceptionMessage)
        coEvery { uiController.readInput() } returnsMany listOf("title", "description")

        // when
        createProjectCli.create()

        // then
        coVerify {
            uiController.printMessage("Failed to create project.$exceptionMessage")
        }
    }

}