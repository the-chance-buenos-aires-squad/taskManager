package domain.customeExceptions

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserEnterInvalidValueExceptionTest {

    @Test
    fun `should throw exception with default message`() {
        val exception = assertThrows<UserEnterInvalidValueException> {
            throw UserEnterInvalidValueException()
        }
        assertThat(exception.message).isEqualTo("you entered Invalid value!")
    }

    @Test
    fun `should throw exception with custom message`() {
        val exception = assertThrows<UserEnterInvalidValueException> {
            throw UserEnterInvalidValueException("you entered Invalid ID!")
        }
        assertThat(exception.message).isEqualTo("you entered Invalid ID!")
    }
}