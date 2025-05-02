package domain.customeExceptions

class InvalidCredentialsException (message: String = "Invalid username or password") : Exception(message)