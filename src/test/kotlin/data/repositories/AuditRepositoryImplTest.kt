package data.repositories

import com.google.common.truth.Truth.assertThat
import data.dataSource.audit.CsvAuditDataSource
import data.dataSource.dummyData.createDummyAudits
import data.dataSource.dummyData.createDummyAudits.dummyProjectCreateAction
import data.dataSource.dummyData.createDummyAudits.dummyTaskCreateAction
import data.dummyData.DummyAudits
import data.repositories.dataSource.AuditDataSource
import data.repositories.mappers.AuditDtoMapper
import domain.repositories.AuditRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuditRepositoryImplTest {

    private lateinit var repository: AuditRepository
    private val auditDataSource = mockk<AuditDataSource>()
    private val mapper = mockk<AuditDtoMapper>()


    @BeforeEach
    fun setUp() {
        repository = AuditRepositoryImpl(auditDataSource, mapper)
    }


    @Test
    fun `should return empty list when no audits exist in data source`() = runTest {
        coEvery { auditDataSource.getAllAudit() } returns emptyList()

        val result = repository.getAllAudit()
        assertThat(result).isEmpty()

    }

    @Test
    fun `should return true when add audit successful in the data source`() = runTest {
        //given
        coEvery { auditDataSource.addAudit(mapper.fromType(DummyAudits.dummyProjectAudit_CreateAction)) } returns true

        //when
        val result = repository.addAudit(DummyAudits.dummyProjectAudit_CreateAction)

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should return all audits from data source`() = runTest {
        every { mapper.toType(createDummyAudits.dummyTaskCreateActionRow) } returns dummyTaskCreateAction
        every { mapper.toType(createDummyAudits.dummyProjectCreateActionRow) } returns dummyProjectCreateAction
        val expectedAudits = listOf(
            createDummyAudits.dummyTaskCreateActionRow,
            createDummyAudits.dummyProjectCreateActionRow,
        )
        coEvery { auditDataSource.getAllAudit() } returns expectedAudits

        val result = repository.getAllAudit()
        assertThat(result).isEqualTo(listOf(dummyTaskCreateAction, dummyProjectCreateAction))
    }

    @Test
    fun `should return false when add audit unSuccessful in the data source`() = runTest {
        //given
        coEvery { auditDataSource.addAudit(mapper.fromType(DummyAudits.dummyProjectAudit_CreateAction)) } returns false

        //when
        val result = repository.addAudit(DummyAudits.dummyProjectAudit_CreateAction)

        //then
        assertThat(result).isFalse()
    }


}