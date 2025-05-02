package domain.Validator

class TaskStateInputValidator {
    fun validate(id: String, name: String, projectId: String) {
        if (id.isEmpty()) throw IllegalArgumentException("ID can't be empty")
        if (name.length <= 2) throw IllegalArgumentException("Name must be at least 2 letters")
        if (!projectId.matches(Regex("^P\\d+"))) throw IllegalArgumentException("Project ID must start with 'P' followed by at least two digits (e.g., P01, P123)")
    }
}