package di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.ProjectDto
import di.MongoCollections.projectCollectionQualifier
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val mongoModule = module {
    single<MongoClient> {
        MongoClient.create("mongodb+srv://hamadarayyan056:PZrnkQVEQ4awiwY9@cluster0.cgucsr8.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")
    }

    single<MongoDatabase> {
        get<MongoClient>().getDatabase("planmate")
    }

    single(qualifier = projectCollectionQualifier) {
        val mangoDb: MongoDatabase = get()
        mangoDb.getCollection<ProjectDto>(MongoCollections.PROJECTS_COLLECTION)
    }

}

object MongoCollections {
    const val USERS_COLLECTION = "users"
    const val TASKS_COLLECTION = "tasks"
    const val TASK_STATES_COLLECTION = "task_states"
    const val PROJECTS_COLLECTION = "projects"
    val projectCollectionQualifier : Qualifier = named(PROJECTS_COLLECTION)
    const val AUDITS_COLLECTION = "audits"
}