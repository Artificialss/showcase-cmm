package com.artificialss.showcase.ui.feature.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ComponentsScreen(modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var toastMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = CONTENT_PADDING),
            verticalArrangement = Arrangement.spacedBy(SECTION_SPACING),
        ) {
            item { Spacer(modifier = Modifier.height(CONTENT_PADDING)) }

            item {
                Text(
                    text = "UI Components",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
            }

            // Buttons
            item {
                ComponentSection(title = "Buttons") {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(ITEM_SPACING),
                        verticalArrangement = Arrangement.spacedBy(ITEM_SPACING),
                    ) {
                        Button(onClick = { }) { Text("Primary") }
                        OutlinedButton(onClick = { }) { Text("Outlined") }
                        ElevatedButton(onClick = { }) { Text("Elevated") }
                        FilledTonalButton(onClick = { }) { Text("Tonal") }
                        TextButton(onClick = { }) { Text("Text") }
                        Button(onClick = { }, enabled = false) { Text("Disabled") }
                    }
                }
            }

            // Icon Buttons
            item {
                ComponentSection(title = "Icon Buttons") {
                    Row(horizontalArrangement = Arrangement.spacedBy(ITEM_SPACING)) {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Favorite, contentDescription = "Favorite")
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Info, contentDescription = "Info", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            // Text Fields with validation
            item { TextFieldsSection() }

            // Chips
            item {
                ComponentSection(title = "Chips") {
                    val chipLabels = listOf("Kotlin", "Compose", "Multiplatform", "Room", "Koin", "Apollo", "Coil")
                    var selectedChip by remember { mutableStateOf(chipLabels.first()) }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(ITEM_SPACING),
                        verticalArrangement = Arrangement.spacedBy(ITEM_SPACING),
                    ) {
                        chipLabels.forEach { label ->
                            FilterChip(
                                selected = label == selectedChip,
                                onClick = { selectedChip = label },
                                label = { Text(label) },
                                leadingIcon = if (label == selectedChip) {
                                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(CHIP_ICON_SIZE)) }
                                } else {
                                    null
                                },
                            )
                        }
                    }
                }
            }

            // Toggle Switches
            item {
                ComponentSection(title = "Toggle Switches") {
                    var notifications by remember { mutableStateOf(true) }
                    var darkMode by remember { mutableStateOf(false) }
                    var autoSync by remember { mutableStateOf(true) }
                    SwitchRow(label = "Enable notifications", checked = notifications) { notifications = it }
                    SwitchRow(label = "Dark mode", checked = darkMode) { darkMode = it }
                    SwitchRow(label = "Auto-sync data", checked = autoSync) { autoSync = it }
                }
            }

            // Checkboxes
            item {
                ComponentSection(title = "Checkboxes") {
                    var terms by remember { mutableStateOf(false) }
                    var marketing by remember { mutableStateOf(true) }
                    var analytics by remember { mutableStateOf(false) }
                    CheckboxRow(label = "Accept terms and conditions", checked = terms) { terms = it }
                    CheckboxRow(label = "Receive marketing emails", checked = marketing) { marketing = it }
                    CheckboxRow(label = "Share anonymous analytics", checked = analytics) { analytics = it }
                }
            }

            // Radio Buttons
            item {
                ComponentSection(title = "Subscription Plan") {
                    val plans = listOf(
                        RadioOption(
                            title = "Free",
                            subtitle = "Basic features, 1 project",
                            icon = Icons.Default.Person,
                        ),
                        RadioOption(
                            title = "Pro",
                            subtitle = "Unlimited projects, priority support",
                            icon = Icons.Default.Favorite,
                        ),
                        RadioOption(
                            title = "Enterprise",
                            subtitle = "Custom integrations, dedicated account manager",
                            icon = Icons.Default.Lock,
                        ),
                    )
                    var selected by remember { mutableIntStateOf(0) }
                    plans.forEachIndexed { index, option ->
                        RadioOptionCard(
                            option = option,
                            isSelected = index == selected,
                            onClick = { selected = index },
                        )
                        if (index < plans.lastIndex) {
                            Spacer(modifier = Modifier.height(ITEM_SPACING))
                        }
                    }
                }
            }

            // Progress Indicators + Shimmer
            item { ProgressSection() }

            // Sliders
            item { SlidersSection() }

            // Snackbar & Toast
            item {
                ComponentSection(title = "Snackbar & Toast") {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(ITEM_SPACING),
                        verticalArrangement = Arrangement.spacedBy(ITEM_SPACING),
                    ) {
                        Button(onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Item deleted successfully",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short,
                                )
                            }
                        }) { Text("Snackbar") }

                        OutlinedButton(onClick = {
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Connection restored",
                                    actionLabel = "Dismiss",
                                    duration = SnackbarDuration.Short,
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                }
                            }
                        }) { Text("Snackbar + Action") }

                        ElevatedButton(onClick = {
                            toastMessage = "Settings saved!"
                        }) { Text("Toast") }
                    }
                }
            }

            // Dialogs
            item { DialogsSection() }

            item { Spacer(modifier = Modifier.height(CONTENT_PADDING)) }
        }

        // Toast overlay
        if (toastMessage != null) {
            ToastOverlay(
                message = toastMessage ?: "",
                onDismiss = { toastMessage = null },
            )
        }

        // Snackbar host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(CONTENT_PADDING),
        )
    }
}

