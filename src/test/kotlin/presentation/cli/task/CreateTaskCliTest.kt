package presentation.cli.task

import com.google.common.truth.Truth.assertThat
import domain.entities.*
import domain.repositories.AuthRepository
import domain.usecases.AddAuditUseCase
import domain.usecases.CreateTaskUseCase
import domain.usecases.project.GetAllProjectsUseCase
import domain.usecases.taskState.GetAllTaskStatesUseCase
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import presentation.UiController
import java.time.LocalDateTime
import java.util.*

class CreateTaskCliTest {

    private val createTaskUseCase = mockk<CreateTaskUseCase>()
    private val getAllProjectsUseCase = mockk<GetAllProjectsUseCase>()
    private val addAuditUseCase = mockk<AddAuditUseCase>(relaxed = true)
    private val authRepository = mockk<AuthRepository>()
    private val getAllTaskStatesUseCase = mockk<GetAllTaskStatesUseCase>()
    private val uiController = mockk<UiController>(relaxed = true)

    private lateinit var cli: CreateTaskCli

    private val testUser = User(
        id = UUID.randomUUID(),
        username = "tester",
        password = "secret",
        role = UserRole.ADMIN,
        createdAt = LocalDateTime.now()
    )

    @BeforeEach
    fun setUp() {
        cli = CreateTaskCli(
            createTaskUseCase = createTaskUseCase,
            getAllProjectsUseCase = getAllProjectsUseCase,
            addAuditUseCase = addAuditUseCase,
            authRepository = authRepository,
            getAllStatesUseCase = getAllTaskStatesUseCase,
            uiController = uiController,
        )
    }

}