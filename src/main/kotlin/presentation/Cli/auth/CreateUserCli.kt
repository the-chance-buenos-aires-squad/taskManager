package presentation.Cli.auth


class CreateUserCli(

) {

    fun start() {}

    companion object {
        const val HEADER_MESSAGE = "========================================\n" +
                "              Create New User           \n" +
                "========================================\n"

        const val USERNAME_PROMPT_MESSAGE = "username: "
        const val PASSWORD_PROMPT_MESSAGE = "password: "
        const val CONFIRM_PASSWORD_PROMPT_MESSAGE = "confirm password: "
        const val USER_ROLE_PROMPT_MESSAGE = "choose user Role 1-ADMIN , 2-MATE \n: "

        const val NOT_ALLOWED_MESSAGE = "You are not allowed to create users.....\n ask the admin to create one"
    }

}