package domain.usecases

import com.google.common.truth.Truth.assertThat
import data.dataSource.dummyData.createDummyAudits
import data.repositories.AuditRepositoryImpl
import io.mockk.every
import io.mockk.mockk
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
    fun `should return empty list when no audits exist in data source`() {
        //given
        every { getAllAuditUseCase.getAllAudit() } returns emptyList()
        //when
        val result = getAllAuditUseCase.getAllAudit()
        //then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return all audits from data source`() {
        //given
        val expectedAudits = listOf(
            createDummyAudits.dummyTaskCreateAction,
            createDummyAudits.dummyProjectCreateAction,
            createDummyAudits.dummyUserUpdateAction
        )
        every { mockedAuditRepositoryImpl.getAllAudit() } returns expectedAudits
        //when
        val result = getAllAuditUseCase.getAllAudit()
        //then
        assertThat(result).isEqualTo(expectedAudits)
    }

}