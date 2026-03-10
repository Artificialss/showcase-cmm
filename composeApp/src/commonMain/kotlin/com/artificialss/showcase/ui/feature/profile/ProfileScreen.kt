package com.artificialss.showcase.ui.feature.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.artificialss.showcase.domain.model.ActivityItem
import com.artificialss.showcase.domain.model.UserProfile
import com.artificialss.showcase.ui.components.ErrorMessage
import com.artificialss.showcase.ui.components.LoadingIndicator

@Composable
fun ProfileScreen(
    presenter: ProfilePresenter,
    modifier: Modifier = Modifier,
) {
    val state by presenter.uiState.collectAsStateWithLifecycle()

    when (val current = state) {
        is ProfileUiState.Loading -> LoadingIndicator(modifier = modifier)
        is ProfileUiState.Error -> ErrorMessage(message = current.message, modifier = modifier)
        is ProfileUiState.Success -> {
            Box(modifier = modifier.fillMaxSize()) {
                ProfileContent(
                    profile = current.profile,
                    activity = current.recentActivity,
                    onEditClick = { presenter.onEditProfile() },
                    onAvatarClick = { presenter.onShowAvatarPicker() },
                    onAskAiClick = { presenter.onOpenChat() },
                )
                ChatBottomSheet(
                    visible = current.isChatVisible,
                    messages = current.chatMessages,
                    isLoading = current.isChatLoading,
                    onDismiss = { presenter.onDismissChat() },
                    onOptionSelected = { presenter.onChatOptionSelected(it) },
                )
            }
            if (current.isEditDialogVisible) {
                EditProfileDialog(
                    profile = current.profile,
                    onDismiss = { presenter.onDismissEditDialog() },
                    onSave = { name, role, email -> presenter.onSaveProfile(name, role, email) },
                )
            }
            if (current.isAvatarPickerVisible) {
                AvatarPickerDialog(
                    onDismiss = { presenter.onDismissAvatarPicker() },
                    onAvatarSelected = { presenter.onAvatarSelected(it) },
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    profile: UserProfile,
    activity: List<ActivityItem>,
    onEditClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onAskAiClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = CONTENT_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ITEM_SPACING),
    ) {
        item { Spacer(modifier = Modifier.height(SECTION_SPACING)) }
        item { AvatarSection(profile = profile, onAvatarClick = onAvatarClick) }
        item { StatsRow(profile) }
        item { EditButton(onClick = onEditClick) }
        item { HorizontalDivider() }
        item { RecentActivityHeader(onAskAiClick = onAskAiClick) }
        items(items = activity, key = { it.id }) { item ->
            ActivityRow(item)
        }
        item { Spacer(modifier = Modifier.height(CONTENT_PADDING)) }
    }
}

@Composable
private fun RecentActivityHeader(onAskAiClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = RECENT_ACTIVITY_TITLE,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        AssistChip(
            onClick = onAskAiClick,
            label = { Text(ASK_AI_LABEL, style = MaterialTheme.typography.labelSmall) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = null,
                    modifier = Modifier.size(CHIP_ICON_SIZE),
                )
            },
        )
    }
}

// region — Chat Bottom Sheet

@Composable
private fun ChatBottomSheet(
    visible: Boolean,
    messages: List<ChatMessage>,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
        modifier = Modifier.fillMaxSize(),
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = SCRIM_ALPHA))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.BottomCenter,
        ) {
            val sheetHeight = minOf(maxHeight * SHEET_HEIGHT_FRACTION, SHEET_MAX_HEIGHT)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sheetHeight)
                    .clickable(enabled = false, onClick = {}),
                shape = RoundedCornerShape(topStart = SHEET_CORNER, topEnd = SHEET_CORNER),
                tonalElevation = SHEET_ELEVATION,
            ) {
                ChatSheetContent(
                    messages = messages,
                    isLoading = isLoading,
                    onDismiss = onDismiss,
                    onOptionSelected = onOptionSelected,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChatSheetContent(
    messages: List<ChatMessage>,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit,
) {
    val listState = rememberLazyListState()
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SHEET_PADDING),
    ) {
        ChatSheetHeader(onDismiss = onDismiss)
        Spacer(modifier = Modifier.height(SPACING_SM))

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(SPACING_SM),
        ) {
            items(items = messages, key = { it.id }) { message ->
                ChatBubble(message = message)
            }
            if (isLoading) {
                item { TypingIndicator() }
            }
        }

        Spacer(modifier = Modifier.height(SPACING_SM))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(SPACING_SM))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(SPACING_XS),
            verticalArrangement = Arrangement.spacedBy(SPACING_XS),
        ) {
            CHAT_OPTIONS.forEach { option ->
                AssistChip(
                    onClick = { onOptionSelected(option) },
                    label = { Text(option, style = MaterialTheme.typography.labelSmall) },
                    enabled = !isLoading,
                )
            }
        }
    }
}

