package data.repositories

import data.dataSource.util.hash.PasswordHash
import data.dto.UserDto
import data.exceptions.FailedUserSaveException
import data.exceptions.InvalidCredentialsException
import data.exceptions.UserNameAlreadyExistException
import data.repositories.dataSource.UserDataSource
import data.repositories.mappers.UserDtoMapper
import domain.entities.User
import domain.entities.UserRole
import domain.repositories.UserRepository
import java.time.LocalDateTime
import java.util.*


class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val userMapper: UserDtoMapper,
    private val md5Hash : PasswordHash
) : UserRepository {


    override suspend fun addUser(userName: String, password: String): User {
        val userDto = this.getUserByUserName(userName)
        if (userDto != null) throw UserNameAlreadyExistException()

        val newUser = UserDto(
            id = UUID.randomUUID().toString(),
            username = userName.trim(),
            password = md5Hash.generateHash(password),
            role = UserRole.MATE,
            createdAt = LocalDateTime.now().toString()
        )

        if (!userDataSource.addUser(newUser)) {
            throw FailedUserSaveException()
        }

        return userMapper.toType(newUser)

    }

    override suspend fun updateUser(user: User): Boolean {
        return userDataSource.updateUser(userMapper.fromType(user))
    }

    override suspend fun deleteUser(user: User): Boolean {
        return userDataSource.deleteUser(user.id.toString())
    }

    override suspend fun getUserById(id: UUID): User? {
        return userDataSource.getUserById(id.toString())?.let {
            userMapper.toType(it)
        }
    }

    override suspend fun getUserByUserName(userName: String): User? {
        return userDataSource.getUserByUserName(userName)?.let { userMapper.toType(it) }
    }

    override suspend fun getUsers(): List<User> {
        val usersRows = userDataSource.getUsers()
        return usersRows.map { userRow ->
            userMapper.toType(userRow)
        }
    }

    override suspend fun loginUser(userName: String, password: String) : User {
        val userDto = userDataSource.getUserByUserName(userName) ?: throw InvalidCredentialsException()

        val hashInput =  md5Hash.generateHash(password)
        if (userDto.password != hashInput) throw InvalidCredentialsException()

        return userMapper.toType(userDto)
    }


}