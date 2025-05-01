package data.dataSource.util

import presentation.UiController

class CheckValidationInputs() {
    fun checkValidation(string:String,uiController:UiController):String{
        var validId = string
        if (validId.isEmpty()) {
            uiController.printMessage("Enter Valid project ID:")
            validId = uiController.readInput().trim()
        }
        return validId
    }
}