package data.repositories.mappers

import com.google.common.truth.Truth.assertThat
import dummyData.DummyUser.dummyUserOne
import dummyData.DummyUser.dummyUserOneRow
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class UserMapperTest{
    private val userMapper = UserMapper()


    @Test
    fun `should return Audit object when Calling mapRowToEntity`(){
        //when
        val expectedUser = userMapper.mapRowToEntity(dummyUserOneRow)

        //then
        assertThat(expectedUser).isEqualTo(dummyUserOne)
    }


    @Test
    fun `should return row string when calling mapEntityToRow`(){
        //when
        val expectedRow = userMapper.mapEntityToRow(dummyUserOne)
        //then
        assertThat(expectedRow).isEqualTo(dummyUserOneRow)
    }


}