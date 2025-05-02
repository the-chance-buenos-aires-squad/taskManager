package domain.customeExceptions

class PasswordEmptyException (message: String = "password cannot be empty !") : Exception(message)
