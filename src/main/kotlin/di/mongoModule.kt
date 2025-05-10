package di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.*
import di.MongoCollections.auditCollectionQualifier
import di.MongoCollections.projectCollectionQualifier
import di.MongoCollections.taskStateCollectionQualifier
import di.MongoCollections.tasksCollectionQualifier
import di.MongoCollections.userCollectionQualifier
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mongoModule = module {
    single<MongoClient> {
        MongoClient.create("mongodb+srv://hamadarayyan056:PZrnkQVEQ4awiwY9@cluster0.cgucsr8.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")
    }

    single<MongoDatabase> {
        get<MongoClient>().getDatabase("planmate")
    }

    single(qualifier = auditCollectionQualifier) {
        val mongoDb: MongoDatabase = get()
        mongoDb.getCollection<AuditDto>(MongoCollections.AUDITS_COLLECTION)
    }

    single(qualifier = projectCollectionQualifier) {
        val mongoDb: MongoDatabase = get()
        mongoDb.getCollection<ProjectDto>(MongoCollections.PROJECTS_COLLECTION)
    }

    single(qualifier = taskStateCollectionQualifier) {
        val mongoDb: MongoDatabase = get()
        mongoDb.getCollection<TaskStateDto>(MongoCollections.TASK_STATES_COLLECTION)
    }

    single(qualifier = userCollectionQualifier) {
        val mongoDB: MongoDatabase = get()
        mongoDB.getCollection<UserDto>(MongoCollections.USERS_COLLECTION)
    }

    single(qualifier = tasksCollectionQualifier) {
        val mongoDb: MongoDatabase = get()
        mongoDb.getCollection<TaskDto>(MongoCollections.TASKS_COLLECTION)
    }
}

object MongoCollections {
    const val USERS_COLLECTION = "users"
    val userCollectionQualifier: Qualifier = named(USERS_COLLECTION)
    const val TASKS_COLLECTION = "tasks"
    val tasksCollectionQualifier = named(TASKS_COLLECTION)
    const val AUDITS_COLLECTION = "audits"
    val auditCollectionQualifier: Qualifier = named(AUDITS_COLLECTION)
    const val TASK_STATES_COLLECTION = "task_states"
    val taskStateCollectionQualifier: Qualifier = named(TASK_STATES_COLLECTION)
    const val PROJECTS_COLLECTION = "projects"
    val projectCollectionQualifier: Qualifier = named(PROJECTS_COLLECTION)
}













