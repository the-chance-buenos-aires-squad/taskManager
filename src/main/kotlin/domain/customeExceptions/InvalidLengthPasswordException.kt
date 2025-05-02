package domain.customeExceptions

class InvalidLengthPasswordException (message: String = "Password must be at least 6 characters!") : Exception(message)