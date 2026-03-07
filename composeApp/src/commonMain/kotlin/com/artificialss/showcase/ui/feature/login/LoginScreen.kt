package com.artificialss.showcase.ui.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginScreen(
    presenter: LoginPresenter,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by presenter.uiState.collectAsStateWithLifecycle()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var rememberMe by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is LoginUiState.Success) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = HORIZONTAL_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        BrandingHeader()

        Spacer(modifier = Modifier.height(SPACING_XL))

        EmailField(value = email, onValueChange = { email = it })
        Spacer(modifier = Modifier.height(SPACING_MD))

        PasswordField(
            value = password,
            onValueChange = { password = it },
            visible = passwordVisible,
            onToggleVisibility = { passwordVisible = !passwordVisible },
        )
        Spacer(modifier = Modifier.height(SPACING_SM))

        RememberAndForgotRow(
            rememberMe = rememberMe,
            onRememberMeChanged = { rememberMe = it },
        )

        Spacer(modifier = Modifier.height(SPACING_LG))

        LoginButton(state = state, onClick = { presenter.onLogin(email, password) })

        ErrorText(state = state)

        Spacer(modifier = Modifier.height(SPACING_LG))

        DividerWithText()

        Spacer(modifier = Modifier.height(SPACING_MD))

        SignUpPrompt()

        Spacer(modifier = Modifier.weight(1f))

        FooterText()
        Spacer(modifier = Modifier.height(SPACING_MD))
    }
}

@Composable
private fun BrandingHeader() {
    Icon(
        imageVector = Icons.Default.Lock,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(BRAND_ICON_SIZE),
    )
    Spacer(modifier = Modifier.height(SPACING_MD))
    Text(
        text = TITLE_TEXT,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(SPACING_XS))
    Text(
        text = SUBTITLE_TEXT,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun EmailField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(EMAIL_LABEL) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onToggleVisibility: () -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(PASSWORD_LABEL) },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (visible) HIDE_PASSWORD_DESC else SHOW_PASSWORD_DESC,
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun RememberAndForgotRow(
    rememberMe: Boolean,
    onRememberMeChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = onRememberMeChanged,
                modifier = Modifier.size(CHECKBOX_SIZE),
            )
            Spacer(modifier = Modifier.width(SPACING_XS))
            Text(
                text = REMEMBER_ME_TEXT,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        TextButton(onClick = { }) {
            Text(
                text = FORGOT_PASSWORD_TEXT,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun LoginButton(
    state: LoginUiState,
    onClick: () -> Unit,
) {
    val isLoading = state is LoginUiState.Loading
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(BUTTON_HEIGHT),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = PROGRESS_STROKE,
            )
        } else {
            Text(SIGN_IN_TEXT)
        }
    }
}

@Composable
private fun ErrorText(state: LoginUiState) {
    if (state is LoginUiState.Error) {
        Spacer(modifier = Modifier.height(SPACING_MD))
        Text(
            text = state.message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun DividerWithText() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f))
        Text(
            text = OR_TEXT,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = SPACING_SM),
        )
        HorizontalDivider(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun SignUpPrompt() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = NO_ACCOUNT_TEXT,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        TextButton(onClick = { }) {
            Text(
                text = SIGN_UP_TEXT,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun FooterText() {
    Text(
        text = FOOTER_TEXT,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.outline,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}

private val HORIZONTAL_PADDING = 32.dp
private val SPACING_XS = 4.dp
private val SPACING_SM = 8.dp
private val SPACING_MD = 16.dp
private val SPACING_LG = 24.dp
private val SPACING_XL = 40.dp
private val BUTTON_HEIGHT = 52.dp
private val PROGRESS_STROKE = 2.dp
private val BRAND_ICON_SIZE = 48.dp
private val CHECKBOX_SIZE = 24.dp

private const val TITLE_TEXT = "Welcome Back"
private const val SUBTITLE_TEXT = "Sign in to your account"
private const val EMAIL_LABEL = "Email"
private const val PASSWORD_LABEL = "Password"
private const val SHOW_PASSWORD_DESC = "Show password"
private const val HIDE_PASSWORD_DESC = "Hide password"
private const val REMEMBER_ME_TEXT = "Remember me"
private const val FORGOT_PASSWORD_TEXT = "Forgot password?"
private const val SIGN_IN_TEXT = "Sign In"
private const val OR_TEXT = "OR"
private const val NO_ACCOUNT_TEXT = "Don't have an account?"
private const val SIGN_UP_TEXT = "Sign Up"
private const val FOOTER_TEXT = "By signing in, you agree to our Terms of Service and Privacy Policy"
