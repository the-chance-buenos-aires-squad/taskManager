package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.CsvAuditDataSource
import data.dataSource.dummyData.createDummyAudits
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuditRepositoryImplTest{

  private val mockedDataSource = mockk<CsvAuditDataSource>(relaxed = true)
  private lateinit var auditRepositoryImpl: AuditRepositoryImpl


  @BeforeEach
  fun setUp(){
   auditRepositoryImpl = AuditRepositoryImpl(mockedDataSource)
  }
    @Test
    fun `should return empty list when no audits exist in data source`() {
        //given
        every { mockedDataSource.getAllAudit() } returns emptyList()
        //when
        val result = auditRepositoryImpl.getAllAudit()
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
        val result = auditRepositoryImpl.getAllAudit()
        //then
        assertThat(result).isEqualTo(expectedAudits)
    }





 }