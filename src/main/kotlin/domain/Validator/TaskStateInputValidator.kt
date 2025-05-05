import domain.customeExceptions.InvalidTaskStateNameException
import domain.customeExceptions.InvalidProjectIdException

class TaskStateInputValidator {
    fun validate( name: String, projectId: String, isEdit: Boolean = false) {
        val prefix = if (isEdit) "New " else ""

//        if (id.isEmpty()) throw InvalidIdException(prefix)
        if (name.length < 2) throw InvalidTaskStateNameException(prefix)
        if (!projectId.matches(Regex("^P\\d{2,}$"))) {
            throw InvalidProjectIdException(prefix)
        }
    }
}