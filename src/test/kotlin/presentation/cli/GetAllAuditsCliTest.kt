package presentation.cli

import com.google.common.truth.Truth.assertThat
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
        coEvery { getAllAuditUseCase.getAllAudit() } returns expectedAudits
        //when
        getAllAuditCli.displayAllAudits()
        //then
        coVerify { getAllAuditUseCase.getAllAudit() }
    }


    @Test
    fun `should display single audit correctly`() {
        //given
        val audit = createDummyAudits.dummyTaskCreateAction
        //when
        val result = getAllAuditCli.displaySingleAudit(audit)
        //then
        assertThat(result)
    }
}