// region — Text Fields with Validation

@Composable
private fun TextFieldsSection() {
    ComponentSection(title = "Text Fields") {
        var name by remember { mutableStateOf("") }
        var nameTouched by remember { mutableStateOf(false) }
        val nameError = nameTouched && name.isBlank()

        var email by remember { mutableStateOf("") }
        var emailTouched by remember { mutableStateOf(false) }
        val emailError = emailTouched && email.isNotEmpty() && !email.matches(EMAIL_REGEX)

        var password by remember { mutableStateOf("") }
        var passwordTouched by remember { mutableStateOf(false) }
        var passwordVisible by remember { mutableStateOf(false) }
        val passwordError = passwordTouched && password.isNotEmpty() && password.length < MIN_PASSWORD_LENGTH

        OutlinedTextField(
            value = name,
            onValueChange = { name = it; nameTouched = true },
            label = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = nameError,
            supportingText = if (nameError) {
                { Text("Name is required") }
            } else {
                null
            },
        )
        Spacer(modifier = Modifier.height(ITEM_SPACING))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; emailTouched = true },
            label = { Text("Email Address") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = emailError,
            supportingText = if (emailError) {
                { Text("Enter a valid email address") }
            } else {
                { Text("e.g. name@example.com") }
            },
        )
        Spacer(modifier = Modifier.height(ITEM_SPACING))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; passwordTouched = true },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Favorite else Icons.Default.Lock,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = passwordError,
            supportingText = if (passwordError) {
                { Text("Must be at least $MIN_PASSWORD_LENGTH characters") }
            } else {
                { Text("Must be at least $MIN_PASSWORD_LENGTH characters") }
            },
        )
    }
}

// endregion

// region — Progress Indicators + Shimmer

