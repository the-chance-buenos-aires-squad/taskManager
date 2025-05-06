package di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import presentation.UiController
import presentation.cli.GetAllAuditsCli
import presentation.cli.MainCli
import presentation.cli.TaskState.*
import presentation.cli.auth.CreateUserCli
import presentation.cli.auth.LoginCli
import presentation.cli.dashBoard.AdminDashBoardCli
import presentation.cli.dashBoard.MateDashBoardCli

import presentation.cli.TaskState.*
import presentation.cli.helper.ProjectCliHelper
import presentation.cli.project.ProjectShowMenu
import presentation.cli.project.CreateProjectCli
import presentation.cli.project.UpdateProjectCli
import presentation.cli.project.DeleteProjectCli
import presentation.cli.project.GetAllProjectsCli
import presentation.cli.project.ProjectScreenController
import presentation.cli.task.CreateTaskCli
import presentation.cli.task.ViewSwimlanesCLI
import java.util.*


val presentationModule = module {
    single { UiController(System.out, Scanner(System.`in`)) }

    single { ProjectShowMenu(uiController = get()) }
    single { ProjectCliHelper(getProjectsUseCase = get(), uiController = get()) }
    single { MateDashBoardCli(uiController = get(), viewSwimlanesCLI = get()) }
    single { GetAllAuditsCli(getAllAuditUseCase = get(), uiController = get()) }
    single { EditTaskStateCli(editTaskStateUseCase = get(), uiController = get(), inputValidator = get()) }
    single { GetAllTaskStatesCli(getAllTaskStatesUseCase = get(), uiController = get()) }
    single { CreateUserCli(uiController = get(), createUserUseCase = get()) }
    single { CreateProjectCli(createProjectUseCase = get(), uiController = get()) }
    single { UpdateProjectCli(getAllProjectsUseCase = get(), updateProjectUseCase = get(), uiController = get()) }
    single { DeleteProjectCli(getAllProjectsUseCase = get(), deleteProjectUseCase = get(), uiController = get()) }
    single { GetAllProjectsCli(getAllProjectsUseCase = get(), uiController = get()) }
    single { DeleteTaskStateCli(deleteTaskStateUseCase = get(), uiController = get()) }
    single { TaskStateInputHandler(uiController = get(), inputValidator = get()) }
    single {
        ViewSwimlanesCLI(
            uiController = get(),
            projectCliHelper = get(),
            getTasksGroupedByStateUseCase = get(),
            createTaskCli = get(),

        )
    }
    single {
        AdminDashBoardCli(
            uiController = get(),
            createUserCli = get(),
            projectScreenController = get(),
            taskStateCliController = get(),
            auditsCli = get()
        )
    }
    single {
        CreateTaskCli(
            createTaskUseCase = get(),
            getAllStatesUseCase = get(),
            userRepository = get(),
            uiController = get(),
        )
    }

    single {
        LoginCli(
            uiController = get(),
            authenticationUseCase = get(),
            adminDashBoardCli = get(),
            mateDashBoardCli = get()
        )
    }
    single {
        CreateTaskStateCli(
            createTaskStateUseCase = get(),
            existsTaskStateUseCase = get(),
            uiController = get(),
            inputValidator = get()
        )
    }

    single {
        ProjectScreenController(
            projectShowMenu = get(),
            createProjectCli = get(),
            updateProjectCli = get(),
            deleteProjectCli = get(),
            getAllProjectsCli = get(),
            uiController = get()
        )
    }
    single {
        TaskStateCliController(
            projectCliHelper = get(),
            createTaskStateCli = get(),
            editTaskStateCli = get(),
            deleteTaskStateCli = get(),
            getAllTaskStatesCli = get(),
            uiController = get()
        )
    }



    singleOf(::MainCli)
}