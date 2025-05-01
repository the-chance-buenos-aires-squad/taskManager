package presentation.Cli.projectClasses

import domain.usecases.CreateProjectUseCase
import domain.usecases.DeleteProjectUseCase
import domain.usecases.EditProjectUseCase
import org.buinos.domain.entities.Project
import presentation.Cli.InputHandler
import java.time.LocalDateTime

class ProjectActionHandler(
    private val createProjectUseCase: CreateProjectUseCase,
    private val editProjectUseCase: EditProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val inputHandler: InputHandler
) {
    fun create() {
        val id = inputHandler.readLine("Enter project ID:")
        val name = inputHandler.readLine("Enter project name:")
        val description = inputHandler.readLine("Enter project description:")
        val project = Project(
            id = id,
            name = name,
            description = description,
            createdAt = LocalDateTime.now()
        )
        val result = createProjectUseCase.execute(project)
        println(if (result) "Project created." else "Failed to create project.")
    }

    fun edit() {
        val id = inputHandler.readLine("Enter project ID:")
        val name = inputHandler.readLine("Enter new name:")
        val description = inputHandler.readLine("Enter new description:")
        val updated = Project(
            id = id,
            name = name,
            description = description,
            createdAt = LocalDateTime.now()
        )
        val result = editProjectUseCase.execute(updated)
        println(if (result) "Project updated." else "Failed to update project.")
    }

    fun delete() {
        val projectId = inputHandler.readLine("Enter project ID To Delete:")
        val result = deleteProjectUseCase.execute(projectId)
        println(if (result) "Project deleted." else "Failed to delete project.")
    }
}