package data.exceptions

class TaskStateNameException() : Exception("Task Name Already exist!")
class InvalidCredentialsException() : Exception("Invalid username or password")
class UserNameEmptyException() : Exception("Username cannot be empty !")
class UserNameAlreadyExistException() : Exception("Username already exist !")
class FailedUserSaveException() : Exception("Failed to save user!")
