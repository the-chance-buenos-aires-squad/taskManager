package domain.customeExceptions

class InvalidIdException(prefix: String = "") : Exception("${prefix}ID can't be empty")
