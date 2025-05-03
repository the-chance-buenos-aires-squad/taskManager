package di

import org.koin.dsl.module
import presentation.cli.GetAllAuditsCli
import presentation.cli.MainCli
import presentation.cli.auth.CreateUserCli
import presentation.cli.auth.LoginCli
import presentation.cli.dashBoard.AdminDashBoardCli
import presentation.cli.dashBoard.MateDashBoardCli
import presentation.cli.TaskState.CreateTaskStateCli
import presentation.cli.TaskState.EditTaskStateCli
import presentation.cli.TaskState.GetAllTaskStatesCli
import presentation.cli.TaskState.TaskStateCliController
import presentation.UiController
import presentation.cli.project.CreateProjectCli
import presentation.cli.project.DeleteProjectCli
import presentation.cli.project.ProjectShowMenu
import presentation.cli.project.UpdateProjectCli
import presentation.cli.project.ProjectScreenController

import java.util.*


val presentationModel = module {
    single { UiController(System.out, Scanner(System.`in`)) }

    single { MainCli(uiController = get(), loginCli = get()) }
    single { GetAllAuditsCli(getAllAuditUseCase = get(), uiController = get()) }
    single { EditTaskStateCli(editTaskStateUseCase = get(), uiController = get(), inputValidator = get()) }
    single { GetAllTaskStatesCli(getAllTaskStatesUseCase = get(), uiController = get()) }
    single { CreateUserCli(uiController = get(), createUserUseCase = get()) }
    single { CreateProjectCli(createProjectUseCase = get(), uiController = get()) }
    single { UpdateProjectCli(getAllProjectsUseCase = get(), updateProjectUseCase = get(), uiController = get()) }
    single { DeleteProjectCli(getAllProjectsUseCase = get(), deleteProjectUseCase = get(), uiController = get()) }
    single { ProjectShowMenu(uiController = get()) }
    single { AdminDashBoardCli(
        uiController = get(),
        createUserCli = get(),
        projectScreenController = get(),
        taskStateCliController = get(),
        auditsCli = get()
    ) }
    single { MateDashBoardCli(uiController = get()) }
    single { LoginCli(
        uiController = get(),
        authenticationUseCase = get(),
        adminDashBoardCli = get(),
        mateDashBoardCli = get()
    ) }
    single { ProjectScreenController(
        projectShowMenu = get(),
        createProjectCli = get(),
        updateProjectCli = get(),
        deleteProjectCli = get(),
        uiController = get()
    ) }
    single { CreateTaskStateCli(
        createTaskStateUseCase = get(),
        existsTaskStateUseCase = get(),
        uiController = get(),
        inputValidator = get()
    ) }
    single { TaskStateCliController(
        taskStateShowMenu = get(),
        createTaskStateCli = get(),
        editTaskStateCli = get(),
        deleteTaskStateCli = get(),
        getAllTaskStatesCli = get(),
        uiController = get()
    ) }
}