package data.repositories.mappers.bson

import domain.entities.Project
import org.bson.Document
import java.time.LocalDateTime
import java.util.*

class ProjectBsonMapper:BsonMapper<Project> {
    override fun toDocument(entity: Project): Document {
        return Document()
            .append("id", entity.id.toString())
            .append("name", entity.name)
            .append("description", entity.description)
            .append("createdAt", entity.createdAt.toString())
    }

    override fun fromDocument(document: Document): Project {
        return Project(
            id = UUID.fromString(document.getString("id")),
            name = document.getString("name"),
            description = document.getString("description"),
            createdAt = LocalDateTime.parse(document.getString("createdAt"))
        )
    }
}