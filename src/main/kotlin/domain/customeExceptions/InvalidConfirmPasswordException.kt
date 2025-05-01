package domain.customeExceptions

class InvalidConfirmPasswordException (message: String = "Passwords do not match!") : Exception(message)
