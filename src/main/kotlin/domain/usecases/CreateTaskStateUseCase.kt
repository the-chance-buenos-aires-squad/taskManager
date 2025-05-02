package domain.usecases

import domain.entities.State
import domain.repositories.TaskStateRepository

class CreateTaskStateUseCase(private val repository: TaskStateRepository) {
    fun execute(state: State) : Boolean {
       TODO("WRITE THE CODE HERE SHAHAD")
    }
}