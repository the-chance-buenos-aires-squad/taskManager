package presentation.Cli


class Navigator(
    private val screenProvider: (String) -> Screen
) {
    private val screenHistory = mutableListOf<String>()


    fun navigate(route: String) {
        val screen = try {
            screenProvider(route)
        } catch (e: Exception) {
            throw RouteNotFoundException(route)
        }

        screenHistory.add(route)
        screen.render()
    }

    fun navigateBack() {
        val previousRoute = screenHistory.dropLast(1).lastOrNull()
            ?: throw NoPreviousScreenException()

        screenHistory.removeLast()
        navigate(previousRoute)
    }
}