package data.dataSource.util

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import java.io.File

class CsvHandler(
    private val csvWriter: CsvWriter,
    private val csvReader: CsvReader
) {

    fun write(row: List<String>, file: File, append: Boolean = true) {
        csvWriter.open(file, append) {
            writeRow(row)
        }
    }


    fun read(file: File): List<List<String>> {
        return csvReader.readAll(file)
    }

    fun writeHeaderIfNotExist(header: List<String>, file: File) {
        write(header, file, false)
    }
}

