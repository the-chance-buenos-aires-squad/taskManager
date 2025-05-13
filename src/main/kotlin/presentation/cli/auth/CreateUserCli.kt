package presentation.cli.auth

import data.exceptions.UserNameAlreadyExistException
import domain.usecases.CreateUserUseCase
import domain.validation.UserValidator
import presentation.UiController
import presentation.exceptions.InvalidConfirmPasswordException
import presentation.exceptions.InvalidLengthPasswordException
import presentation.exceptions.PasswordEmptyException
import presentation.exceptions.UserNameEmptyException


class CreateUserCli(
    private val uiController: UiController,
    private val createUserUseCase: CreateUserUseCase,
    private val userValidator: UserValidator
) {

    suspend fun start() {
        uiController.printMessage(HEADER_MESSAGE)
        try {
            uiController.printMessage(USERNAME_PROMPT_MESSAGE)
            val username = uiController.readInput().trim()
            userValidator.validateUsername(username)

            uiController.printMessage(PASSWORD_PROMPT_MESSAGE)
            val password = uiController.readInput().trim()

            userValidator.validatePassword(password)

            uiController.printMessage(CONFIRM_PASSWORD_PROMPT_MESSAGE)
            val confirmPassword = uiController.readInput().trim()
            userValidator.validatePasswordConfirmation(password, confirmPassword)

            val newUserMate = createUserUseCase.addUser(username, password)
            uiController.printMessage(SUCCESS_MESSAGE.format(newUserMate.username))
        } catch (e: UserNameEmptyException) {
            uiController.printMessage(e.localizedMessage)
            return
        } catch (e: PasswordEmptyException) {
            uiController.printMessage(e.localizedMessage)
            return
        } catch (e: InvalidLengthPasswordException) {
            uiController.printMessage(e.localizedMessage)
            return
        } catch (e: InvalidConfirmPasswordException) {
            uiController.printMessage(e.localizedMessage)
            return
        } catch (e: UserNameAlreadyExistException){
             uiController.printMessage(e.localizedMessage)
        }
        catch (e: Exception) {
            uiController.printMessage(ERROR_MESSAGE.format(e.message))
        }
    }

    companion object {
        const val HEADER_MESSAGE = "========================================\n" +
                "              Create New User           \n" +
                "========================================\n"
        const val USERNAME_PROMPT_MESSAGE = "username: "
        const val PASSWORD_PROMPT_MESSAGE = "password: "
        const val CONFIRM_PASSWORD_PROMPT_MESSAGE = "confirm password: "
        const val SUCCESS_MESSAGE = "add new user mate %s successfully"
        const val ERROR_MESSAGE = "Error: %s"
    }

}