@Composable
private fun ChatSheetHeader(onDismiss: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Chat,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(CHAT_HEADER_ICON_SIZE),
            )
            Spacer(modifier = Modifier.width(SPACING_SM))
            Text(
                text = AI_ASSISTANT_TITLE,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
        }
        IconButton(onClick = onDismiss, modifier = Modifier.size(CLOSE_BUTTON_SIZE)) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = CLOSE_DESC,
                modifier = Modifier.size(CLOSE_ICON_SIZE),
            )
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val bgColor = if (message.isUser) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = if (message.isUser) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val shape = if (message.isUser) {
        RoundedCornerShape(BUBBLE_CORNER, BUBBLE_CORNER, BUBBLE_TAIL, BUBBLE_CORNER)
    } else {
        RoundedCornerShape(BUBBLE_TAIL, BUBBLE_CORNER, BUBBLE_CORNER, BUBBLE_CORNER)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment,
    ) {
        Surface(
            shape = shape,
            color = bgColor,
            modifier = Modifier.widthIn(max = BUBBLE_MAX_WIDTH),
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodySmall,
                color = textColor,
                modifier = Modifier.padding(BUBBLE_PADDING),
            )
        }
    }
}

@Composable
private fun TypingIndicator() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SPACING_SM),
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(TYPING_INDICATOR_SIZE),
            strokeWidth = TYPING_STROKE,
            strokeCap = StrokeCap.Round,
        )
        Text(
            text = TYPING_TEXT,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// endregion

// region — Profile Components

@Composable
private fun AvatarSection(
    profile: UserProfile,
    onAvatarClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AvatarWithOverlay(avatarUrl = profile.avatarUrl, onClick = onAvatarClick)
        Spacer(modifier = Modifier.height(SPACING_SM))
        Text(
            text = profile.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = profile.role,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = profile.email,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun AvatarWithOverlay(avatarUrl: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(AVATAR_SIZE),
        contentAlignment = Alignment.BottomEnd,
    ) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = AVATAR_DESC,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(AVATAR_SIZE)
                .clip(CircleShape)
                .clickable(onClick = onClick),
        )
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(CAMERA_BUTTON_SIZE),
        ) {
            IconButton(
                onClick = onClick,
                modifier = Modifier.size(CAMERA_BUTTON_SIZE),
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = CHANGE_AVATAR_DESC,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(CAMERA_ICON_SIZE),
                )
            }
        }
    }
}

@Composable
private fun StatsRow(profile: UserProfile) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        StatItem(value = profile.projectCount.toString(), label = PROJECTS_LABEL)
        StatItem(value = profile.reviewCount.toString(), label = REVIEWS_LABEL)
        StatItem(value = profile.starCount.toString(), label = STARS_LABEL)
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun EditButton(onClick: () -> Unit) {
    OutlinedButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = EDIT_DESC,
            modifier = Modifier.size(ICON_SIZE),
        )
        Spacer(modifier = Modifier.width(SPACING_XS))
        Text(EDIT_BUTTON_TEXT)
    }
}

@Composable
private fun ActivityRow(item: ActivityItem) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(CARD_PADDING)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = item.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// endregion

// region — Dialogs

@Composable
private fun EditProfileDialog(
    profile: UserProfile,
    onDismiss: () -> Unit,
    onSave: (name: String, role: String, email: String) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf(profile.name) }
    var role by rememberSaveable { mutableStateOf(profile.role) }
    var email by rememberSaveable { mutableStateOf(profile.email) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(EDIT_DIALOG_TITLE) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(DIALOG_FIELD_SPACING)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(NAME_LABEL) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text(TITLE_LABEL) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(EMAIL_LABEL) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name, role, email) },
                enabled = name.isNotBlank() && email.isNotBlank(),
            ) {
                Text(SAVE_BUTTON_TEXT)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(CANCEL_BUTTON_TEXT)
            }
        },
    )
}