@Composable
private fun ProgressSection() {
    ComponentSection(title = "Progress Indicators") {
        Text("Circular — Indeterminate", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(ITEM_SPACING))
        Row(
            horizontalArrangement = Arrangement.spacedBy(PROGRESS_ITEM_SPACING),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircularProgressIndicator(modifier = Modifier.size(PROGRESS_SMALL))
            CircularProgressIndicator(modifier = Modifier.size(PROGRESS_MEDIUM))
            CircularProgressIndicator(
                modifier = Modifier.size(PROGRESS_LARGE),
                strokeWidth = PROGRESS_THICK_STROKE,
                strokeCap = StrokeCap.Round,
            )
        }

        Spacer(modifier = Modifier.height(PROGRESS_SECTION_SPACING))

        Text("Circular — Determinate", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(ITEM_SPACING))
        var animTarget by remember { mutableFloatStateOf(0f) }
        LaunchedEffect(Unit) { animTarget = DEMO_PROGRESS_HIGH }
        val animProgress by animateFloatAsState(
            targetValue = animTarget,
            animationSpec = tween(durationMillis = PROGRESS_ANIM_MS),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(PROGRESS_ITEM_SPACING),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProgressWithLabel(progress = DEMO_PROGRESS_LOW, label = "25%")
            ProgressWithLabel(progress = DEMO_PROGRESS_MID, label = "50%")
            ProgressWithLabel(progress = animProgress, label = "75%")
        }

        Spacer(modifier = Modifier.height(PROGRESS_SECTION_SPACING))

        Text("Linear — Indeterminate", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(ITEM_SPACING))
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(LINEAR_CORNER)),
        )

        Spacer(modifier = Modifier.height(PROGRESS_SECTION_SPACING))

        Text("Linear — Determinate", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(ITEM_SPACING))
        LinearProgressIndicator(
            progress = { DEMO_PROGRESS_LOW },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(LINEAR_CORNER)),
        )
        Spacer(modifier = Modifier.height(ITEM_SPACING))
        LinearProgressIndicator(
            progress = { DEMO_PROGRESS_MID },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(LINEAR_CORNER)),
        )
        Spacer(modifier = Modifier.height(ITEM_SPACING))
        LinearProgressIndicator(
            progress = { animProgress },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(LINEAR_CORNER)),
        )

        Spacer(modifier = Modifier.height(PROGRESS_SECTION_SPACING))

        Text("Shimmer Loading", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(ITEM_SPACING))
        Row(
            horizontalArrangement = Arrangement.spacedBy(SHIMMER_ROW_SPACING),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(SHIMMER_IMAGE_SIZE)
                    .clip(CircleShape)
                    .shimmerEffect(),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(SHIMMER_LINE_SPACING),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(SHIMMER_TITLE_FRACTION)
                        .height(SHIMMER_TITLE_HEIGHT)
                        .clip(RoundedCornerShape(SHIMMER_CORNER))
                        .shimmerEffect(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SHIMMER_DESC_HEIGHT)
                        .clip(RoundedCornerShape(SHIMMER_CORNER))
                        .shimmerEffect(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(SHIMMER_SHORT_FRACTION)
                        .height(SHIMMER_DESC_HEIGHT)
                        .clip(RoundedCornerShape(SHIMMER_CORNER))
                        .shimmerEffect(),
                )
            }
        }
    }
}

// endregion

