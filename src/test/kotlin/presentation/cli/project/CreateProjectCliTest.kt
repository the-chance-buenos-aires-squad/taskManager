package presentation.cli.project

import data.repositories.ProjectRepositoryImpl
import data.repositories.dataSource.ProjectDataSource
import domain.customeExceptions.UserEnterInvalidValueException
import domain.repositories.AuthRepository
import domain.repositories.ProjectRepository
import domain.usecases.project.CreateProjectUseCase
import dummyData.DummyUser
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController
import java.time.LocalDateTime
import java.util.*

class CreateProjectCliTest {
    private val createProjectUseCase: CreateProjectUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private lateinit var createProjectCli: CreateProjectCli

    val project = listOf(UUID.randomUUID().toString(), "ahmed", "ahmed mohamed egypt", LocalDateTime.now().toString())
    private val project2 =
        listOf("", "ahmed mohamed egypt", LocalDateTime.now().toString())
    private val project3 =
        listOf("ahmed", "", LocalDateTime.now().toString())


    @BeforeEach
    fun setup() {
        createProjectCli = CreateProjectCli(createProjectUseCase, uiController)
    }

    @Test
    fun `should call execute function in create use case when I call create function and success to create project`() =
        runTest {
            coEvery { createProjectUseCase.execute(any()) } returns true
            every { uiController.readInput() } returnsMany project

            createProjectCli.create()

            coVerify { createProjectUseCase.execute(any()) }
        }

    @Test
    fun `should call execute function in create use case when I call create function and failed to create project`() =
        runTest {
            coEvery { createProjectUseCase.execute(any()) } returns false
            every { uiController.readInput() } returnsMany project

            createProjectCli.create()

            coVerify { createProjectUseCase.execute(any()) }
        }

    @Test
    fun `should catch UserEnterInvalidValueException from repo and print it message`() = runTest {
        //given
        val mockedRepo: ProjectRepository = mockk()
        val mockedUseCase = CreateProjectUseCase(mockedRepo)
        val mockedCreateProjectCli = CreateProjectCli(mockedUseCase, uiController)
        val exceptionMessage = "title or description can't be empty"
        coEvery { mockedRepo.createProject(any()) } throws UserEnterInvalidValueException(exceptionMessage)
        every { uiController.readInput() } returns ""


        //when
        mockedCreateProjectCli.create()

        verify {
            uiController.printMessage(exceptionMessage)
        }

    }

    @Test
    fun `should catch any data source specific exception and print it message`() = runTest {
        //given
        val exceptionMessage = "fail from data source dou to ......."
        val mockedDataSource: ProjectDataSource = mockk(relaxed = true)
        val mockedAuthRepo:AuthRepository = mockk(relaxed = true)
        coEvery { mockedAuthRepo.getCurrentUser() } returns DummyUser.dummyUserOne
        coEvery { mockedDataSource.addProject(any()) } throws Exception(exceptionMessage)
        every { uiController.readInput() } returnsMany listOf("title", "description")
        val mockedRepo: ProjectRepository = ProjectRepositoryImpl(mockedDataSource, mockk(relaxed = true), mockedAuthRepo)
        val mockedUseCase = CreateProjectUseCase(mockedRepo)
        val createProjectCli = CreateProjectCli(mockedUseCase, uiController)


        //when
        createProjectCli.create()

        verify {
            uiController.printMessage("Failed to create project.${exceptionMessage}")
        }

    }

}