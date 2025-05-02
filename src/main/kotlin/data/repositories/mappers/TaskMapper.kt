package data.repositories.mappers

import domain.entities.Task
import java.time.LocalDateTime
import java.util.*

class TaskMapper:Mapper<Task> {
    override fun mapEntityToRow(entity: Task): List<String> {
        return listOf(
            entity.id.toString(),
            entity.title.escape(),
            entity.description.escape(),
            entity.projectId.escape(),
            entity.stateId.escape(),
            entity.assignedTo.orEmpty().escape(),
            entity.createdBy.escape(),
            entity.createdAt.toString(),
            entity.updatedAt.toString()
        )
    }

    override fun mapRowToEntity(row: List<String>): Task {
        return Task(
                id = UUID.fromString(row[0]),
                title = row[1].unescape(),
                description = row[2].unescape(),
                projectId = row[3].unescape(),
                stateId = row[4].unescape(),
                assignedTo = row[5].ifBlank { null }?.unescape(),
                createdBy = row[6].unescape(),
                createdAt = LocalDateTime.parse(row[7]),
                updatedAt = LocalDateTime.parse(row[8])
            )
    }

    private fun String.escape(): String = replace(",", "%2C")
    private fun String.unescape(): String = replace("%2C", ",")
}