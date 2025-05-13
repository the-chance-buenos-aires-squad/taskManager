package data.dataSource.user

import com.google.common.truth.Truth.assertThat
import data.dto.UserDto
import domain.entities.UserRole
import dummyData.createDummyUser
import org.junit.jupiter.api.Test
import java.util.UUID

class UserDtoParserTest {



    private val userDtoParser = UserDtoParser()


    @Test
    fun `parsing to dto should return dto object with the same properties values`() {
        //when
        val result = userDtoParser.toDto(row)

        assertThat(result.username).isEqualTo(dto.username)
        assertThat(result.id).isEqualTo(dto.id)
    }

    @Test
    fun `parsing from dto should return row  with the same values`() {
        //when
        val result = userDtoParser.fromDto(dto)

        assertThat(result[0]).isEqualTo(row[0])
    }

    val dummyUserOne = createDummyUser(
        id = UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b221"),
        username = "adminUserName",
        role = UserRole.ADMIN
    )

    private val row: List<String> = listOf(
        UUID.fromString("e7a1a8b0-51e2-4e61-b4f6-7c9f3e05b221").toString(),
        "adminUserName",
        "adminPassword",
        UserRole.ADMIN.name,
        dummyUserOne.createdAt.toString()
    )
    private val dto = UserDto(
        id = dummyUserOne.id.toString(),
        username = dummyUserOne.username,
        password = "adminUserName",
        role = dummyUserOne.role,
        createdAt = dummyUserOne.createdAt.toString()
    )
}