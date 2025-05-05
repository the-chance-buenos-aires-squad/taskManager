package di

import GetAllTaskStatesCli
import TaskStateShowMenu
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import presentation.cli.GetAllAuditsCli
import presentation.cli.MainCli
import presentation.cli.auth.CreateUserCli
import presentation.cli.auth.LoginCli
import presentation.cli.dashBoard.AdminDashBoardCli
import presentation.cli.dashBoard.MateDashBoardCli

import presentation.UiController
import presentation.cli.TaskState.*
import presentation.cli.project.ProjectShowMenu
import presentation.cli.project.CreateProjectCli
import presentation.cli.project.UpdateProjectCli
import presentation.cli.project.DeleteProjectCli
import presentation.cli.project.GetAllProjectsCli
import presentation.cli.project.ProjectScreenController
import presentation.cli.task.CreateTaskCli
import java.util.*


val presentationModule = module {
    single { UiController(System.out, Scanner(System.`in`)) }

    single { ProjectShowMenu(uiController = get()) }
    single { MateDashBoardCli(uiController = get()) }
    single { TaskStateShowMenu(uiController = get()) }
    single { GetAllAuditsCli(getAllAuditUseCase = get(), uiController = get()) }
    single { EditTaskStateCli(editTaskStateUseCase = get(), uiController = get(), inputValidator = get(), getAllTaskStatesUseCase = get()) }
    single { GetAllTaskStatesCli(getAllTaskStatesUseCase = get(), uiController = get()) }
    single { CreateUserCli(uiController = get(), createUserUseCase = get()) }
    single { CreateProjectCli(createProjectUseCase = get(), uiController = get()) }
    single { UpdateProjectCli(getAllProjectsUseCase = get(), updateProjectUseCase = get(), uiController = get()) }
    single { DeleteProjectCli(getAllProjectsUseCase = get(), deleteProjectUseCase = get(), uiController = get()) }
    single { GetAllProjectsCli(getAllProjectsUseCase = get(), uiController = get()) }
    single { DeleteTaskStateCli( deleteTaskStateUseCase=get(), uiController = get(), getAllTaskStatesUseCase = get()) }
    single { TaskStateInputHandler(uiController = get(), inputValidator = get()) }
    single{ CreateTaskCli(
        createTaskUseCase = get(),
        getAllProjectsUseCase = get(),
        addAuditUseCase = get(),
        authRepository = get(),
        getAllStatesUseCase = get(),
        uiController = get(),) }

    single { LoginCli(
        uiController = get(),
        authenticationUseCase = get(),
        adminDashBoardCli = get(),
        mateDashBoardCli = get()
    ) }
    single { CreateTaskStateCli(
        createTaskStateUseCase = get(),
        existsTaskStateUseCase = get(),
        uiController = get(),
        inputValidator = get()
    ) }

    single { ProjectScreenController(get(),get(), get(),get(),get(),get()) }
    single { TaskStateCliController(get(),get(), get(),get(),get(),get()) }

    single { AdminDashBoardCli(
        uiController = get(),
        createUserCli = get(),
        projectScreenController = get(),
        taskStateCliController = get(),
        auditsCli = get()
    ) }


    singleOf(::MainCli)
}