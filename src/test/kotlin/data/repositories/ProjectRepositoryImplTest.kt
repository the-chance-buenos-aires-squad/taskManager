package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dto.ProjectDto
import data.repositories.dataSource.ProjectDataSource
import data.repositories.mappers.ProjectDtoMapper
import domain.customeExceptions.UserEnterInvalidValueException
import domain.entities.User
import domain.repositories.AuditRepository
import domain.repositories.AuthRepository
import dummyData.DummyProjects
import dummyData.DummyUser
import dummyData.DummyUser.dummyUserOne
import dummyData.createDummyProject
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.exceptions.UserNotLoggedInException
import java.time.LocalDateTime
import java.util.*

class ProjectRepositoryImplTest {
    private var projectDataSource: ProjectDataSource = mockk(relaxed = true)
    private var projectDtoMapper: ProjectDtoMapper = mockk(relaxed = true)
    private lateinit var projectRepositoryImpl: ProjectRepositoryImpl
    private val auditRepository:AuditRepository = mockk(relaxed = true)

    private val fakeAuthRepo = object : AuthRepository {
        var currentUser:User? = dummyUserOne
        override suspend fun login(username: String, password: String) = dummyUserOne
        override suspend fun addUser(userName: String, password: String) = dummyUserOne
        override suspend fun logout() {}
        override suspend fun getCurrentUser() = currentUser
        override suspend fun <T> runIfLoggedIn(action: suspend (User) -> T): T {
            return action(getCurrentUser()?:throw UserNotLoggedInException())
        }
    }

    val id: UUID = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        projectRepositoryImpl = ProjectRepositoryImpl(projectDataSource, projectDtoMapper, fakeAuthRepo,auditRepository)
    }



    @Test
    fun `should not allow any repository action if user not logged in`() = runTest {
        //given
        fakeAuthRepo.currentUser = null

        assertThrows<UserNotLoggedInException> { projectRepositoryImpl.createProject(createDummyProject()) }
        coVerify(exactly = 0) { projectDataSource.addProject(any()) }
        coVerify(exactly = 0) { projectDataSource.updateProject(any()) }
        coVerify(exactly = 0) { projectDataSource.deleteProject(any()) }
        coVerify(exactly = 0) { projectDataSource.getAllProjects() }
        coVerify (exactly = 0){ auditRepository.addAudit(any()) }
    }


    @Test
    fun `should return true if project created successfully`() = runTest {
        //given
        coEvery { projectDataSource.addProject(any()) } returns true

        val result = projectRepositoryImpl.createProject(createDummyProject())

        assertThat(result).isTrue()
        coVerify { projectDataSource.addProject(any()) }
        coVerify { auditRepository.addAudit(any()) }
    }

    @Test
    fun `should throw UserEnterInvalidValueException trying to create project with empty title`() = runTest {
        //given

        //when & then
        assertThrows< UserEnterInvalidValueException > { projectRepositoryImpl.createProject(createDummyProject(name = "")) }
    }

    @Test
    fun `should throw UserEnterInvalidValueException trying to create project with empty description`() = runTest {
        //given

        //when & then
        assertThrows< UserEnterInvalidValueException > { projectRepositoryImpl.createProject(createDummyProject(description = "")) }
    }

    @Test
    fun `should return false if project not created successfully`() = runTest {
        coEvery { projectDataSource.addProject(any()) } returns false

        val result = projectRepositoryImpl.createProject(createDummyProject())

        assertThat(result).isFalse()
        coVerify { projectDataSource.addProject(any()) }
        coVerify (exactly = 0){ auditRepository.addAudit(any()) }
    }

    @Test
    fun `should return true if project updated successfully`() = runTest {
        coEvery { projectDataSource.updateProject(any()) } returns true

        val result = projectRepositoryImpl.updateProject(createDummyProject())

        assertThat(result).isTrue()
        coVerify { projectDataSource.updateProject(any()) }
        coVerify { auditRepository.addAudit(any()) }
    }

    @Test
    fun `should return false if project not updated successfully`() = runTest {
        coEvery { projectDataSource.updateProject(any()) } returns false

        val result = projectRepositoryImpl.updateProject(createDummyProject())

        assertThat(result).isFalse()
        coVerify { projectDataSource.updateProject(any()) }
        coVerify (exactly = 0){ auditRepository.addAudit(any()) }
    }

    @Test
    fun `should return true if project deleted successfully`() = runTest {
        coEvery { projectDataSource.deleteProject(any()) } returns true

        val result = projectRepositoryImpl.deleteProject(createDummyProject().id)

        assertThat(result).isTrue()
        coVerify { projectDataSource.deleteProject(any()) }
        coVerify { auditRepository.addAudit(any()) }
    }

    @Test
    fun `should return false if project not deleted successfully`() = runTest {
        coEvery { projectDataSource.deleteProject(any()) } returns false

        val result = projectRepositoryImpl.deleteProject(createDummyProject().id)

        assertThat(result).isFalse()
        coVerify { projectDataSource.deleteProject(any()) }
        coVerify (exactly = 0){ auditRepository.addAudit(any()) }
    }


    @Test
    fun `should return list of projects if there are projects`() = runTest {
        val testDto = ProjectDto(
            id.toString(),
            "ahmed",
            "ahmed mate",
            LocalDateTime.now().toString()
        )
        coEvery { projectDataSource.getAllProjects() } returns listOf(testDto, testDto)

        val result = projectRepositoryImpl.getAllProjects()

        assertThat(result).hasSize(2)
        verify { projectDtoMapper.toEntity(testDto) }
        coVerify { projectDataSource.getAllProjects() }
    }

    @Test
    fun `should return empty list if there aren't projects`() = runTest {
        coEvery { projectDataSource.getAllProjects() } returns emptyList()

        val result = projectRepositoryImpl.getAllProjects()

        assertThat(result).isEmpty()
        coVerify { projectDataSource.getAllProjects() }
    }
}