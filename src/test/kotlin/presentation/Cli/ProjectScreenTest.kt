package presentation.Cli

import domain.usecases.CreateProjectUseCase
import domain.usecases.DeleteProjectUseCase
import domain.usecases.EditProjectUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProjectScreenTest{
  private lateinit var screen: ProjectScreen
  private val createProjectUseCase:CreateProjectUseCase= mockk(relaxed = true)
  private val editProjectUseCase:EditProjectUseCase= mockk(relaxed = true)
  private val deleteProjectUseCase:DeleteProjectUseCase= mockk(relaxed = true)
 private lateinit var inputHandler: InputHandler
  @BeforeEach
  fun setup(){
   inputHandler= mockk(relaxed = true)
   screen = ProjectScreen(createProjectUseCase, deleteProjectUseCase, editProjectUseCase,inputHandler)
  }
 @Test
 fun `should call execute function in create use case when call create function by choosing number one and success to create it`(){
  //given
  every { inputHandler.readInt("Choose an option: ") } returns 1 andThen 5
  every { createProjectUseCase.execute(any()) } returns true
  //when
  screen.show()
  //then
  verify { createProjectUseCase.execute(any()) }
 }
 @Test
 fun `should call execute function in create use case when call create function by choosing number one and failed to create it`(){
  //given
  every { inputHandler.readInt("Choose an option: ") } returns 1 andThen 5
  every { createProjectUseCase.execute(any()) } returns false
  //when
  screen.show()
  //then
  verify { createProjectUseCase.execute(any()) }
 }
 @Test
 fun `should call execute function in edit use case when call update function by choosing number tow and success to update it`(){
  //given
  every { inputHandler.readInt("Choose an option: ") } returns 2 andThen 5
  every { editProjectUseCase.execute(any()) } returns true
  //when
  screen.show()
  //then
  verify { editProjectUseCase.execute(any()) }
 }
 @Test
 fun `should call execute function in edit use case when call update function by choosing number tow and failed to update it`(){
  //given
  every { inputHandler.readInt("Choose an option: ") } returns 2 andThen 5
  every { editProjectUseCase.execute(any()) } returns false
  //when
  screen.show()
  //then
  verify { editProjectUseCase.execute(any()) }
 }
 @Test
 fun `should call execute function in delete use case when call delete function by choosing number 3 and success to delete it`(){
  //given
  every { inputHandler.readInt("Choose an option: ") } returns 3 andThen 5
  every { deleteProjectUseCase.execute(any()) } returns true
  //when
  screen.show()
  //then
  verify { deleteProjectUseCase.execute(any()) }
 }
 @Test
 fun `should call execute function in delete use case when call delete function by choosing number 3 and failed to delete it`(){
  //given
  every { inputHandler.readInt("Choose an option: ") } returns 3 andThen 5
  every { deleteProjectUseCase.execute(any()) } returns false
  //when
  screen.show()
  //then
  verify { deleteProjectUseCase.execute(any()) }
 }

 }