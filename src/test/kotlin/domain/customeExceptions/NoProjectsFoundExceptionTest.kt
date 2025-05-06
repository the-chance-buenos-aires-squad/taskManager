package domain.customeExceptions

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class NoProjectsFoundExceptionTest {

    @Test
    fun `should throw exception with default message`() {
        val exception = org.junit.jupiter.api.assertThrows<NoProjectsFoundException> {
            throw NoProjectsFoundException()
        }
        assertThat(exception.message).isEqualTo("Not projects found")
    }

    @Test
    fun `should throw exception with custom message`() {
        val exception = org.junit.jupiter.api.assertThrows<NoProjectsFoundException> {
            throw NoProjectsFoundException("you entered Invalid ID!")
        }
        assertThat(exception.message).isEqualTo("you entered Invalid ID!")
    }
}