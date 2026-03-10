package com.artificialss.showcase.ui.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.artificialss.showcase.ui.localization.LocalAppStrings

@Composable
fun LoginScreen(
    presenter: LoginPresenter,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by presenter.uiState.collectAsStateWithLifecycle()
    var email by rememberSaveable { mutableStateOf(DEFAULT_EMAIL) }
    var password by rememberSaveable { mutableStateOf(DEFAULT_PASSWORD) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var rememberMe by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is LoginUiState.Success) onLoginSuccess()
    }

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        StatusBarBackground()
        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .padding(top = statusBarHeight, bottom = navBarHeight),
        ) {
            if (maxWidth > maxHeight) {
                LandscapeLoginContent(
                    email = email,
                    password = password,
                    passwordVisible = passwordVisible,
                    rememberMe = rememberMe,
                    state = state,
                    onEmailChange = { email = it },
                    onPasswordChange = { password = it },
                    onToggleVisibility = { passwordVisible = !passwordVisible },
                    onRememberMeChange = { rememberMe = it },
                    onLogin = { presenter.onLogin(email, password) },
                )
            } else {
                PortraitLoginContent(
                    email = email,
                    password = password,
                    passwordVisible = passwordVisible,
                    rememberMe = rememberMe,
                    state = state,
                    onEmailChange = { email = it },
                    onPasswordChange = { password = it },
                    onToggleVisibility = { passwordVisible = !passwordVisible },
                    onRememberMeChange = { rememberMe = it },
                    onLogin = { presenter.onLogin(email, password) },
                )
            }
        }
    }
}

@Composable
private fun PortraitLoginContent(
    email: String,
    password: String,
    passwordVisible: Boolean,
    rememberMe: Boolean,
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onToggleVisibility: () -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onLogin: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = HORIZONTAL_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(SPACING_MD))
        BrandingHeader()
        Spacer(modifier = Modifier.height(SPACING_LG))
        LoginFormContent(
            email = email,
            password = password,
            passwordVisible = passwordVisible,
            rememberMe = rememberMe,
            state = state,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onToggleVisibility = onToggleVisibility,
            onRememberMeChange = onRememberMeChange,
            onLogin = onLogin,
        )
        Spacer(modifier = Modifier.height(SPACING_LG))
        FooterText()
        Spacer(modifier = Modifier.height(SPACING_MD))
    }
}

@Composable
private fun LandscapeLoginContent(
    email: String,
    password: String,
    passwordVisible: Boolean,
    rememberMe: Boolean,
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onToggleVisibility: () -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onLogin: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(HORIZONTAL_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            BrandingHeader()
        }
        VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = HORIZONTAL_PADDING, vertical = SPACING_MD),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            LoginFormContent(
                email = email,
                password = password,
                passwordVisible = passwordVisible,
                rememberMe = rememberMe,
                state = state,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onToggleVisibility = onToggleVisibility,
                onRememberMeChange = onRememberMeChange,
                onLogin = onLogin,
            )
        }
    }
}

@Composable
private fun LoginFormContent(
    email: String,
    password: String,
    passwordVisible: Boolean,
    rememberMe: Boolean,
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onToggleVisibility: () -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onLogin: () -> Unit,
) {
    val s = LocalAppStrings.current
    EmailField(value = email, onValueChange = onEmailChange, label = s.loginEmail)
    Spacer(modifier = Modifier.height(SPACING_MD))
    PasswordField(
        value = password,
        onValueChange = onPasswordChange,
        visible = passwordVisible,
        onToggleVisibility = onToggleVisibility,
        label = s.loginPassword,
        showDesc = s.loginShowPassword,
        hideDesc = s.loginHidePassword,
    )
    Spacer(modifier = Modifier.height(SPACING_SM))
    RememberAndForgotRow(
        rememberMe = rememberMe,
        onRememberMeChanged = onRememberMeChange,
        rememberMeLabel = s.loginRememberMe,
        forgotLabel = s.loginForgotPassword,
    )
    Spacer(modifier = Modifier.height(SPACING_LG))
    LoginButton(state = state, onClick = onLogin, signInLabel = s.loginSignIn, signingInLabel = s.loginSigningIn)
    ErrorText(state = state)
    Spacer(modifier = Modifier.height(SPACING_LG))
    DividerWithText(orLabel = s.loginOr)
    Spacer(modifier = Modifier.height(SPACING_MD))
    SignUpPrompt(noAccountLabel = s.loginNoAccount, signUpLabel = s.loginSignUp)
}

@Composable
private fun StatusBarBackground() {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(statusBarHeight)
            .background(MaterialTheme.colorScheme.primary),
    )
}

@Composable
private fun BrandingHeader() {
    val s = LocalAppStrings.current
    Icon(
        imageVector = Icons.Default.Lock,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(BRAND_ICON_SIZE),
    )
    Spacer(modifier = Modifier.height(SPACING_MD))
    Text(
        text = s.loginTitle,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Spacer(modifier = Modifier.height(SPACING_XS))
    Text(
        text = s.loginSubtitle,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun EmailField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
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
    label: String,
    showDesc: String,
    hideDesc: String,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (visible) hideDesc else showDesc,
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
    rememberMeLabel: String,
    forgotLabel: String,
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
                text = rememberMeLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        TextButton(onClick = { }) {
            Text(
                text = forgotLabel,
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
    signInLabel: String,
    signingInLabel: String,
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(LOADER_SIZE),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = LOADER_STROKE,
                    strokeCap = StrokeCap.Round,
                )
                Spacer(modifier = Modifier.width(SPACING_SM))
                Text(signingInLabel)
            }
        } else {
            Text(signInLabel)
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
private fun DividerWithText(orLabel: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f))
        Text(
            text = orLabel,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = SPACING_SM),
        )
        HorizontalDivider(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun SignUpPrompt(noAccountLabel: String, signUpLabel: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = noAccountLabel,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        TextButton(onClick = { }) {
            Text(
                text = signUpLabel,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun FooterText() {
    val s = LocalAppStrings.current
    Text(
        text = s.loginFooter,
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
private val BRAND_ICON_SIZE = 48.dp
private val CHECKBOX_SIZE = 24.dp
private val LOADER_SIZE = 18.dp
private val LOADER_STROKE = 2.dp

private const val DEFAULT_EMAIL = "guest@artificialss.com"
private const val DEFAULT_PASSWORD = "showcase2025"
