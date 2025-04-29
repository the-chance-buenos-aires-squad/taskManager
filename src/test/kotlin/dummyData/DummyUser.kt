package dummyData

import org.buinos.domain.entities.User
import org.buinos.domain.entities.UserRole
import java.time.LocalDateTime

object DummyUser {
     val testUserOne = User(
        id = "1",
        username = "testUser1",
        password = "testPassword1",
        role = UserRole.ADMIN,
        createdAt = LocalDateTime.now()
    )

     val testUserTwo = User(
        id = "2",
        username = "testUser2",
        password = "testPassword2",
        role = UserRole.MATE,
        createdAt = LocalDateTime.now()
    )
}