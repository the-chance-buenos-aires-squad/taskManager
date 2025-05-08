package data.repositories.mappers

import com.google.common.truth.Truth.assertThat
import data.dto.UserDto
import domain.entities.User
import dummyData.DummyUser
import org.junit.jupiter.api.Test

class UserDtoMapperTest {

    private val entityUser = DummyUser.dummyUserOne
    private val dtoUser = DummyUser.dummyUserOneDto
    private val userDtoMapper = UserDtoMapper()

    @Test
    fun `should return dto object when mapping from entity`() {
        //when
        val result = userDtoMapper.fromEntity(entityUser)

        //then
        assertThat(result).isInstanceOf(UserDto::class.java)
    }

    @Test
    fun `should return entity object when mapping to entity`() {
        //when
        val result = userDtoMapper.toEntity(dtoUser)

        //then
        assertThat(result).isInstanceOf(User::class.java)
    }

}