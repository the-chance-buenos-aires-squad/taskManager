package data.dataSource.user

import com.google.common.truth.Truth.assertThat
import com.mongodb.client.model.DeleteOptions
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.dto.UserDto
import dummyData.DummyUser.dummyUserOneDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.runTest
import org.bson.conversions.Bson
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class MongoUserDataSourceTest {
    private lateinit var dataSource: MongoUserDataSource
    private var userCollection: MongoCollection<UserDto> = mockk(relaxed = true)
    private var testUser: UserDto = dummyUserOneDto

    @BeforeEach
    fun setUp() {
        dataSource = MongoUserDataSource(userCollection)
    }

    @Test
    fun `addUser should return false when insert is not acknowledged`() = runTest {
        val insertResult = mockk<InsertOneResult>(relaxed = true)
        every { insertResult.wasAcknowledged() } returns false
        coEvery { userCollection.insertOne(document = testUser, options = any()) } returns insertResult

        val result = dataSource.addUser(testUser)
        assertThat(result).isFalse()
    }

    @Test
    fun `addUser should call collection insertOne`() = runTest {
        val mockResult = mockk<InsertOneResult>()
        every { mockResult.wasAcknowledged() } returns true

        coEvery { userCollection.insertOne(document = testUser, options = any()) } returns mockResult

        val result = dataSource.addUser(testUser)

        coVerify { userCollection.insertOne(testUser, any()) }
        coEvery { userCollection.insertOne(testUser, any()).wasAcknowledged() }
        assertThat(result).isTrue()
    }

    @Test
    fun `getUserById should return user when found by id`() = runTest {
        val findFlow = mockk<FindFlow<UserDto>>()
        coEvery { userCollection.find(any<Bson>()) } returns findFlow
        coEvery { findFlow.collect(any()) } coAnswers {
            val collector = firstArg<FlowCollector<UserDto>>()
            collector.emit(testUser)
        }

        val result = dataSource.getUserById(dummyUserOneDto.id)

        assertThat(result).isEqualTo(testUser)
    }

    @Test
    fun `getUserByUserName should return user when found by his username`() = runTest {
        val findFlow = mockk<FindFlow<UserDto>>(relaxed = true)
        coEvery { userCollection.find(any<Bson>()) } returns findFlow
        coEvery { findFlow.collect(any()) } coAnswers {
            val collector = firstArg<FlowCollector<UserDto>>()
            collector.emit(testUser)
        }

        val result = dataSource.getUserByUserName(testUser.username)

        assertThat(result).isEqualTo(testUser)
    }

    @Test
    fun `user should be removed when we use deleteUser`() = runTest {
        dataSource.addUser(testUser)
        val deleted = mockk<DeleteResult>()
        coEvery { deleted.wasAcknowledged() } returns true
        coEvery { userCollection.deleteOne(any(), any<DeleteOptions>()) } returns deleted

        val result = dataSource.deleteUser(testUser.id)

        assertThat(result).isTrue()
    }

    @Test
    fun `deleteUser should return false when delete is not acknowledged`() = runTest {
        val deleted = mockk<DeleteResult>()
        coEvery { deleted.wasAcknowledged() } returns false
        coEvery { userCollection.deleteOne(any(), any<DeleteOptions>()) } returns deleted

        val result = dataSource.deleteUser(testUser.id)
        assertThat(result).isFalse()
    }

    @Test
    fun `should return all users when we use getUsers`() = runTest {
        val findFlow = mockk<FindFlow<UserDto>>()
        val user = testUser.copy(id = UUID.randomUUID().toString(), username = "second")

        coEvery { userCollection.find() } returns findFlow
        coEvery { findFlow.collect(any()) } coAnswers {
            val collector = firstArg<FlowCollector<UserDto>>()
            collector.emit(testUser)
            collector.emit(user)
        }

        val result = dataSource.getUsers()

        assertThat(result).containsExactly(testUser, user)
    }

    @Test
    fun `user should be updated using updateUser`() = runTest {
        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.wasAcknowledged() } returns true

        coEvery { userCollection.updateOne(any<Bson>(), any<Bson>(), any()) } answers {
            updateResult
        }

        val findFlow = mockk<FindFlow<UserDto>>()
        val updatedUser = testUser.copy(username = "aziz updates")
        coEvery { userCollection.find(any<Bson>()) } returns findFlow
        coEvery { findFlow.collect(any()) } coAnswers {
            val collector = firstArg<FlowCollector<UserDto>>()
            collector.emit(updatedUser)
        }

        val updated = dataSource.updateUser(updatedUser)
        assertThat(updated).isTrue()

        val result = dataSource.getUserById(testUser.id)
        assertThat(result).isEqualTo(updatedUser)
    }

    @Test
    fun `updateUser should return false when update is not acknowledged`() = runTest {
        val updateResult = mockk<UpdateResult>()
        coEvery { updateResult.wasAcknowledged() } returns false
        coEvery { userCollection.updateOne(any<Bson>(), any<Bson>(), any()) } returns updateResult

        val updatedUser = testUser.copy(username = "fail update")
        val updated = dataSource.updateUser(updatedUser)
        assertThat(updated).isFalse()
    }

}