// region — Sliders

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun SlidersSection() {
    ComponentSection(title = "Sliders") {
        var brightness by remember { mutableFloatStateOf(DEMO_SLIDER_VALUE) }
        SliderRow(
            label = "Brightness",
            value = "${(brightness * PERCENT_MULTIPLIER).toInt()}%",
            valueColor = MaterialTheme.colorScheme.primary,
        )
        val primaryColor = MaterialTheme.colorScheme.primary
        Slider(
            value = brightness,
            onValueChange = { brightness = it },
            valueRange = SLIDER_MIN..SLIDER_MAX,
            thumb = { SliderThumb(color = primaryColor) },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    drawStopIndicator = null,
                    thumbTrackGapSize = 0.dp,
                )
            },
            modifier = Modifier.height(SLIDER_HEIGHT),
        )

        var temperature by remember { mutableFloatStateOf(TEMP_DEFAULT) }
        SliderRow(
            label = "Temperature",
            value = "${temperature.roundToInt()}${TEMP_UNIT}",
            valueColor = MaterialTheme.colorScheme.tertiary,
        )
        val tertiaryColor = MaterialTheme.colorScheme.tertiary
        Slider(
            value = temperature,
            onValueChange = { temperature = it },
            valueRange = TEMP_MIN..TEMP_MAX,
            steps = TEMP_STEPS,
            colors = SliderDefaults.colors(
                thumbColor = tertiaryColor,
                activeTrackColor = tertiaryColor,
            ),
            thumb = { SliderThumb(color = tertiaryColor) },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    drawStopIndicator = null,
                    thumbTrackGapSize = 0.dp,
                    colors = SliderDefaults.colors(
                        activeTrackColor = tertiaryColor,
                    ),
                )
            },
            modifier = Modifier.height(SLIDER_HEIGHT),
        )

        var volume by remember { mutableFloatStateOf(VOLUME_DEFAULT) }
        SliderRow(
            label = "Volume",
            value = "${(volume * PERCENT_MULTIPLIER).toInt()}",
            valueColor = MaterialTheme.colorScheme.error,
        )
        val errorColor = MaterialTheme.colorScheme.error
        val inactiveTrackColor = MaterialTheme.colorScheme.outlineVariant
        Slider(
            value = volume,
            onValueChange = { volume = it },
            valueRange = SLIDER_MIN..SLIDER_MAX,
            colors = SliderDefaults.colors(
                thumbColor = errorColor,
                activeTrackColor = errorColor,
                inactiveTrackColor = inactiveTrackColor,
            ),
            thumb = { SliderThumb(color = errorColor) },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    drawStopIndicator = null,
                    thumbTrackGapSize = 0.dp,
                    colors = SliderDefaults.colors(
                        activeTrackColor = errorColor,
                        inactiveTrackColor = inactiveTrackColor,
                    ),
                )
            },
            modifier = Modifier.height(SLIDER_HEIGHT),
        )

        var priceRange by remember { mutableStateOf(PRICE_DEFAULT_MIN..PRICE_DEFAULT_MAX) }
        SliderRow(
            label = "Price Range",
            value = "$${priceRange.start.roundToInt()} – $${priceRange.endInclusive.roundToInt()}",
            valueColor = MaterialTheme.colorScheme.secondary,
        )
        val secondaryColor = MaterialTheme.colorScheme.secondary
        RangeSlider(
            value = priceRange,
            onValueChange = { priceRange = it },
            valueRange = PRICE_MIN..PRICE_MAX,
            startThumb = { RangeSliderThumb() },
            endThumb = { RangeSliderThumb() },
            track = { rangeSliderState ->
                SliderDefaults.Track(
                    rangeSliderState = rangeSliderState,
                    drawStopIndicator = null,
                    thumbTrackGapSize = 0.dp,
                    colors = SliderDefaults.colors(
                        activeTrackColor = secondaryColor,
                    ),
                )
            },
            modifier = Modifier.height(SLIDER_HEIGHT),
        )
    }
}

// endregion

