package presentation.cli.task

import domain.customeExceptions.NoProjectsFoundException
import domain.usecases.project.GetAllProjectsUseCase
import presentation.UiController

class ViewSwimlanesCLI(private val uiController: UiController, private val getProjectsUseCase: GetAllProjectsUseCase) {

    fun start() {


    }


    companion object {
        const val HEADER_MESSAGE = "========================================\n" +
                "              view SwimLanes          \n" +
                "========================================\n"

    }
}