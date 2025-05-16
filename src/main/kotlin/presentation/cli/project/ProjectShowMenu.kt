package presentation.cli.project


import presentation.UiController

class ProjectShowMenu(private val uiController: UiController) {
    fun showMenu() {
        uiController.printMessage(HEADER_PROJECT_MESSAGE)
    }

    companion object {
        private const val HEADER_PROJECT_MESSAGE =
            " === Project Management ===\n" +
                    " 1. Create Project\n" +
                    " 2. Update Project\n" +
                    " 3. Delete Project\n" +
                    " 4. Get All Projects\n" +
                    " 5. Back\n" +
                    " Choose an option (1-5):"
    }
}