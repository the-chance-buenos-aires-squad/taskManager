package data.repositories.mappers

import com.google.common.truth.Truth
import data.dto.UserDto
import dummyData.DummyUser
import org.junit.jupiter.api.Test

class UserDtoMapperTest {

    private val entityUser = DummyUser.dummyUserOne
    private val dtoUser = DummyUser.dummyUserOneDto
    private val userDtoMapper = UserDtoMapper()

    @Test
    fun `should return dto object when mapping from entity to dto`() {
        //when
        val result = userDtoMapper.fromEntity(entityUser)

        //then
        Truth.assertThat(result).isInstanceOf(UserDto::class.java)
    }

}