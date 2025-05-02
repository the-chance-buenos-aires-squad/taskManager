package presentation.Cli.TaskState

import presentation.UiController

class TaskStateShowMenu(private val uiController: UiController) {
    fun showMenu() {
        uiController.printMessage(
            """
            ┌────────────────────────────┐
            │   Task State Management    │
            ├────────────────────────────┤
            │ 1. Create Task State       │
            │ 2. Edit Task State         │
            │ 3. Delete Task State       │
            │ 4. View All Task States    │
            │ 5. Back to Main Menu       │
            └────────────────────────────┘
            """.trimIndent()
        )
    }
}