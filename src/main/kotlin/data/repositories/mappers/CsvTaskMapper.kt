package data.repositories.mappers

import domain.entities.Task
import java.time.LocalDateTime
import java.util.*

class CsvTaskMapper : Mapper<Task, List<String>> {

    override fun fromType(type: Task): List<String> {
        return listOf(
            type.id.toString(),
            type.title,
            type.description,
            type.projectId.toString(),
            type.stateId.toString(),
            type.assignedTo.toString(),
            type.createdBy.toString(),
            type.createdAt.toString(),
            type.updatedAt.toString()
        )
    }

    override fun toType(row: List<String>): Task {
        return Task(
            id = UUID.fromString(row[0]),
            title = row[1],
            description = row[2],
            projectId = UUID.fromString(row[3]),
            stateId = UUID.fromString(row[4]),
            assignedTo = row[5].toUUIDOrNull(),
            createdBy = UUID.fromString(row[6]),
            createdAt = LocalDateTime.parse(row[7]),
            updatedAt = LocalDateTime.parse(row[8])
        )
    }


    fun String?.toUUIDOrNull(): UUID? = try {
        this?.takeUnless { it.isBlank() }?.let { UUID.fromString(it) }
    } catch (e: IllegalArgumentException) {
        null
    }
}
