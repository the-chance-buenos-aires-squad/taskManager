package presentation.Cli

import domain.usecases.CreateProjectUseCase
import domain.usecases.DeleteProjectUseCase
import domain.usecases.EditProjectUseCase
import org.buinos.domain.entities.Project
import java.time.LocalDateTime

class ProjectScreen(
    private val createProjectUseCase: CreateProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val editProjectUseCase: EditProjectUseCase,
    private val inputHandler: InputHandler
) {
    fun show(){
        while (true){
            println(
                """
                === Project Management ===
                1. Create Project
                2. Update Project
                3. Delete Project
                5. Back
            """.trimIndent()
            )
            when(inputHandler.readInt("Choose an option: ")){
                1 -> create()
                2 -> update()
                3 -> delete()
                5 -> return
                else -> println("Invalid option.")
            }
        }
    }
    private fun create(){
        val id = inputHandler.readLine("Enter project ID:")
        val name = inputHandler.readLine("Enter project name:")
        val description = inputHandler.readLine("Enter project description:")
        val project = Project(
            id = id,
            name = name,
            description = description,
            createdBy = "admin",
            createdAt = LocalDateTime.now()
        )
        val result = createProjectUseCase.execute(project)
        println(if (result) "Project created." else "Failed to create project.")
    }
    private fun update(){
        val id = inputHandler.readLine("Enter project ID:")
        val name = inputHandler.readLine("Enter new name:")
        val description = inputHandler.readLine("Enter new description:")
        val updated = Project(
            id = id,
            name = name,
            description = description,
            createdBy = "admin",
            createdAt = LocalDateTime.now()
        )
        val result = editProjectUseCase.execute(updated)
        println(if (result) "Project updated." else "Failed to update project.")
    }
    private fun delete(){
        val projectId = inputHandler.readLine("Enter project ID To Delete:")
        val result=deleteProjectUseCase.execute(projectId)
        println(if (result) "Project deleted." else "Failed to delete project.")
    }
}
class InputHandler(private val reader: () -> String = { readln() }) {
    fun readLine(prompt: String): String {
        print("$prompt ")
        return reader()
    }

    fun readInt(prompt: String): Int {
        print("$prompt ")
        return reader().toIntOrNull() ?: -1
    }
}