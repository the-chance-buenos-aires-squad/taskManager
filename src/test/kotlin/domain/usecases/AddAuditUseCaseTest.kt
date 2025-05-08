package domain.usecases

import com.google.common.truth.Truth.assertThat
import data.dummyData.DummyAudits
import data.dummyData.DummyAudits.dummyProjectAudit_CreateAction
import data.repositories.AuditRepositoryImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddAuditUseCaseTest {

    private lateinit var addAuditUseCase: AddAuditUseCase
    private val auditRepository: AuditRepositoryImpl = mockk(relaxed = true)


    @BeforeEach
    fun setUp() {
        addAuditUseCase = AddAuditUseCase(auditRepository)
    }

    @Test
    fun `should return audit object with given info when adding is successful`() = runTest{
        //given
        coEvery { auditRepository.addAudit(any()) } returns true

        val resultAudit = addAuditUseCase.addAudit(
            dummyProjectAudit_CreateAction.entityId,
            dummyProjectAudit_CreateAction.entityType,
            dummyProjectAudit_CreateAction.action,
            dummyProjectAudit_CreateAction.field,
            dummyProjectAudit_CreateAction.oldValue,
            dummyProjectAudit_CreateAction.newValue,
            dummyProjectAudit_CreateAction.userId
        )
        //then
        assertThat(resultAudit).isNotNull()
        assertThat(resultAudit?.entityId).isEqualTo(dummyProjectAudit_CreateAction.entityId)
    }

    @Test
    fun `should return null  when adding is unSuccessful`()= runTest{
        //given
        coEvery { auditRepository.addAudit(any()) } returns false

        val resultAudit = addAuditUseCase.addAudit(
            DummyAudits.dummyProjectAudit_CreateAction.entityId,
            DummyAudits.dummyProjectAudit_CreateAction.entityType,
            DummyAudits.dummyProjectAudit_CreateAction.action,
            DummyAudits.dummyProjectAudit_CreateAction.field,
            DummyAudits.dummyProjectAudit_CreateAction.oldValue,
            DummyAudits.dummyProjectAudit_CreateAction.newValue,
            DummyAudits.dummyProjectAudit_CreateAction.userId
        )

        //then
        assertThat(resultAudit).isEqualTo(null)
    }


}