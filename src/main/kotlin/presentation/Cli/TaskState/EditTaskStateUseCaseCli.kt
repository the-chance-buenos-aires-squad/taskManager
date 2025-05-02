package presentation.Cli.TaskState

import domain.entities.TaskState
import domain.usecases.EditTaskStateUseCase
import domain.usecases.GetAllTaskStatesUseCase

class EditTaskStateUseCaseCli(
    private val editStateUseCase: EditTaskStateUseCase,
    private val getAllStatesUseCase: GetAllTaskStatesUseCase,
) {

    fun start() {
        while (true) {
            println("\n1. Edit State")
            println("2. Exit")
            print("Choose an option: ")

            when (readlnOrNull()?.trim()) {
                "1" -> editState()
                "2" -> {
                    println("Exiting...")
                    return
                }

                else -> println("Invalid option. Please try again.")
            }
        }
    }

    private fun editState() {
        val allStates = getAllStatesUseCase.execute()
        if (allStates.isEmpty()) {
            println("No states available to edit.")
            return
        }

        println("Available States:")
        allStates.forEach { state ->
            println("ID: ${state.id}, Name: ${state.name}, Project ID: ${state.projectId}\n")
        }

        existingStateToEdit(allStates)
    }

    private fun existingStateToEdit(allStates: List<TaskState>) {
        print("Enter State ID to edit: ")
        val id = readlnOrNull()?.trim().orEmpty()
        val existingState = allStates.find { it.id == id }
        if (existingState == null) {
            println("State with ID $id not found.")
            return
        }

        print("Enter new State Name (current: ${existingState.name}): ")
        val newName = readlnOrNull()?.trim().orEmpty()

        print("Enter new project ID (current: ${existingState.projectId}): ")
        val newProjectId = readlnOrNull()?.trim().orEmpty()

        try {
            val updatedState = TaskState(
                id = existingState.id,
                name = newName,
                projectId = newProjectId
            )

            val result = editStateUseCase.execute(updatedState)

            if (result) {
                println("State updated successfully.")
            } else {
                println("Failed to update the state. Please check your input.")
            }
        } catch (e: IllegalArgumentException) {
            println("Invalid input: ${e.message}")
        }
    }

}

