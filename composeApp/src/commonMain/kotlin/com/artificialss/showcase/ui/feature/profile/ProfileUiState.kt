package com.artificialss.showcase.ui.feature.profile

import com.artificialss.showcase.domain.model.ActivityItem
import com.artificialss.showcase.domain.model.UserProfile

sealed class ProfileUiState {
    data object Loading : ProfileUiState()

    data class Success(
        val profile: UserProfile,
        val recentActivity: List<ActivityItem>,
        val isEditDialogVisible: Boolean = false,
        val isAvatarPickerVisible: Boolean = false,
    ) : ProfileUiState()

    data class Error(val message: String) : ProfileUiState()
}
