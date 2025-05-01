package domain.customeExceptions

class UserNameEmptyException (message: String = "Username cannot be empty") : Exception(message) {
}