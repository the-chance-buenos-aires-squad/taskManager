package org.buinos.presentation

abstract class Screen(
    protected val ui: UiController
) {
    abstract fun render()
    abstract fun handleInput(navigator: Navigator)
}