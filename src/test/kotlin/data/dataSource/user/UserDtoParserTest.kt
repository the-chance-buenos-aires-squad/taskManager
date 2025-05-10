package data.dataSource.user

import com.google.common.truth.Truth.assertThat
import dummyData.DummyUser
import org.junit.jupiter.api.Test

class UserDtoParserTest {

    private val userDtoParser = UserDtoParser()
    private val row: List<String> = DummyUser.dummyUserOneRow
    private val dto = DummyUser.dummyUserOneDto


    @Test
    fun `parsing to dto should return dto object with the same properties values`() {
        //when
        val result = userDtoParser.toDto(row)

        assertThat(result).isEqualTo(dto)
    }

    @Test
    fun `parsing from dto should return row  with the same values`() {
        //when
        val result = userDtoParser.fromDto(dto)

        assertThat(result).isEqualTo(row)
    }


}