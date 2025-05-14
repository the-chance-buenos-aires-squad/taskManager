package domain.usecases

import com.google.common.truth.Truth.assertThat
import data.dummyData.DummyAudits.DummyTaskAuditDto
import data.dummyData.DummyAudits.dummyProjectAudit_CreateAction
import data.repositories.AuditRepositoryImpl
import data.repositories.dataSource.AuditDataSource
import data.repositories.mappers.AuditDtoMapper
import domain.entities.ActionType
import domain.entities.EntityType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class AddAuditUseCaseTest {

    private lateinit var addAuditUseCase: AddAuditUseCase
    private lateinit var auditRepository: AuditRepositoryImpl

    @Test
    fun `should call addAudit from repository with given info`() = runTest {
        //given
        auditRepository = mockk(relaxed = true)
        addAuditUseCase = AddAuditUseCase(auditRepository)
        coEvery { auditRepository.addAudit(any()) } returns true

        //when
        addAuditUseCase.addAudit(
            dummyProjectAudit_CreateAction.entityId,
            dummyProjectAudit_CreateAction.entityType,
            dummyProjectAudit_CreateAction.action,
            dummyProjectAudit_CreateAction.field,
            dummyProjectAudit_CreateAction.oldValue,
            dummyProjectAudit_CreateAction.newValue,
            dummyProjectAudit_CreateAction.userId
        )
        //then
        coVerify { auditRepository.addAudit(any()) }
    }

    @Test
    fun `should  catch exceptions thrown from the dataSource when adding unSuccessful and print it`() = runTest {
        // given
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val exceptionMessage = "data source specific exception"
        val fakeAuditDataSource = mockk<AuditDataSource>()
        coEvery { fakeAuditDataSource.addAudit(any()) } throws Exception(exceptionMessage)

        val mockedMapper: AuditDtoMapper = mockk()
        every { mockedMapper.fromEntity(any()) } returns DummyTaskAuditDto
        val auditRepository = AuditRepositoryImpl(fakeAuditDataSource, mockedMapper)
        val addAuditUseCase = AddAuditUseCase(auditRepository)

        // when
        addAuditUseCase.addAudit(
            DummyTaskAuditDto.entityId,
            DummyTaskAuditDto.entityType?.let { EntityType.entries.find { e -> e.name == it } },
            DummyTaskAuditDto.action?.let { ActionType.entries.find { a -> a.name == it } },
            DummyTaskAuditDto.field,
            DummyTaskAuditDto.oldValue,
            DummyTaskAuditDto.newValue,
            DummyTaskAuditDto.userId
        )

        // then
        val output = outputStream.toString().trim()
        assertThat(output).contains("Failed to add audit:$exceptionMessage")
    }


}