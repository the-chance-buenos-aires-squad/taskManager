package domain.customeExceptions

class InvalidConfirmPasswordException(message: String = "Passwords do not match!") : Exception(message)

class InvalidCredentialsException(message: String = "Invalid username or password") : Exception(message)

class PasswordEmptyException(message: String = "password cannot be empty !") : Exception(message)

class UserNameEmptyException(message: String = "Username cannot be empty !") : Exception(message)

class InvalidLengthPasswordException(
    message: String = "Password must be at least 6 characters!"
) : Exception(message)

class CreateUserException(message: String = "Failed to create user") : Exception(message)