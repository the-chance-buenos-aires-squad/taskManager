package auth

import com.google.common.truth.Truth.assertThat
import data.exceptions.UserMateNotAllowedException
import dummyData.DummyUser.dummyUserOne
import dummyData.DummyUser.dummyUserTwo
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import presentation.exceptions.UserNotLoggedInException

class UserSessionImplTest {

    private val userSession = UserSessionImpl()

    private val adminUser = dummyUserOne

    private val mateUser = dummyUserTwo;

    @Test
    fun `runIfLoggedIn should return result when user is logged in`() = runTest {
        userSession.setCurrentUser(adminUser)

        val result = userSession.runIfLoggedIn {
            "Hello, ${it.username}"
        }

        assertThat(result).isEqualTo("Hello, ${adminUser.username}")
    }

    @Test
    fun `runIfLoggedIn should throw UserNotLoggedInException when no user is logged in`() = runTest {
        userSession.setCurrentUser(null)

        assertThrows<UserNotLoggedInException> {
            userSession.runIfLoggedIn { "Should not run" }
        }

    }

    @Test
    fun `runIfUserIsAdmin should return result for admin user`() = runTest {
        userSession.setCurrentUser(adminUser)

        val result = userSession.runIfUserIsAdmin {
            "Admin Access for ${it.username}"
        }

        assertThat(result).isEqualTo("Admin Access for adminUserName")
    }

    @Test
    fun `runIfUserIsAdmin should throw UserNotLoggedInException when no user is logged in`() = runTest {
        userSession.setCurrentUser(null)

        assertThrows<UserNotLoggedInException> {
            userSession.runIfUserIsAdmin { "Should not run" }
        }
    }

    @Test
    fun `runIfUserIsAdmin should throw UserMateNotAllowedException for mate user`() = runTest {
        userSession.setCurrentUser(mateUser)

        assertThrows<UserMateNotAllowedException> {
            userSession.runIfUserIsAdmin { "Should not run" }
        }
    }
}