// region — Dialogs

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DialogsSection() {
    var showSimpleDialog by remember { mutableStateOf(false) }
    var showBasicDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showInputDialog by remember { mutableStateOf(false) }

    ComponentSection(title = "Dialogs") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(ITEM_SPACING),
            verticalArrangement = Arrangement.spacedBy(ITEM_SPACING),
        ) {
            FilledTonalButton(onClick = { showSimpleDialog = true }) { Text("Simple") }
            Button(onClick = { showBasicDialog = true }) { Text("Info") }
            OutlinedButton(onClick = { showConfirmDialog = true }) { Text("Confirm") }
            ElevatedButton(onClick = { showInputDialog = true }) { Text("Input") }
        }
    }

    if (showSimpleDialog) {
        AlertDialog(
            onDismissRequest = { showSimpleDialog = false },
            title = { Text("Notice") },
            text = { Text("Your changes have been saved.") },
            confirmButton = {
                TextButton(onClick = { showSimpleDialog = false }) { Text("OK") }
            },
        )
    }

    if (showBasicDialog) {
        AlertDialog(
            onDismissRequest = { showBasicDialog = false },
            icon = { Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text("App Update Available") },
            text = { Text("A new version of the app is available with bug fixes and performance improvements.") },
            confirmButton = {
                Button(onClick = { showBasicDialog = false }) { Text("Update Now") }
            },
            dismissButton = {
                TextButton(onClick = { showBasicDialog = false }) { Text("Later") }
            },
        )
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                )
            },
            title = { Text("Delete Account?") },
            text = {
                Text(
                    "This action cannot be undone. All your data, projects, and settings " +
                        "will be permanently removed. Are you sure you want to proceed?",
                )
            },
            confirmButton = {
                Button(
                    onClick = { showConfirmDialog = false },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                    ),
                ) { Text("Delete") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showConfirmDialog = false }) { Text("Cancel") }
            },
        )
    }

    if (showInputDialog) {
        var inputName by remember { mutableStateOf("") }
        var inputDate by remember { mutableStateOf("") }
        var inputNotes by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showInputDialog = false },
            title = { Text("New Event") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(ITEM_SPACING)) {
                    OutlinedTextField(
                        value = inputName,
                        onValueChange = { inputName = it },
                        label = { Text("Event Name") },
                        leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = inputDate,
                        onValueChange = { inputDate = it },
                        label = { Text("Date") },
                        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = inputNotes,
                        onValueChange = { inputNotes = it },
                        label = { Text("Notes") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = INPUT_NOTES_MIN_LINES,
                        maxLines = INPUT_NOTES_MAX_LINES,
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showInputDialog = false }) { Text("Create") }
            },
            dismissButton = {
                TextButton(onClick = { showInputDialog = false }) { Text("Cancel") }
            },
        )
    }
}

// endregion

// region — Shared Components

@Composable
private fun ComponentSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(CARD_PADDING)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = DIVIDER_PADDING))
            content()
        }
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SWITCH_VERTICAL_PADDING),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun CheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
    }
}

private data class RadioOption(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
)

@Composable
private fun RadioOptionCard(
    option: RadioOption,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }
    val bgColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = SELECTED_BG_ALPHA)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(RADIO_CARD_CORNER))
            .border(RADIO_CARD_BORDER, borderColor, RoundedCornerShape(RADIO_CARD_CORNER))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(RADIO_CARD_PADDING),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(RADIO_ICON_BOX)
                .clip(CircleShape)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = option.icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(RADIO_ICON_SIZE),
            )
        }
        Spacer(modifier = Modifier.width(RADIO_TEXT_SPACING))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = option.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = option.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun ProgressWithLabel(progress: Float, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(PROGRESS_LABELED_SIZE),
                strokeWidth = PROGRESS_LABELED_STROKE,
                strokeCap = StrokeCap.Round,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

private fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = SHIMMER_DURATION_MS),
        ),
        label = "shimmerOffset",
    )

    background(
        brush = Brush.linearGradient(
            colors = SHIMMER_COLORS,
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat()),
        ),
    ).onGloballyPositioned { size = it.size }
}

@Composable
private fun SliderThumb(color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .size(THUMB_SIZE)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSurface),
    )
}

@Composable
private fun RangeSliderThumb() {
    Box(
        modifier = Modifier
            .width(RANGE_THUMB_WIDTH)
            .height(RANGE_THUMB_HEIGHT)
            .clip(RoundedCornerShape(RANGE_THUMB_CORNER))
            .background(MaterialTheme.colorScheme.onSurface),
    )
}

@Composable
private fun SliderRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = valueColor,
        )
    }
}

@Composable
private fun ToastOverlay(
    message: String,
    onDismiss: () -> Unit,
) {
    LaunchedEffect(message) {
        delay(TOAST_DURATION_MS)
        onDismiss()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Card(
            modifier = Modifier.padding(bottom = TOAST_BOTTOM_PADDING),
            shape = RoundedCornerShape(TOAST_CORNER),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseSurface,
            ),
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                modifier = Modifier.padding(horizontal = TOAST_H_PADDING, vertical = TOAST_V_PADDING),
                textAlign = TextAlign.Center,
            )
        }
    }
}

