package com.artificialss.showcase.ui.feature.profile

import com.artificialss.showcase.domain.model.ActivityItem
import com.artificialss.showcase.domain.model.UserProfile

data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
)

sealed class ProfileUiState {
    data object Loading : ProfileUiState()

    data class Success(
        val profile: UserProfile,
        val recentActivity: List<ActivityItem>,
        val isEditDialogVisible: Boolean = false,
        val isAvatarPickerVisible: Boolean = false,
        val isChatVisible: Boolean = false,
        val chatMessages: List<ChatMessage> = emptyList(),
        val isChatLoading: Boolean = false,
    ) : ProfileUiState()

    data class Error(val message: String) : ProfileUiState()
}
