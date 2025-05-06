package data.repositories.mappers.bson

import org.bson.Document

interface BsonMapper<entity> {
    fun toDocument(entity: entity): Document
    fun fromDocument(document: Document): entity
}