// endregion

// region — Constants

private val CONTENT_PADDING = 16.dp
private val SECTION_SPACING = 12.dp
private val ITEM_SPACING = 8.dp
private val CARD_PADDING = 16.dp
private val DIVIDER_PADDING = 8.dp
private val CHIP_ICON_SIZE = 18.dp
private val SWITCH_VERTICAL_PADDING = 4.dp

// Radio option card
private val RADIO_CARD_CORNER = 12.dp
private val RADIO_CARD_BORDER = 1.5.dp
private val RADIO_CARD_PADDING = 12.dp
private val RADIO_ICON_BOX = 40.dp
private val RADIO_ICON_SIZE = 20.dp
private val RADIO_TEXT_SPACING = 12.dp
private const val SELECTED_BG_ALPHA = 0.1f

// Progress indicators
private val PROGRESS_ITEM_SPACING = 16.dp
private val PROGRESS_SMALL = 24.dp
private val PROGRESS_MEDIUM = 36.dp
private val PROGRESS_LARGE = 48.dp
private val PROGRESS_THICK_STROKE = 5.dp
private val PROGRESS_SECTION_SPACING = 16.dp
private val PROGRESS_LABELED_SIZE = 52.dp
private val PROGRESS_LABELED_STROKE = 5.dp
private val LINEAR_CORNER = 4.dp
private const val PROGRESS_ANIM_MS = 1500
private const val DEMO_PROGRESS_LOW = 0.25f
private const val DEMO_PROGRESS_MID = 0.5f
private const val DEMO_PROGRESS_HIGH = 0.75f

// Shimmer
private const val SHIMMER_DURATION_MS = 1000
private val SHIMMER_COLORS = listOf(
    Color(0xFFF9F9F9),
    Color(0xFFECECEC),
    Color(0xFFFFFFFF),
)
private val SHIMMER_IMAGE_SIZE = 52.dp
private val SHIMMER_TITLE_HEIGHT = 14.dp
private val SHIMMER_DESC_HEIGHT = 10.dp
private val SHIMMER_ROW_SPACING = 12.dp
private val SHIMMER_LINE_SPACING = 6.dp
private val SHIMMER_CORNER = 4.dp
private const val SHIMMER_TITLE_FRACTION = 0.6f
private const val SHIMMER_SHORT_FRACTION = 0.4f

// Sliders
private val SLIDER_HEIGHT = 32.dp
private val THUMB_SIZE = 20.dp
private val RANGE_THUMB_WIDTH = 4.dp
private val RANGE_THUMB_HEIGHT = 24.dp
private val RANGE_THUMB_CORNER = 2.dp
private const val DEMO_SLIDER_VALUE = 0.5f
private const val SLIDER_MIN = 0f
private const val SLIDER_MAX = 1f
private const val PERCENT_MULTIPLIER = 100
private const val TEMP_DEFAULT = 22f
private const val TEMP_MIN = 16f
private const val TEMP_MAX = 30f
private const val TEMP_STEPS = 13
private const val TEMP_UNIT = "\u00B0C"
private const val VOLUME_DEFAULT = 0.7f
private const val PRICE_MIN = 0f
private const val PRICE_MAX = 500f
private const val PRICE_DEFAULT_MIN = 50f
private const val PRICE_DEFAULT_MAX = 300f

// Text field validation
private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
private const val MIN_PASSWORD_LENGTH = 8

// Dialog inputs
private const val INPUT_NOTES_MIN_LINES = 3
private const val INPUT_NOTES_MAX_LINES = 5

// Toast
private val TOAST_BOTTOM_PADDING = 80.dp
private val TOAST_CORNER = 24.dp
private val TOAST_H_PADDING = 24.dp
private val TOAST_V_PADDING = 12.dp
private const val TOAST_DURATION_MS = 2500L

// endregion
