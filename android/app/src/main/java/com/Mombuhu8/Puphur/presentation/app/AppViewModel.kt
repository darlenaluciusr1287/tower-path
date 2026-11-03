package com.Mombuhu8.Puphur.presentation.app

import androidx.lifecycle.ViewModel
import com.Mombuhu8.Puphur.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val backStack = ArrayDeque<Screen>()

    fun openMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
    }

    fun navigate(target: Screen) {
        val current = _screen.value
        if (current !is Screen.Splash) {
            backStack.addLast(current)
        }
        _screen.value = target
    }

    fun navigateReplacing(target: Screen) {
        _screen.value = target
    }

    fun back(): Boolean {
        val previous = backStack.removeLastOrNull() ?: return false
        _screen.value = previous
        return true
    }
}
