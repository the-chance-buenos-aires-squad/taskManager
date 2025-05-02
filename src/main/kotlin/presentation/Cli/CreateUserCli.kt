package presentation.Cli

import domain.customeExceptions.CreateUserException
import domain.customeExceptions.UserAlreadyExistException
import domain.entities.UserRole
import domain.repositories.AuthRepository
import domain.usecases.CreateUserUseCase
import presentation.UiController

class CreateUserCli(
    private val uiController: UiController,
    private val createUserUseCase: CreateUserUseCase,
    private val authRepository: AuthRepository
) {

    fun start(){
        //display header
        uiController.printMessage(HEADER_MESSAGE)
        //display username prompt "username:
        uiController.printMessage(USERNAME_PROMPT_MESSAGE,true)
        //val get username input
        val usernameInput = uiController.readInput()
        //display password prompt "password:"
        uiController.printMessage(PASSWORD_PROMPT_MESSAGE,true)
        // val get password input
        val passwordInput = uiController.readInput()
        //display confirm password prompt "confirm password:"
        uiController.printMessage(CONFIRM_PASSWORD_PROMPT_MESSAGE,true)
        // val get confirm password input
        val confirmPasswordInput = uiController.readInput()
        //display user Role options 1-ADMIN 2-MATE "choose user Role 1-ADMIN , 2-MATE : "
        uiController.printMessage(USER_ROLE_PROMPT_MESSAGE,true)
        // val userRole = userRoleInput?:""
        val roleInput = uiController.readInput()
        val role: UserRole? = UserRole.entries.find { it.name ==  roleInput}
        //createUserUseCase.addUser(username,password)
        val userIsAdmin = authRepository.getCurrentUser() != null
        if(userIsAdmin){
            when(role){
                UserRole.MATE ->{
                    createMateUser(usernameInput, passwordInput, confirmPasswordInput)
                }
                UserRole.ADMIN->{
                    createAdminUser(usernameInput, passwordInput, confirmPasswordInput, role)
                }
                null->{
                    createMateUser(usernameInput, passwordInput, confirmPasswordInput)
                }
            }
        }else{
         uiController.printMessage("You are not allowed to create users.....\n ask the admin to create one")
        }
     }

    private fun createAdminUser(
        usernameInput: String,
        passwordInput: String,
        confirmPasswordInput: String,
        role: UserRole
    ) {
        tryCreateNewUser(
            addUserCal = {
                createUserUseCase.addUser(
                    usernameInput,
                    passwordInput,
                    confirmPasswordInput,
                    role
                )
            }
        )
    }

    private fun createMateUser(usernameInput: String, passwordInput: String, confirmPasswordInput: String) {
        tryCreateNewUser(
            addUserCal = {
                createUserUseCase.addUser(
                    usernameInput,
                    passwordInput,
                    confirmPasswordInput,
                )
            }
        )

    }


    private fun tryCreateNewUser(addUserCal: () -> Unit) {
        try {
            addUserCal()
        }catch (e: UserAlreadyExistException){
            uiController.printMessage(e.message?:"error")
        }catch (e: CreateUserException){
            uiController.printMessage(e.message?:"error")
        }
    }



    companion object{
        const val HEADER_MESSAGE  = "========================================\n" +
                                    "              Create New User           \n" +
                                    "========================================\n"

        const val USERNAME_PROMPT_MESSAGE = "username: "
        const val PASSWORD_PROMPT_MESSAGE = "password: "
        const val CONFIRM_PASSWORD_PROMPT_MESSAGE = "confirm password: "
        const val USER_ROLE_PROMPT_MESSAGE = "choose user Role 1-ADMIN , 2-MATE \n: "

    }

}