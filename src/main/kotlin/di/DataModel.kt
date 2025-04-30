package di


//import org.example.data.CsvFileReader
//import org.example.data.CsvRecipesRepository
//import org.example.data.MemoryDataSource
//import org.example.data.RecipeParser
//import org.example.logic.RecipesRepository
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import data.dataSource.UserCsvDataSource
import data.util.CsvHandler
import org.koin.dsl.module
import java.io.File

//
val dataModule = module {
    single { CsvWriter() }
    single { CsvReader() }
    single { CsvHandler(get(), get()) }


    single { UserCsvDataSource(Files.UserFile,get()) }


}

object Files {
    val UserFile = File("src/main/kotlin/data/resource/users_file.csv")

}