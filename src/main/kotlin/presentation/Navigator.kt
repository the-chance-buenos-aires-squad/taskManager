package org.buinos.presentation

import java.util.ArrayDeque

class Navigator {
    private val screenStack = ArrayDeque<Screen>()

    fun getStackScreenCounts() = screenStack.size
    fun getTopScreen(): Screen = screenStack.peek()

    fun start() {
        while (screenStack.isNotEmpty()) {
            val currentScreen = screenStack.peek()
            currentScreen.render()
            currentScreen.handleInput(this)
        }
    }

    fun push(screen: Screen) = screenStack.push(screen)
    fun pop() = screenStack.pop()
}