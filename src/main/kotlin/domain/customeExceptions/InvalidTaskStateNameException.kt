package domain.customeExceptions

class InvalidTaskStateNameException(prefix: String = "") :
    Exception("${prefix}Task State Name must be at least 2 letters")

