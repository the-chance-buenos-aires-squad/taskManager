package di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.ProjectDto
import di.MongoCollections.projectCollectionQualifier
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import data.dto.AuditDto
import data.dto.UserDto
import di.MongoCollections.auditQualifier
import di.MongoCollections.userQualifier
import org.koin.dsl.module

val mongoModule = module {
    single<MongoClient> {
        MongoClient.create("mongodb+srv://hamadarayyan056:PZrnkQVEQ4awiwY9@cluster0.cgucsr8.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")
    }

    single<MongoDatabase> {
        get<MongoClient>().getDatabase("planmate")
    }
    
    single(qualifier = auditQualifier) {
        val mongoDb: MongoDatabase = get()
        mongoDb.getCollection<AuditDto>(MongoCollections.AUDITS_COLLECTION)
    }

    single(qualifier = projectCollectionQualifier) {
        val mongoDb: MongoDatabase = get()
        mongoDb.getCollection<ProjectDto>(MongoCollections.PROJECTS_COLLECTION)
    }

    single (qualifier = taskStateCollectionQualifier){
        val mongoDb: MongoDatabase = get()
        mongoDb.getCollection<TaskStateDto>(MongoCollections.TASK_STATES_COLLECTION)
    }

    single(qualifier = userQualifier) {
        val mongoDB: MongoDatabase = get()
        mongoDB.getCollection<UserDto>(MongoCollections.USERS_COLLECTION)
    }

}

object MongoCollections {
    const val USERS_COLLECTION = "users"
    const val TASKS_COLLECTION = "tasks"
    const val AUDITS_COLLECTION = "audits"
    val auditQualifier: Qualifier = named(AUDITS_COLLECTION)
    const val TASK_STATES_COLLECTION = "task_states"
    val taskStateCollectionQualifier : Qualifier = named(TASK_STATES_COLLECTION)
    const val PROJECTS_COLLECTION = "projects"
    val projectCollectionQualifier: Qualifier = named(PROJECTS_COLLECTION)
    val userQualifier: Qualifier = named(USERS_COLLECTION)
}













