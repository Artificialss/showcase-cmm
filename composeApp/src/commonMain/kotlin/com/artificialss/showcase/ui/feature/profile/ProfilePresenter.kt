package com.artificialss.showcase.ui.feature.profile

import androidx.lifecycle.ViewModel
import com.artificialss.showcase.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface ProfilePresenter {
    val uiState: StateFlow<ProfileUiState>
    fun onEditProfile()
    fun onDismissEditDialog()
    fun onSaveProfile(name: String, role: String, email: String)
    fun onShowAvatarPicker()
    fun onDismissAvatarPicker()
    fun onAvatarSelected(url: String)
}

class ProfilePresenterImpl(
    private val userRepository: UserRepository,
) : ViewModel(), ProfilePresenter {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    override val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    override fun onEditProfile() {
        val current = _uiState.value as? ProfileUiState.Success ?: return
        _uiState.value = current.copy(isEditDialogVisible = true)
    }

    override fun onDismissEditDialog() {
        val current = _uiState.value as? ProfileUiState.Success ?: return
        _uiState.value = current.copy(isEditDialogVisible = false)
    }

    override fun onSaveProfile(name: String, role: String, email: String) {
        val current = _uiState.value as? ProfileUiState.Success ?: return
        val updated = userRepository.updateProfile(name, role, email)
        _uiState.value = current.copy(profile = updated, isEditDialogVisible = false)
    }

    override fun onShowAvatarPicker() {
        val current = _uiState.value as? ProfileUiState.Success ?: return
        _uiState.value = current.copy(isAvatarPickerVisible = true)
    }

    override fun onDismissAvatarPicker() {
        val current = _uiState.value as? ProfileUiState.Success ?: return
        _uiState.value = current.copy(isAvatarPickerVisible = false)
    }

    override fun onAvatarSelected(url: String) {
        val current = _uiState.value as? ProfileUiState.Success ?: return
        val updated = userRepository.updateAvatarUrl(url)
        _uiState.value = current.copy(profile = updated, isAvatarPickerVisible = false)
    }

    private fun loadProfile() {
        val profile = userRepository.getProfile()
        val activity = userRepository.getRecentActivity()
        _uiState.value = ProfileUiState.Success(
            profile = profile,
            recentActivity = activity,
        )
    }
}
