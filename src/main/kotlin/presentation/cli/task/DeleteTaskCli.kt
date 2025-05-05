package presentation.cli.task
import domain.customeExceptions.NoTasksFoundException
import domain.customeExceptions.UserEnterInvalidValueException
import domain.usecases.taskState.GetAllTaskStatesUseCase
import presentation.UiController
import domain.usecases.task.DeleteTaskUseCase
import java.util.UUID


class DeleteTaskCli (
    private val getAllTaskStatesUseCase: GetAllTaskStatesUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val uiController: UiController

) {
    fun delete() {
        val tasks = getAllTaskStatesUseCase.execute()

        uiController.printMessage("Select a task to delete: ")
        tasks.forEachIndexed{ index, task ->
            uiController.printMessage("${index + 1}. ${task.name}")

        }
        if (tasks.isEmpty()) throw NoTasksFoundException()

        uiController.printMessage("Enter the task ID to delete: ")
        val indexInput = uiController.readInput().trim()
        val index = indexInput.toIntOrNull()?: throw UserEnterInvalidValueException ("Should enter valid value")
        if (index > tasks.size || index <=0) throw UserEnterInvalidValueException ("should enter found id")
        val selectedTask = tasks [index - 1]
        uiController.printMessage(" Are you sure you want to delete: ${selectedTask.name}? (yes/no)")
        val confirmation = uiController.readInput().trim().lowercase()

        if (confirmation =="yes") {
            val deleted = deleteTaskUseCase.deleteTask(UUID.fromString(selectedTask.id))
            uiController.printMessage(if (deleted)"Task deleted." else "Task not found.")
        } else {
            uiController.printMessage(" Task deletion canceled.")
        }

    }
}