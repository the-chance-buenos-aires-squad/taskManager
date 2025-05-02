package domain.customeExceptions

class InvalidNameException(prefix: String = "") :
    Exception("${prefix}Name must be at least 2 letters")

