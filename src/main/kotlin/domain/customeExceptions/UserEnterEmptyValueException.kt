package domain.customeExceptions

class UserEnterEmptyValueException(message: String = "you entered Invalid value!") : Exception(message)