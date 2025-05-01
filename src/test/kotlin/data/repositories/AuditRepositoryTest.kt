package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.CsvAuditDataSource
import data.dummyData.DummyAudits
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuditRepositoryTest {

    private val mockedDataSource = mockk<CsvAuditDataSource>(relaxed = true)
    private lateinit var auditRepository: AuditRepository


    @BeforeEach
    fun setUp() {
        auditRepository = AuditRepository(mockedDataSource)
    }



    @Test
    fun `should return true when add audit successful in the data source`(){
        //given
        every { mockedDataSource.addAudit(DummyAudits.dummyProjectAudit_CreateAction) } returns true

        //when
        val result = auditRepository.addAudit(DummyAudits.dummyProjectAudit_CreateAction)

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when add audit unSuccessful in the data source`(){
        //given
        every { mockedDataSource.addAudit(DummyAudits.dummyProjectAudit_CreateAction) } returns false

        //when
        val result = auditRepository.addAudit(DummyAudits.dummyProjectAudit_CreateAction)

        //then
        assertThat(result).isFalse()
    }

}