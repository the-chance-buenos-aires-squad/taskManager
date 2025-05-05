package dummyData

import domain.entities.*
import java.time.LocalDateTime
import java.util.*

object DummyTaskData {
    val project = Project(
        id = UUID.randomUUID(),
        name = "Test Project",
        description = "Test Description",
        createdAt = LocalDateTime.now()
    )

    val currentUser = User(
        id = UUID.randomUUID(),
        username = "testUser",
        password = "password",
        role = UserRole.ADMIN,
        createdAt = LocalDateTime.now()
    )

    val assignedUser = User(
        id = UUID.randomUUID(),
        username = "assignedUser",
        password = "password",
        role = UserRole.MATE,
        createdAt = LocalDateTime.now()
    )

    val taskStates = listOf(
        TaskState(
            id = UUID.randomUUID().toString(),
            name = "todo",
            projectId = project.id.toString()
        ),
        TaskState(
            id = UUID.randomUUID().toString(),
            name = "in progress",
            projectId = project.id.toString()
        )
    )

    val validInputs = listOf(
        "Test Title", // Title
        "Test Description", // Description
        "1", // State selection
        "assignedUser" // Username
    )

    val emptyTitleInputs = listOf(
        "", // Empty title first attempt
        "" // Empty title second attempt
    )

    val emptyDescriptionInputs = listOf(
        "Test Title", // Valid title
        "", // Empty description first attempt
        "" // Empty description second attempt
    )

    val invalidStateInputs = listOf(
        "Test Title", // Valid title
        "Test Description", // Valid description
        "3", // Invalid state first attempt
        "4" // Invalid state second attempt
    )


    val invalidUserInputs = listOf(
        "Test Title", // Valid title
        "Test Description", // Valid description
        "1", // Valid state
        "nonexistentUser", // Invalid user first attempt
        "anotherNonexistentUser" // Invalid user second attempt
    )


}