package presentation.Cli.auth

import org.junit.jupiter.api.Assertions.*
class CreateUserCliTest {
    /* private val uiController: UiController = mockk(relaxed = true)
     private val createUserUseCase: CreateUserUseCase = mockk(relaxed = true)
     private val authRepository: AuthRepositoryImpl = mockk()
     private lateinit var createUserCli: CreateUserCli

     @BeforeEach
     fun setUp(){
         createUserCli = CreateUserCli(uiController, createUserUseCase, authRepository)
     }

     @Test
     fun `should print header and username prompt before handle username input`(){
         every { uiController.readInput() } returnsMany listOf(
             "testUser", // username input
             "1234",     // password input
             "1234",     // confirm password
             "MATE"      // role input
         )
         every { authRepository.getCurrentUser() } returns dummyAdminUser

         createUserCli.start()

         verifySequence {
             uiController.printMessage(HEADER_MESSAGE)
             uiController.printMessage(USERNAME_PROMPT_MESSAGE, true)
             uiController.readInput()
             uiController.printMessage(PASSWORD_PROMPT_MESSAGE, true)
             uiController.readInput()
             uiController.printMessage(CONFIRM_PASSWORD_PROMPT_MESSAGE, true)
             uiController.readInput()
             uiController.printMessage(USER_ROLE_PROMPT_MESSAGE, true)
             uiController.readInput()
         }
     }



     @Test
     fun `call addUser after reading username, password, confirmPassword and Role and the user is ADMIN`(){
         //given
         every { uiController.readInput() } returnsMany listOf("username input","password input","confirmPassword input",UserRole.MATE.name)
         every { authRepository.getCurrentUser() } returns dummyAdminUser
         //when
         createUserCli.start()
         //then
         verify { createUserUseCase.addUser(any(),any(),any()) }
     }

     @Test
     fun `call addUser after reading username, password, confirmPassword and Role and the user is ADMIN creating admin user`(){
         //given
         every { uiController.readInput() } returnsMany listOf("username input","password input","confirmPassword input",UserRole.ADMIN.name)
         every { authRepository.getCurrentUser() } returns dummyAdminUser
         //when
         createUserCli.start()
         //then
         verify { createUserUseCase.addUser(any(),any(),any(),UserRole.ADMIN) }
     }


     @Test
     fun `call addUser after reading username, password, confirmPassword and Role and the user is ADMIN creating mate user with null Role input`(){
         //given
         every { uiController.readInput() } returnsMany listOf("username input","password input","confirmPassword input","something not Role")
         every { authRepository.getCurrentUser() } returns dummyAdminUser
         //when
         createUserCli.start()
         //then
         verify { createUserUseCase.addUser(any(),any(),any()) }
     }



     @Test
     fun `should display not allowed to create user message if the current user is not ADMIN or not logged in`(){
         //given
         every { uiController.readInput() } returnsMany listOf("username input","password input","confirmPassword input",UserRole.MATE.name)
         every { authRepository.getCurrentUser() } returns DummyUser.dummyUserTwo
         //when
         createUserCli.start()
         //then
         verify(exactly = 0) { createUserUseCase.addUser(any(),any(),any()) }
     }

     @Test
     fun `should print error message when user already exists`() {
         // Arrange
         every { uiController.readInput() } returnsMany listOf(
             "username input", "password input", "confirmPassword input", "ADMIN"
         )
         every { authRepository.getCurrentUser() } returns dummyAdminUser
         every { createUserUseCase.addUser(any(), any(), any(), any()) } throws UserAlreadyExistException("Username already exists!")

         // Act
         createUserCli.start()

         // Assert
         verify { uiController.printMessage("Username already exists!") }
     }

     @Test
     fun `should print error message when current user is null and CreateUserException is thrown`() {
         // Arrange
         every { uiController.readInput() } returnsMany listOf(
             "username input", "password input", "confirmPassword input", "ADMIN"
         )
         every { authRepository.getCurrentUser() } returns null // triggers CreateUserException
         // We don’t need to stub addUser here — it’s never called due to null user.

         // Act
         createUserCli.start()

         // Assert
         verify { uiController.printMessage(NOT_ALLOWED_MESSAGE,true) } // since e.message is null, fallback to "error"
     }*/
}