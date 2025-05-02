package data.mapper

import domain.entities.State
import domain.entities.StateIndices

object StateMapper {
    fun map(parts: List<String>): State? {
        return if (parts.size == 3) {
            State(
                id = parts[StateIndices.ID],
                name = parts[StateIndices.NAME],
                projectId = parts[StateIndices.PROJECT_ID]
            )
        } else {
            null
        }
    }
}