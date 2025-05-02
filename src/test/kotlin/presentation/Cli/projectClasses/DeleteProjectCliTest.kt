package presentation.Cli.projectClasses

import domain.usecases.project.CreateProjectUseCase
import domain.usecases.project.DeleteProjectUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.UiController

class DeleteProjectCliTest{
   private val deleteProjectUseCase:DeleteProjectUseCase = mockk(relaxed = true)
   private val uiController: UiController = mockk(relaxed = true)
   private lateinit var deleteProjectCli: DeleteProjectCli

   @BeforeEach
   fun setup(){
    deleteProjectCli=DeleteProjectCli(deleteProjectUseCase,uiController)
   }

   @Test
   fun `should call execute function in create use case when I call create function and success to create project`() {
    every { deleteProjectUseCase.execute(any()) } returns true

    deleteProjectCli.delete()

    verify { deleteProjectUseCase.execute(any()) }
   }

   @Test
   fun `should call execute function in create use case when I call create function but failed to create project`() {
    every { deleteProjectUseCase.execute(any()) } returns false

    deleteProjectCli.delete()

    verify { deleteProjectUseCase.execute(any()) }
   }
  }