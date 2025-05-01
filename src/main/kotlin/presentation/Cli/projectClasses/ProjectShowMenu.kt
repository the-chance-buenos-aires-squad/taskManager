package presentation.Cli.projectClasses

import presentation.Cli.InputHandler

class ProjectShowMenu(private val inputHandler: InputHandler) {
    fun showMenu(): Int {
        println(
            """
            === Project Management ===
            1. Create Project
            2. Update Project
            3. Delete Project
            4. Back
            """.trimIndent()
        )
        return inputHandler.readInt("Choose an option: ")
    }
}