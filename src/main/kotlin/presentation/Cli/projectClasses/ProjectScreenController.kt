package presentation.Cli.projectClasses

class ProjectScreenController(
    private val projectShowMenu: ProjectShowMenu,
    private val projectActionHandler: ProjectActionHandler
) {
    fun show() {
        while (true) {
            when (projectShowMenu.showMenu()) {
                1 -> projectActionHandler.create()
                2 -> projectActionHandler.edit()
                3 -> projectActionHandler.delete()
                4 -> return
                else -> println("Invalid option.")
            }
        }
    }
}