package presentation.exceptions

class UserNameEmptyException() : Exception("Username cannot be empty !")
class PasswordEmptyException(message: String = "password cannot be empty !") : Exception(message)
