package com.artificialss.showcase.ui.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface LoginPresenter {
    val uiState: StateFlow<LoginUiState>
    fun onLogin(email: String, password: String)
}

class LoginPresenterImpl : ViewModel(), LoginPresenter {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    override val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    override fun onLogin(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Please enter both email and password")
            return
        }
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            delay(LOGIN_DELAY_MS)
            _uiState.value = LoginUiState.Success
        }
    }

    companion object {
        private const val LOGIN_DELAY_MS = 1500L
    }
}
