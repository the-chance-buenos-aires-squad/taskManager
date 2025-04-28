package org.buinos.presentation

class LoginScreen(
    private val loginUseCase: LoginUseCase,
    ui: UiController
) : Screen(ui) {
    override fun render() {
        ui.printMessage("=== Login ===")
    }

    override fun handleInput(navigator: Navigator) {
        ui.printMessage("Username: ", isInline = true)
        val username = ui.readInput()
        ui.printMessage("Password: ", isInline = true)
        val password = ui.readInput()

        if (loginUseCase.execute(username, password)) {
            navigator.push(MainMenuScreen(ui, username))
        } else {
            ui.printMessage("Invalid credentials!")
        }
    }
}