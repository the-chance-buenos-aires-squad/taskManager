package domain.usecases

import domain.repositories.Repository
import org.buinos.domain.entities.User

class AuthenticationUseCase(private val repository: Repository<User>) {

}