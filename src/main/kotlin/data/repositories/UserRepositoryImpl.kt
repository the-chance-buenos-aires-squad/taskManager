package data.repositories

import auth.UserSession
import data.dataSource.util.hash.PasswordHash
import data.dto.UserDto
import data.exceptions.FailedUserSaveException
import data.exceptions.UserMateNotAllowedException
import data.exceptions.UserNameAlreadyExistException
import data.repositories.dataSource.UserDataSource
import data.repositories.mappers.UserDtoMapper
import domain.entities.*
import domain.repositories.AuditRepository
import domain.repositories.UserRepository
import java.time.LocalDateTime
import java.util.*

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val userMapper: UserDtoMapper,
    private val md5Hash: PasswordHash,
    private val auditRepository: AuditRepository,
    private val userSession: UserSession
) : UserRepository {

    override suspend fun addUser(userName: String, password: String): User {
        if (userSession.getCurrentUser() != null && userSession.getCurrentUser()!!.role == UserRole.ADMIN) {
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
                    userId = userSession.getCurrentUser()!!.id.toString(),
                    timestamp = LocalDateTime.now()
                )
            )

            return userMapper.toType(newUser)
        }else{
            throw UserMateNotAllowedException()
        }
    }

    override suspend fun getUserByUserName(userName: String): UserDto? {
        return userDataSource.getUserByUserName(userName)
    }

}