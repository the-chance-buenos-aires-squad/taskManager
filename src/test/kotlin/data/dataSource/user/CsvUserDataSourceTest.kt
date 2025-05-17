package data.dataSource.user

import com.google.common.truth.Truth.assertThat
import data.dataSource.util.CsvHandler
import dummyData.DummyUser.dummyUserOneDto
import dummyData.DummyUser.dummyUserOneRow
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class CsvUserDataSourceTest {

    private lateinit var file: File
    private lateinit var csvHandler: CsvHandler
    private lateinit var parser: UserDtoParser
    private lateinit var dataSource: CsvUserDataSource


    @BeforeEach
    fun setUp() {
        file = mockk(relaxed = true)
        csvHandler = mockk(relaxed = true)
        parser = mockk(relaxed = true)
        dataSource = CsvUserDataSource(csvHandler, parser, file)
    }

    @Test
    fun `should return true when user added successfully`() = runTest {
        every { parser.toType(dummyUserOneDto) } returns dummyUserOneRow
        every { csvHandler.write(dummyUserOneRow, file, true) } just Runs

        val result = dataSource.addUser(dummyUserOneDto)
        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when exception occurs while adding user`() = runTest {
        every { parser.toType(dummyUserOneDto) } returns dummyUserOneRow
        every { csvHandler.write(dummyUserOneRow, file, true) } throws RuntimeException("Failed")

        val result = dataSource.addUser(dummyUserOneDto)
        assertThat(result).isFalse()
    }

    @Test
    fun `get user by id should return correct user`() = runTest {
        every { file.exists() } returns true
        every { csvHandler.read(file) } returns listOf(dummyUserOneRow)
        every { parser.fromType(dummyUserOneRow) } returns dummyUserOneDto

        val result = dataSource.getUserById(dummyUserOneDto.id)
        assertThat(result).isEqualTo(dummyUserOneDto)
    }

    @Test
    fun `get user by id should return null if not found`() = runTest {
        every { file.exists() } returns true
        every { csvHandler.read(file) } returns emptyList()

        val result = dataSource.getUserById(dummyUserOneDto.id)
        assertThat(result).isNull()
    }

    @Test
    fun `get user by username should return correct user`() = runTest {
        every { file.exists() } returns true
        every { csvHandler.read(file) } returns listOf(dummyUserOneRow)
        every { parser.fromType(dummyUserOneRow) } returns dummyUserOneDto

        val result = dataSource.getUserByUserName(dummyUserOneDto.username)
        assertThat(result).isEqualTo(dummyUserOneDto)
    }

    @Test
    fun `get user by username should return null if not found`() = runTest {
        every { file.exists() } returns true
        every { csvHandler.read(file) } returns emptyList()

        val result = dataSource.getUserByUserName(dummyUserOneDto.username)
        assertThat(result).isNull()
    }

    @Test
    fun `delete user should return false when user not found`() = runTest {
        every { file.exists() } returns true
        every { csvHandler.read(file) } returns emptyList()

        val result = dataSource.deleteUser(dummyUserOneDto.id)
        assertThat(result).isFalse()
    }

    @Test
    fun `get users should return list of users`() = runTest {
        every { file.exists() } returns true
        every { csvHandler.read(file) } returns listOf(dummyUserOneRow, dummyUserOneRow)
        every { parser.fromType(dummyUserOneRow) } returns dummyUserOneDto

        val result = dataSource.getUsers()
        assertThat(result).hasSize(2)
    }

    @Test
    fun `get users should return empty list when file does not exist`() = runTest {
        every { file.exists() } returns false

        val result = dataSource.getUsers()
        assertThat(result).isEmpty()
    }

    @Test
    fun `update user should return false when user not found`() = runTest {
        every { file.exists() } returns true
        every { csvHandler.read(file) } returns emptyList()

        val result = dataSource.updateUser(dummyUserOneDto)
        assertThat(result).isFalse()
    }
}