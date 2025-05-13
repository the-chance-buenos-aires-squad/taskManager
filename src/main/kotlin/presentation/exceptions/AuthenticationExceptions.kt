package presentation.exceptions

class InvalidConfirmPasswordException() : Exception("Passwords not match!")

class InvalidLengthPasswordException() : Exception("Password must be at least 6 characters!")

class CreateUserException(message: String = "Failed to create user") : Exception(message)
class UserNotLoggedInException(
    message: String = "Operation not allowed: User is not logged in."
) : RuntimeException(message)