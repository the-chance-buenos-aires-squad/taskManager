package presentation.Cli.TaskState

import domain.Validator.TaskStateInputValidator
import domain.usecases.CreateTaskStateUseCase
import presentation.UiController

class CreateTaskStateCli(
    private val createTaskStateUseCase: CreateTaskStateUseCase,
    private val uiController: UiController,
    private val inputValidator: TaskStateInputValidator
) {
    fun createTaskState() {}
}