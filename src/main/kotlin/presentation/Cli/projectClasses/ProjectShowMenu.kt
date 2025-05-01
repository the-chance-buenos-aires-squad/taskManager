package presentation.Cli.projectClasses


import presentation.UiController

class ProjectShowMenu(private val uiController: UiController) {
    fun showMenu(): Int? {
        println(
            """
            === Project Management ===
            1. Create Project
            2. Update Project
            3. Delete Project
            4. Back
            """.trimIndent()
        )
        return uiController.readInput().toIntOrNull()
    }
}