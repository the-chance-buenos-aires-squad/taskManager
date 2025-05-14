package domain.usecases.audit

import com.google.common.truth.Truth.assertThat
import data.dataSource.dummyData.createDummyAudits
import data.repositories.AuditRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllAuditUseCaseTest {
    private lateinit var getAllAuditUseCase: GetAllAuditUseCase
    private val mockedAuditRepositoryImpl: AuditRepositoryImpl = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        getAllAuditUseCase = GetAllAuditUseCase(mockedAuditRepositoryImpl)
    }

    @Test
    fun `should return empty list when no audits exist in data source`() = runTest {
        //given
        coEvery { getAllAuditUseCase.execute() } returns emptyList()
        //when
        val result = getAllAuditUseCase.execute()
        //then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return all audits from data source`() = runTest {
        //given
        val expectedAudits = listOf(
            createDummyAudits.dummyTaskCreateAction,
            createDummyAudits.dummyProjectCreateAction,
            createDummyAudits.dummyUserUpdateAction
        )
        coEvery { mockedAuditRepositoryImpl.getAllAudit() } returns expectedAudits
        //when
        val result = getAllAuditUseCase.execute()
        //then
        assertThat(result).isEqualTo(expectedAudits)
    }

}