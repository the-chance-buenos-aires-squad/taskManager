package presentation.Cli.projectClasses

import data.dataSource.util.CheckValidationInputs
import domain.usecases.CreateProjectUseCase
import domain.usecases.DeleteProjectUseCase
import domain.usecases.UpdateProjectUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController

class ProjectActionHandlerTest {
    private lateinit var projectActionHandler: ProjectActionHandler
    private val createProjectUseCase: CreateProjectUseCase = mockk(relaxed = true)
    private val updateProjectUseCase: UpdateProjectUseCase = mockk(relaxed = true)
    private val deleteProjectUseCase: DeleteProjectUseCase = mockk(relaxed = true)
    private val uiController: UiController = mockk(relaxed = true)
    private val checkValidationInputs: CheckValidationInputs = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        projectActionHandler =
            ProjectActionHandler(
                createProjectUseCase,
                updateProjectUseCase,
                deleteProjectUseCase,
                uiController,
                checkValidationInputs
            )
    }

    @Test
    fun `should call execute function in create use case when I call create function and success to create project`() {
        every { createProjectUseCase.execute(any()) } returns true

        projectActionHandler.create()

        verify { createProjectUseCase.execute(any()) }
    }

    @Test
    fun `should call execute function in create use case when I call create function but failed to create project`() {
        every { createProjectUseCase.execute(any()) } returns false

        projectActionHandler.create()

        verify { createProjectUseCase.execute(any()) }
    }

    @Test
    fun `should call execute function in edit use case when I call edit function and success to edit project`() {
        every { updateProjectUseCase.execute(any()) } returns true

        projectActionHandler.edit()

        verify { updateProjectUseCase.execute(any()) }
    }

    @Test
    fun `should call execute function in edit use case when I call edit function but failed to edit project`() {
        every { updateProjectUseCase.execute(any()) } returns false

        projectActionHandler.edit()

        verify { updateProjectUseCase.execute(any()) }
    }

    @Test
    fun `should call execute function in delete use case when I call delete function and success to delete project`() {
        every { deleteProjectUseCase.execute(any()) } returns true

        projectActionHandler.delete()

        verify { deleteProjectUseCase.execute(any()) }
    }

    @Test
    fun `should call execute function in delete use case when I call delete function but failed to delete project`() {
        every { deleteProjectUseCase.execute(any()) } returns false

        projectActionHandler.delete()

        verify { deleteProjectUseCase.execute(any()) }
    }
}