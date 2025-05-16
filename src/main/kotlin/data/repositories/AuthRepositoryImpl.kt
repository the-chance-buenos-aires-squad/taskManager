package data.repositories

import auth.UserSession
import data.dataSource.util.hash.PasswordHash
import data.dto.UserDto
import data.exceptions.InvalidCredentialsException
import data.repositories.mappers.UserDtoMapper
import domain.entities.User
import domain.repositories.AuthRepository
import domain.repositories.UserRepository


class AuthRepositoryImpl(
    private val userRepository: UserRepository,
    private val session: UserSession,
    private val md5Hash: PasswordHash,
    private val userMapper: UserDtoMapper
) : AuthRepository {

    override suspend fun login(username: String, password: String): User {
        val userDto: UserDto = userRepository.getUserByUserName(username) ?: throw InvalidCredentialsException()
        val hashInput = md5Hash.generateHash(password)
        if (userDto.password != hashInput) throw InvalidCredentialsException()
        session.setCurrentUser(userMapper.toEntity(userDto))
        return userMapper.toEntity(userDto)
    }


    override suspend fun logout() {
        session.setCurrentUser(null)
    }
}