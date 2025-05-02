package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.CsvAuditDataSource
import data.dataSource.dummyData.createDummyAudits
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuditRepositoryTest{

  private val mockedDataSource = mockk<CsvAuditDataSource>(relaxed = true)
  private lateinit var auditRepository: AuditRepository


  @BeforeEach
  fun setUp(){
   auditRepository = AuditRepository(mockedDataSource)
  }
    @Test
    fun `should return empty list when no audits exist in data source`() {
        //given
        every { mockedDataSource.getAllAudit() } returns emptyList()
        //when
        val result = auditRepository.getAllAudit()
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
        every { mockedDataSource.getAllAudit() } returns expectedAudits
        //when
        val result = auditRepository.getAllAudit()
        //then
        assertThat(result).isEqualTo(expectedAudits)
    }





 }