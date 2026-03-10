package com.artificialss.showcase.ui.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artificialss.showcase.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface ProfilePresenter {
    val uiState: StateFlow<ProfileUiState>
    fun onEditProfile()
    fun onDismissEditDialog()
    fun onSaveProfile(name: String, role: String, email: String)
    fun onShowAvatarPicker()
    fun onDismissAvatarPicker()
    fun onAvatarSelected(url: String)
    fun onOpenChat()
    fun onDismissChat()
    fun onChatOptionSelected(option: String)
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

    override fun onOpenChat() {
        val current = _uiState.value as? ProfileUiState.Success ?: return
        _uiState.value = current.copy(
            isChatVisible = true,
            chatMessages = listOf(ChatMessage(id = "msg_0", text = GREETING_MESSAGE, isUser = false)),
            isChatLoading = false,
        )
    }

    override fun onDismissChat() {
        val current = _uiState.value as? ProfileUiState.Success ?: return
        _uiState.value = current.copy(isChatVisible = false)
    }

    override fun onChatOptionSelected(option: String) {
        val current = _uiState.value as? ProfileUiState.Success ?: return
        val nextId = current.chatMessages.size
        val userMessage = ChatMessage(id = "msg_$nextId", text = option, isUser = true)
        _uiState.value = current.copy(
            chatMessages = current.chatMessages + userMessage,
            isChatLoading = true,
        )
        viewModelScope.launch {
            delay(AI_RESPONSE_DELAY_MS)
            val response = AI_RESPONSES[option] ?: DEFAULT_RESPONSE
            val updated = _uiState.value as? ProfileUiState.Success ?: return@launch
            val botId = updated.chatMessages.size
            val botMessage = ChatMessage(id = "msg_$botId", text = response, isUser = false)
            _uiState.value = updated.copy(
                chatMessages = updated.chatMessages + botMessage,
                isChatLoading = false,
            )
        }
    }

    private fun loadProfile() {
        val profile = userRepository.getProfile()
        val activity = userRepository.getRecentActivity()
        _uiState.value = ProfileUiState.Success(
            profile = profile,
            recentActivity = activity,
        )
    }

    companion object {
        private const val AI_RESPONSE_DELAY_MS = 1200L
        private const val GREETING_MESSAGE =
            "Hi! I'm your AI assistant. How can I help you today?"
        private const val DEFAULT_RESPONSE =
            "I'm not sure how to help with that. Try one of the suggested options."

        private val AI_RESPONSES = mapOf(
            "What are my pending tasks?" to
                "You have 3 pending tasks:\n" +
                "\u2022 Review pull request #42 — due today\n" +
                "\u2022 Update project documentation — due tomorrow\n" +
                "\u2022 Deploy v2.1 to staging — due Friday",
            "Summarize my recent activity" to
                "This week you completed 5 code reviews, merged 2 pull requests, " +
                "and resolved 4 issues. Your productivity is up 12% compared to last week.",
            "Suggest next steps for my project" to
                "Based on your recent activity, I suggest:\n" +
                "\u2022 Finish the remaining UI tests for the dashboard\n" +
                "\u2022 Schedule a design review for the new gallery layout\n" +
                "\u2022 Update the API documentation before the sprint demo",
        )
    }
}
