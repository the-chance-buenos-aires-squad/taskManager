package data.dataSource.util

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class PasswordHashTest {
    private val passwordHasher = PasswordHash()

    @Test
    fun `hash should return correct MD5 hash for normal string`() {
        // given
        val input = "password"
        val expectedHash = "5f4dcc3b5aa765d61d8327deb882cf99"

        // when
        val result = passwordHasher.hash(input)

        // then
        assertThat(expectedHash).isEqualTo(result)
    }

    @Test
    fun `hash should return correct MD5 hash for empty string`() {
        // given
        val input = ""
        val expectedHash = "d41d8cd98f00b204e9800998ecf8427e"

        // when
        val result = passwordHasher.hash(input)

        // then
        assertThat(expectedHash).isEqualTo(result)
    }

    @Test
    fun `hash should return correct MD5 hash for special characters`() {
        // given
        val input = "hello@123"
        val expectedHash = "56bf377cae026633fe10d7401f40dbb4"

        // when
        val result = passwordHasher.hash(input)

        // then
        assertThat(expectedHash).isEqualTo(result)
    }

    @Test
    fun `hash should be case-sensitive`() {
        // given
        val inputLowercase = "password"
        val inputUppercase = "PASSWORD"

        // when
        val resultLowercase = passwordHasher.hash(inputLowercase)
        val resultUppercase = passwordHasher.hash(inputUppercase)

        // then
        assertThat(resultLowercase).isNotEqualTo(resultUppercase)
    }
}