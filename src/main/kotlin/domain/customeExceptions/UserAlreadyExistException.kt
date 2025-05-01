package domain.customeExceptions

class UserAlreadyExistException (message: String = "Username Already Exist !") : Exception(message) {
}