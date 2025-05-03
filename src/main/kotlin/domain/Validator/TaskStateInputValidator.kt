package domain.Validator

class TaskStateInputValidator {
    fun validate(id: String, name: String, projectId: String, isEdit: Boolean = false) {
        val prefix = if (isEdit) "New " else ""

        if (id.isEmpty()) throw IllegalArgumentException("${prefix}ID can't be empty")
        if (name.length < 2) throw IllegalArgumentException("${prefix}Name must be at least 2 letters")
        if (!projectId.matches(Regex("^P\\d{2,}$"))) {
            throw IllegalArgumentException("${prefix}Project ID must start with 'P' followed by at least two digits (e.g., P01, P123)")
        }
    }
}