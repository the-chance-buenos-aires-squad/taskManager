package domain.usecases

import domain.repositories.Repository
import org.buinos.domain.entities.User

class CreateUserUseCase(private val repository: Repository<User>) {

}