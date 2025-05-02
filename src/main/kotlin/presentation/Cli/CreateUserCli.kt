package presentation.Cli

import domain.usecases.AuthenticationUseCase
import domain.usecases.CreateUserUseCase
import presentation.UiController

class CreateUserCli(
    private val uiController: UiController,
    private val createUserUseCase: CreateUserUseCase,
    private val authenticationUseCase: AuthenticationUseCase
) {

    fun start(){
        //display header
        //display username prompt "username:
        //val get username input
        //display password prompt "password:"
        // val get password input
        //display confirm password prompt "confirm password:"
        // val get confirm password input
        //display user Role options 1-ADMIN 2-MATE "choose user Role 1-ADMIN , 2-MATE : "
        // val userRole = userRoleInput?:""
        //createUserUseCase.addUser(username,password)
        //
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