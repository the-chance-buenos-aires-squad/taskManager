package domain.usecases

import com.google.common.truth.Truth.assertThat
import domain.entities.ActionType
import domain.entities.EntityType
import domain.repositories.AuthRepository
import domain.validation.UserValidator
import dummyData.DummyUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class CreateUserUseCaseTest {


    private lateinit var useCase: CreateUserUseCase
    private val authRepo: AuthRepository = mockk()
    private val addAuditUseCase: AddAuditUseCase = mockk()

    private val dummyUser = DummyUser.dummyUserTwo
    private val currentUser = DummyUser.dummyUserOne

    @BeforeEach
    fun setup() {
        useCase = CreateUserUseCase(authRepo, addAuditUseCase)
        coEvery { authRepo.getCurrentUser() } returns currentUser
        coEvery { authRepo.addUser(any(), any()) } returns dummyUser
        coEvery { addAuditUseCase.addAudit(any(),any(),
            any(),any(),any(),any(),any()) } returns Unit
    }


    @Test
    fun `should save user when data is valid`() = runTest {
        // When
        useCase.addUser("mateUserName", "password")

        // Then
        coVerify(exactly = 1) {
            authRepo.addUser(dummyUser.username, "password")
        }
    }

    @Test
    fun `addUser should returns user when added successfully`() = runTest {
        // When
        val result = useCase.addUser(dummyUser.username, "password")

        // Then
        assertThat(result).isEqualTo(dummyUser)
    }


    @Test
    fun `should create audit when user is created successfully`() = runTest {
        // When
        useCase.addUser(dummyUser.username, "password")

        // Then
        coVerify(exactly = 1) {
            addAuditUseCase.addAudit(
                entityId = dummyUser.id.toString(),
                entityType = EntityType.USER,
                action = ActionType.CREATE,
                field = "create new user",
                oldValue = null,
                newValue = "creating user:${dummyUser.username}",
                userId = currentUser.id.toString()
            )
        }
    }
}