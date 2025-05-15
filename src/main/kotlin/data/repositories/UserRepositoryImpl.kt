package data.repositories

import data.dataSource.util.hash.PasswordHash
import data.dto.UserDto
import data.exceptions.FailedUserSaveException
import data.exceptions.InvalidCredentialsException
import data.exceptions.UserNameAlreadyExistException
import data.repositories.dataSource.UserDataSource
import data.repositories.mappers.UserDtoMapper
import domain.entities.*
import domain.repositories.AuditRepository
import domain.repositories.AuthRepository
import domain.repositories.UserRepository
import java.time.LocalDateTime
import java.util.*


class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val userMapper: UserDtoMapper,
    private val md5Hash: PasswordHash,
    private val authRepository: Lazy<AuthRepository>,
    private val auditRepository: AuditRepository
) : UserRepository {


    override suspend fun addUser(userName: String, password: String): User {
        return authRepository.value.runIfLoggedIn { currentUser ->
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

            auditRepository.addAudit(
                Audit(
                    id = UUID.randomUUID(),
                    entityId = newUser.id,
                    entityType = EntityType.USER,
                    action = ActionType.CREATE,
                    field = "",
                    originalValue = "new User:${newUser.username}",
                    modifiedValue = "",
                    userId = currentUser.id.toString(),
                    timestamp = LocalDateTime.now()
                )
            )

            userMapper.toEntity(newUser)
        }
    }

    //todo not used !!
    override suspend fun updateUser(user: User): Boolean {
        return userDataSource.updateUser(userMapper.fromEntity(user))
    }

    //todo not used !!
    override suspend fun deleteUser(user: User): Boolean {
        return userDataSource.deleteUser(user.id.toString())
    }

    //todo not used !!
    override suspend fun getUserById(id: UUID): User? {
        return userDataSource.getUserById(id.toString())?.let {
            userMapper.toEntity(it)
        }
    }

    override suspend fun getUserByUserName(userName: String): User? {
        return userDataSource.getUserByUserName(userName)?.let { userMapper.toEntity(it) }
    }

    //todo : not used !!
    override suspend fun getUsers(): List<User> {
        val usersRows = userDataSource.getUsers()
        return usersRows.map { userRow ->
            userMapper.toEntity(userRow)
        }
    }

    override suspend fun loginUser(userName: String, password: String): User {
        val userDto = userDataSource.getUserByUserName(userName) ?: throw InvalidCredentialsException()

        val hashInput = md5Hash.generateHash(password)
        if (userDto.password != hashInput) throw InvalidCredentialsException()

        return userMapper.toEntity(userDto)
    }


}