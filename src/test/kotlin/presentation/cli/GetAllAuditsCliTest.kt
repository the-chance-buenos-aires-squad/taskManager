package presentation.cli

import data.dataSource.dummyData.createDummyAudits
import domain.usecases.GetAllAuditUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import presentation.UiController
import kotlin.test.Test

class GetAllAuditsCliTest {

    private lateinit var getAllAuditCli: GetAllAuditsCli
    private lateinit var getAllAuditUseCase: GetAllAuditUseCase
    private val uiController: UiController = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        getAllAuditUseCase = mockk()
        getAllAuditCli = GetAllAuditsCli(getAllAuditUseCase, uiController)
    }

    @Test
    fun `should return all audits we entered`() = runTest {
        //given
        val expectedAudits = listOf(
            createDummyAudits.dummyTaskCreateAction,
            createDummyAudits.dummyTaskCreateAction,
            createDummyAudits.dummyUserUpdateAction
        )
        coEvery { getAllAuditUseCase.execute() } returns expectedAudits
        //when
        getAllAuditCli.displayAllAudits()
        //then
        coVerify { getAllAuditUseCase.execute() }
    }

    @Test
    fun `should handle exception and print error message`() = runTest {
        // Given
        val errorMessage = "Database error"
        coEvery { getAllAuditUseCase.execute() } throws RuntimeException(errorMessage)

        // When
        getAllAuditCli.displayAllAudits()

        // Then
        coVerify { uiController.printMessage("error from data source:$errorMessage") }
    }

}

