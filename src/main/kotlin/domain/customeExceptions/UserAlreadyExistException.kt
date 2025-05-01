package domain.customeExceptions

class UserAlreadyExistException (message: String = "Username already exists!") : Exception(message) {
}