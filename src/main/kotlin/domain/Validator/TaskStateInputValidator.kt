import domain.customeExceptions.InvalidIdException
import domain.customeExceptions.InvalidNameException
import domain.customeExceptions.InvalidProjectIdException

class TaskStateInputValidator {
    fun validate(name: String, projectId: String, isEdit: Boolean = false) {
        val prefix = if (isEdit) "New " else ""

        if (name.length < 2) throw InvalidNameException(prefix)
        if (projectId.isEmpty()) throw InvalidProjectIdException(prefix)

    }
}