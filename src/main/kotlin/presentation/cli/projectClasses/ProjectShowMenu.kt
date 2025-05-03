package presentation.cli.projectClasses


import presentation.UiController

class ProjectShowMenu(private val uiController: UiController) {
    fun showMenu() {
        uiController.printMessage(
            """
            === Project Management ===
            1. Create Project
            2. Update Project
            3. Delete Project
            4. Get All Projects
            5. Back
            Choose an option (1-5):
            """.trimIndent()
        )
    }
}