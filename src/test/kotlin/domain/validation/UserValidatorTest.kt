package domain.validation

import presentation.exceptions.InvalidConfirmPasswordException
import presentation.exceptions.InvalidLengthPasswordException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.exceptions.PasswordEmptyException
import presentation.exceptions.UserNameEmptyException

class UserValidatorTest {
    private val validator = UserValidator()

    @Test
    fun `should throw PasswordEmptyException when password is blank`() {
        assertThrows<PasswordEmptyException> {
            validator.validatePasswordConfirmation(" ", "validPass123")
        }
    }

    @Test
    fun `should throw PasswordEmptyException when confirm password is blank`() {
        assertThrows<PasswordEmptyException> {
            validator.validatePasswordConfirmation("validPass123", " ")
        }
    }

    @Test
    fun `should throw UserNameEmptyException when userName is blank`() {
        assertThrows<UserNameEmptyException> {
            validator.validateUsername(" ")
        }
    }

    @Test
    fun `should pass when userName is valid`() {
        validator.validateUsername("ahmed")
    }

    @Test
    fun `should throw InvalidLengthPasswordException when password is less than 6 chars`() {
        assertThrows<InvalidLengthPasswordException> {
            validator.validatePasswordConfirmation("123", "123")
        }
    }

    @Test
    fun `should throw InvalidConfirmPasswordException when passwords do not match`() {
        assertThrows<InvalidConfirmPasswordException> {
            validator.validatePasswordConfirmation("validPass123", "differentPass")
        }
    }

    @Test
    fun `should pass when password is valid and matches confirm password`() {
        validator.validatePasswordConfirmation("validPass123", "validPass123")
    }
}