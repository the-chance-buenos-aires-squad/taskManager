package presentation.Cli


class RouteNotFoundException(route: String) :
    RuntimeException("Route not found: $route")

class NoPreviousScreenException :
    RuntimeException("No previous screen in history.")