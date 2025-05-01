package data.repositories

import data.dataSource.CsvAuditDataSource
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuditRepositoryTest{

  private val mockedDataSource = mockk<CsvAuditDataSource>(relaxed = true)
  private lateinit var auditRepository: AuditRepository


  @BeforeEach
  fun setUp(){
   auditRepository = AuditRepository(mockedDataSource)
  }






 }