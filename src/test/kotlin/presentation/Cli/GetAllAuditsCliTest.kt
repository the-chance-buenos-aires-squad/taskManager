package presentation.Cli

import com.google.common.truth.Truth.assertThat
import data.dataSource.dummyData.createDummyAudits
import domain.usecases.GetAllAuditUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import presentation.UiController
import kotlin.test.Test

class GetAllAuditsCliTest {

    private lateinit var getAllAuditCli: GetAllAuditsCli
    private lateinit var getAllAuditUseCase: GetAllAuditUseCase
    private val uiController:UiController = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        getAllAuditUseCase = mockk()
        getAllAuditCli = GetAllAuditsCli(getAllAuditUseCase,uiController)
    }

    @Test
    fun `should return all audits we entered`() {
        //given
        val expectedAudits = listOf(
            createDummyAudits.dummyTaskCreateAction,
            createDummyAudits.dummyTaskCreateAction,
            createDummyAudits.dummyUserUpdateAction
        )
        every { getAllAuditUseCase.getAllAudit() } returns expectedAudits
        //when
        getAllAuditCli.displayAllAudits()
        //then
        verify { getAllAuditUseCase.getAllAudit() }
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

