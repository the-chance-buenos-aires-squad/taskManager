package di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.AuditDto
import di.MongoCollections.auditQualifier
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


    single (qualifier = auditQualifier){
        val mongoDb:MongoDatabase = get()
        mongoDb.getCollection<AuditDto>(MongoCollections.AUDITS_COLLECTION)
    }

}

object MongoCollections{
    const val USERS_COLLECTION = "users"
    const val TASKS_COLLECTION =  "tasks"
    const val TASK_STATES_COLLECTION =  "task_states"
    const val PROJECTS_COLLECTION =  "projects"
     const val AUDITS_COLLECTION =  "audits"
    val auditQualifier : Qualifier = named(AUDITS_COLLECTION)
}