@Composable
private fun AvatarPickerDialog(
    onDismiss: () -> Unit,
    onAvatarSelected: (String) -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(DIALOG_CORNER_RADIUS),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = DIALOG_ELEVATION,
        ) {
            Column(modifier = Modifier.padding(DIALOG_PADDING)) {
                Text(
                    text = AVATAR_PICKER_TITLE,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(DIALOG_FIELD_SPACING))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(AVATAR_GRID_COLUMNS),
                    horizontalArrangement = Arrangement.spacedBy(AVATAR_GRID_SPACING),
                    verticalArrangement = Arrangement.spacedBy(AVATAR_GRID_SPACING),
                    modifier = Modifier.height(AVATAR_GRID_HEIGHT),
                ) {
                    items(items = SAMPLE_AVATAR_URLS, key = { it }) { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = AVATAR_OPTION_DESC,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(AVATAR_OPTION_SIZE)
                                .clip(CircleShape)
                                .clickable { onAvatarSelected(url) },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(DIALOG_FIELD_SPACING))
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text(CANCEL_BUTTON_TEXT)
                }
            }
        }
    }
}

// endregion

// region — Constants

private val SAMPLE_AVATAR_URLS = listOf(
    "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200",
    "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
    "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200",
    "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200",
    "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200",
    "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200",
    "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200",
    "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200",
    "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200",
)

private val CHAT_OPTIONS = listOf(
    "What are my pending tasks?",
    "Summarize my recent activity",
    "Suggest next steps for my project",
)

private val CONTENT_PADDING = 16.dp
private val SECTION_SPACING = 24.dp
private val ITEM_SPACING = 12.dp
private val CARD_PADDING = 12.dp
private val SPACING_XS = 4.dp
private val SPACING_SM = 8.dp
private val AVATAR_SIZE = 96.dp
private val ICON_SIZE = 18.dp
private val CHIP_ICON_SIZE = 16.dp
private val CAMERA_BUTTON_SIZE = 28.dp
private val CAMERA_ICON_SIZE = 16.dp
private val DIALOG_FIELD_SPACING = 12.dp
private val DIALOG_PADDING = 20.dp
private val DIALOG_CORNER_RADIUS = 16.dp
private val DIALOG_ELEVATION = 6.dp
private val AVATAR_OPTION_SIZE = 64.dp
private val AVATAR_GRID_SPACING = 8.dp
private val AVATAR_GRID_HEIGHT = 220.dp
private const val AVATAR_GRID_COLUMNS = 3

// Chat bottom sheet
private val SHEET_MAX_HEIGHT = 400.dp
private const val SHEET_HEIGHT_FRACTION = 0.75f
private val SHEET_CORNER = 20.dp
private val SHEET_ELEVATION = 8.dp
private val SHEET_PADDING = 16.dp
private val CHAT_HEADER_ICON_SIZE = 20.dp
private val CLOSE_BUTTON_SIZE = 32.dp
private val CLOSE_ICON_SIZE = 18.dp
private val BUBBLE_CORNER = 16.dp
private val BUBBLE_TAIL = 4.dp
private val BUBBLE_PADDING = 10.dp
private val BUBBLE_MAX_WIDTH = 260.dp
private val TYPING_INDICATOR_SIZE = 14.dp
private val TYPING_STROKE = 2.dp
private const val SCRIM_ALPHA = 0.4f

private const val RECENT_ACTIVITY_TITLE = "Recent Activity"
private const val EDIT_BUTTON_TEXT = "Edit Profile"
private const val EDIT_DESC = "Edit profile"
private const val AVATAR_DESC = "Profile photo"
private const val CHANGE_AVATAR_DESC = "Change avatar"
private const val EDIT_DIALOG_TITLE = "Edit Profile"
private const val NAME_LABEL = "Name"
private const val TITLE_LABEL = "Title"
private const val EMAIL_LABEL = "Email"
private const val SAVE_BUTTON_TEXT = "Save"
private const val CANCEL_BUTTON_TEXT = "Cancel"
private const val AVATAR_PICKER_TITLE = "Choose Avatar"
private const val AVATAR_OPTION_DESC = "Avatar option"
private const val PROJECTS_LABEL = "Projects"
private const val REVIEWS_LABEL = "Reviews"
private const val STARS_LABEL = "Stars"
private const val ASK_AI_LABEL = "Ask AI"
private const val AI_ASSISTANT_TITLE = "AI Assistant"
private const val CLOSE_DESC = "Close"
private const val TYPING_TEXT = "Thinking..."

// endregion
