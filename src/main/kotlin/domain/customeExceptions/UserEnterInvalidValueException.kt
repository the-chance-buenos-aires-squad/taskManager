package domain.customeExceptions

class UserEnterInvalidValueException(message: String = "you entered Invalid value!") : Exception(message)
class NoProjectsFoundException(message: String = "Not projects found") : Exception(message)