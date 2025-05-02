package domain.customeExceptions

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserEnterEmptyValueExceptionTest{
  @Test
  fun `should throw exception with default message`() {
   val exception = assertThrows<UserEnterEmptyValueException>{
    throw UserEnterEmptyValueException()
   }
   assertThat( exception.message).isEqualTo("you entered Invalid value!")
  }
 @Test
 fun `should throw exception with custom message`() {
  val exception = assertThrows<UserEnterEmptyValueException>{
   throw UserEnterEmptyValueException("you entered Invalid ID!")
  }
  assertThat( exception.message).isEqualTo("you entered Invalid ID!")
 }
 }