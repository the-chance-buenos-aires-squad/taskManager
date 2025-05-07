package data.repositories.mappers

import domain.entities.Task
import java.time.LocalDateTime
import java.util.*

class CsvTaskMapper : Mapper<Task,List<String>> {

    override fun fromEntity(entity: Task): List<String> {
        return listOf(entity.id.toString(),
            entity.title,
            entity.description,
            entity.projectId.toString(),
            entity.stateId.toString(),
            entity.assignedTo.toString(),
            entity.createdBy.toString(),
            entity.createdAt.toString(),
            entity.updatedAt.toString()
        )
    }

    override fun toEntity(row: List<String>): Task {
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
