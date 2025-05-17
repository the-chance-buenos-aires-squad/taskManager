package data.repositories

import auth.FakeUserSession
import auth.UserSession
import com.google.common.truth.Truth.assertThat
import data.dto.ProjectDto
import data.dto.UserDto
import data.exceptions.NoProjectsFoundException
import data.repositories.dataSource.ProjectDataSource
import data.repositories.mappers.ProjectDtoMapper
import domain.customeExceptions.UserEnterInvalidValueException
import domain.entities.User
import domain.entities.UserRole
import domain.repositories.AuditRepository
import domain.repositories.AuthRepository
import dummyData.DummyProjects
import dummyData.DummyUser
import dummyData.DummyUser.dummyUserOne
import dummyData.createDummyProject
import io.mockk.*
import io.mockk.impl.MultiNotifier
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
    private val auditRepository: AuditRepository = mockk(relaxed = true)
    private val session: UserSession = mockk()


    private lateinit var projectRepositoryImpl: ProjectRepositoryImpl


    val id: UUID = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        projectRepositoryImpl = ProjectRepositoryImpl(
            projectDataSource = projectDataSource,
            projectMapper = projectDtoMapper,
            userSession = session,
            auditRepository = auditRepository
        )
    }

    @Test
    fun `should not allow any repository action if user not logged in`() = runTest {
        //given
        every { session.setCurrentUser(null) }

        assertThrows<MockKException> { projectRepositoryImpl.createProject(createDummyProject()) }
        coVerify(exactly = 0) { projectDataSource.addProject(any()) }
        coVerify(exactly = 0) { projectDataSource.updateProject(any()) }
        coVerify(exactly = 0) { projectDataSource.deleteProject(any()) }
        coVerify(exactly = 0) { projectDataSource.getAllProjects() }
        coVerify(exactly = 0) { auditRepository.addAudit(any()) }
    }


    @Test
    fun `should return true if project created successfully`() = runTest {
        // Given
        initUser()
        coEvery { projectDataSource.addProject(any()) } returns true
        coEvery { auditRepository.addAudit(any()) } returns true

        // When
        val result = projectRepositoryImpl.createProject(createDummyProject())

        // Then
        assertThat(result).isTrue()
        coVerify { projectDataSource.addProject(any()) }
        coVerify { auditRepository.addAudit(any()) }
    }

    @Test
    fun `should return false if project not created successfully`() = runTest {
        initUser()
        coEvery { projectDataSource.addProject(any()) } returns false

        val result = projectRepositoryImpl.createProject(createDummyProject())

        assertThat(result).isFalse()
        coVerify { projectDataSource.addProject(any()) }
        coVerify(exactly = 0) { auditRepository.addAudit(any()) }
    }

    @Test
    fun `should return true if project updated successfully`() = runTest {
        initUser()
        coEvery { projectDataSource.updateProject(any()) } returns true

        val result = projectRepositoryImpl.updateProject(createDummyProject())

        assertThat(result).isTrue()
        coVerify { projectDataSource.updateProject(any()) }
        coVerify { auditRepository.addAudit(any()) }
    }

    @Test
    fun `should return false if project not updated successfully`() = runTest {
        initUser()
        coEvery { projectDataSource.updateProject(any()) } returns false

        val result = projectRepositoryImpl.updateProject(createDummyProject())

        assertThat(result).isFalse()
        coVerify { projectDataSource.updateProject(any()) }
        coVerify(exactly = 0) { auditRepository.addAudit(any()) }
    }

    @Test
    fun `should return true if project deleted successfully`() = runTest {
        initUser()
        coEvery { projectDataSource.deleteProject(any()) } returns true

        val result = projectRepositoryImpl.deleteProject(createDummyProject().id)

        assertThat(result).isTrue()
        coVerify { projectDataSource.deleteProject(any()) }
        coVerify { auditRepository.addAudit(any()) }
    }

    @Test
    fun `should return false if project not deleted successfully`() = runTest {
        initUser()
        coEvery { projectDataSource.deleteProject(any()) } returns false

        val result = projectRepositoryImpl.deleteProject(createDummyProject().id)

        assertThat(result).isFalse()
        coVerify { projectDataSource.deleteProject(any()) }
        coVerify(exactly = 0) { auditRepository.addAudit(any()) }
    }


    @Test
    fun `should return list of projects if there are projects`() = runTest {
        initUser()
        val testDto = ProjectDto(
            id.toString(),
            "ahmed",
            "ahmed mate",
            LocalDateTime.now().toString()
        )
        coEvery { projectDataSource.getAllProjects() } returns listOf(testDto, testDto)

        val result = projectRepositoryImpl.getAllProjects()

        assertThat(result).hasSize(2)
        verify { projectDtoMapper.toType(testDto) }
        coVerify { projectDataSource.getAllProjects() }
    }

    @Test
    fun `should throw NoProjectsFoundException aren't projects`() = runTest {
        //given
        initUser()
        coEvery { projectDataSource.getAllProjects() } returns emptyList()

        //then & when
        assertThrows<NoProjectsFoundException> { projectRepositoryImpl.getAllProjects() }


//        coVerify { projectDataSource.getAllProjects() }
    }

    @Test
    fun `should return false if project don't created`() = runTest {

        coEvery { projectDataSource.getAllProjects() } returns emptyList()
        assertThrows<MockKException> {
            projectRepositoryImpl.getAllProjects()
        }
    }

    private fun initUser() {
        every { session.setCurrentUser(adminUser) }
        coEvery { session.runIfLoggedIn(any<suspend (User) -> Boolean>()) } coAnswers {
            val action = firstArg<suspend (User) -> Boolean>()
            action(adminUser)
        }
    }

    private val adminUser = User(
        id = UUID.randomUUID(),
        username = "admin",
        role = UserRole.ADMIN,
        createdAt = LocalDateTime.now()
    )
}