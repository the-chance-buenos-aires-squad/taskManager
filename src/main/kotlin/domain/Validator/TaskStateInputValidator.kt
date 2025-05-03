import domain.customeExceptions.InvalidIdException
import domain.customeExceptions.InvalidNameException
import domain.customeExceptions.InvalidProjectIdException

class TaskStateInputValidator {
    fun validate(id: String, name: String, projectId: String, isEdit: Boolean = false) {
        val prefix = if (isEdit) "New " else ""

        if (id.isEmpty()) throw InvalidIdException(prefix)
        if (name.length < 2) throw InvalidNameException(prefix)
        if (!projectId.matches(Regex("^P\\d{2,}$"))) {
            throw InvalidProjectIdException(prefix)
        }
    }
}