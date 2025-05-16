package domain.usecases.user

import com.google.common.truth.Truth.assertThat
import domain.entities.ActionType
import domain.entities.EntityType
import domain.repositories.AuthRepository
import domain.usecases.audit.AddAuditUseCase
import dummyData.DummyUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
//TODO fix
//class CreateUserUseCaseTest {
//
//
//    private lateinit var useCase: CreateUserUseCase
//    private val authRepo: AuthRepository = mockk()
//    private val addAuditUseCase: AddAuditUseCase = mockk()
//
//    private val dummyUser = DummyUser.dummyUserTwo
//    private val currentUser = DummyUser.dummyUserOne
//
//    @BeforeEach
//    fun setup() {
//        useCase = CreateUserUseCase(authRepo, addAuditUseCase)
//        coEvery { authRepo.getCurrentUser() } returns currentUser
//        coEvery { authRepo.addUser(any(), any()) } returns dummyUser
//        coEvery { addAuditUseCase.execute(any(),any(),
//            any(),any(),any(),any(),any()) } returns Unit
//    }
//
//
//    @Test
//    fun `should save user when data is valid`() = runTest {
//        // When
//        useCase.execute("mateUserName", "password")
//
//        // Then
//        coVerify(exactly = 1) {
//            authRepo.addUser(dummyUser.username, "password")
//        }
//    }
//
//    @Test
//    fun `addUser should returns user when added successfully`() = runTest {
//        // When
//        val result = useCase.execute(dummyUser.username, "password")
//
//        // Then
//        assertThat(result).isEqualTo(dummyUser)
//    }
//
//
//    @Test
//    fun `should create audit when user is created successfully`() = runTest {
//        // When
//        useCase.execute(dummyUser.username, "password")
//
//        // Then
//        coVerify(exactly = 1) {
//            addAuditUseCase.execute(
//                entityId = dummyUser.id.toString(),
//                entityType = EntityType.USER,
//                action = ActionType.CREATE,
//                field = "create new user",
//                oldValue = null,
//                newValue = "creating user:${dummyUser.username}",
//                userId = currentUser.id.toString()
//            )
//        }
//    }
//}