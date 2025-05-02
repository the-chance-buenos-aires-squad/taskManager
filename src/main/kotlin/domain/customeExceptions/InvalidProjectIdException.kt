package domain.customeExceptions

class InvalidProjectIdException(prefix: String = "") :
    Exception("${prefix}Project ID must start with 'P' followed by at least two digits (e.g., P01, P123)")

