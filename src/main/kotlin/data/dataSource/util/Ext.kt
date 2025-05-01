package data.dataSource.util

import domain.entities.Project
import java.time.LocalDateTime

fun Project.toCsvRow(): List<String> {
    return listOf(id, name, description, createdAt.toString())
}

fun List<String>.toProject(): Project {
    return Project(
        id = this[0],
        name = this[1],
        description = this[2],
        createdAt = LocalDateTime.parse(this[3])
    )
}