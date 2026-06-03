package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import android.widget.Toast
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.*
import com.example.ui.theme.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun MainLayout(viewModel: AppViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect Toast/Notification events from ViewModel
    LaunchedEffect(key1 = true) {
        viewModel.toastMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DeepOceanSapphire, CharcoalObsidian)
                )
            )
    ) {
        // App Navigation Routing Controller
        when (currentScreen) {
            Screen.Splash -> SplashScreenView(viewModel)
            Screen.OnboardingCarousel -> OnboardingCarouselView(viewModel)
            Screen.Registration -> RegistrationView(viewModel)
            Screen.Login -> LoginView(viewModel)
            Screen.TerritorySelection -> TerritorySelectionView(viewModel)
            Screen.PersonalitySetup -> PersonalitySetupView(viewModel)
            Screen.CitizenOath -> CitizenOathView(viewModel)
            Screen.MainDashboard -> MainDashboardView(viewModel, snackbarHostState)
        }

        // Floating Toast Notification
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
            snackbar = { snap ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = VelvetCard),
                    border = BorderStroke(1.dp, RegalGold),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = "Imperial Notice",
                            tint = RegalGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = snap.visuals.message,
                            color = GhostWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        )
    }
}

// ============================================================================
// 1. SPLASH SCREEN (Deep holographic rotational aesthetic)
// ============================================================================
@Composable
fun SplashScreenView(viewModel: AppViewModel) {
    val scope = rememberCoroutineScope()
    var animationState by remember { mutableStateOf(0) }

    // Start Sequential Word animation
    LaunchedEffect(key1 = true) {
        kotlinx.coroutines.delay(600)
        animationState = 1 // Show ONE EARTH
        kotlinx.coroutines.delay(800)
        animationState = 2 // Show ONE FAMILY
        kotlinx.coroutines.delay(800)
        animationState = 3 // Show ENTER THE EMPIRE
    }

    // Rotating Core effect
    val infiniteTransition = rememberInfiniteTransition(label = "Globe rotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Holographic Globe representation
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(220.dp)
        ) {
            // Radiant gold background glow
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(RegalGold.copy(alpha = 0.15f), shape = CircleShape)
            )

            // Dynamic rotating orbit icons
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Drawing planetary abstract rings
                drawCircle(
                    color = RegalGold.copy(alpha = 0.4f),
                    radius = 90.dp.toPx(),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5f * density)
                )
                drawCircle(
                    color = ElectricBlue.copy(alpha = 0.3f),
                    radius = 110.dp.toPx(),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f * density)
                )
            }

            // Central Crown Crest
            Icon(
                imageVector = Icons.Default.Public,
                contentDescription = "Cosmic Globe",
                tint = RegalGold,
                modifier = Modifier
                    .size(100.dp)
            )

            Icon(
                imageVector = Icons.Default.MilitaryTech,
                contentDescription = "Embassy Shield",
                tint = LustrousAmber,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = (-15).dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Sequenced typography fades
        AnimatedVisibility(
            visible = animationState >= 1,
            enter = fadeIn(tween(600)) + slideInVertically { 20 }
        ) {
            Text(
                text = "ONE EARTH",
                color = GhostWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 4.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(
            visible = animationState >= 2,
            enter = fadeIn(tween(600)) + slideInVertically { 20 }
        ) {
            Text(
                text = "ONE FAMILY",
                color = RegalGold,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = animationState >= 3,
            enter = fadeIn(tween(600)) + slideInVertically { 20 }
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "A Meritocratic Digital Civilization",
                    color = MutedSlate,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(36.dp))
                Button(
                    onClick = { viewModel.navigateTo(Screen.OnboardingCarousel) },
                    colors = ButtonDefaults.buttonColors(containerColor = RegalGold, contentColor = CharcoalObsidian),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, LightGold),
                    modifier = Modifier.width(220.dp)
                ) {
                    Text(
                        text = "ENTER THE EMPIRE",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { viewModel.navigateTo(Screen.Login) },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = RegalGold),
                    border = BorderStroke(1.dp, RegalGold),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.width(220.dp)
                ) {
                    Text(
                        text = "CITIZEN SIGN IN",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

// ============================================================================
// 2. ONBOARDING CAROUSEL (Symmetrical Gold framed Slides)
// ============================================================================
@Composable
fun OnboardingCarouselView(viewModel: AppViewModel) {
    var activeSlideIndex by remember { mutableStateOf(0) }

    val slides = listOf(
        OnboardingSlide(
            title = "Wisdom over Popularity",
            desc = "In this digital Empire, character and verifiable knowledge wield greater structural influence than cheap follow metrics or clout chase loops.",
            icon = Icons.Default.MenuBook,
            themeColor = RegalGold
        ),
        OnboardingSlide(
            title = "Hierarchy by Merit",
            desc = "Every citizen starts their journey equal. Ascend from Citizen to Guardian, Noble, or Prince purely based on educational and community service credits.",
            icon = Icons.Default.EmojiEvents,
            themeColor = LustrousAmber
        ),
        OnboardingSlide(
            title = "Represent Your Territory",
            desc = "Engage in friendly competitions, resolve planetary challenges, coordinate reforestation, and run for the democratic Sovereign crown of the world.",
            icon = Icons.Default.Map,
            themeColor = ElectricBlue
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App header
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Stars, contentDescription = null, tint = RegalGold, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                "ONE EARTH ONE FAMILY",
                color = GhostWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }

        // Middle slide card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 32.dp),
            colors = CardDefaults.cardColors(containerColor = VelvetCard),
            border = BorderStroke(1.5.dp, slides[activeSlideIndex].themeColor),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(slides[activeSlideIndex].themeColor.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = slides[activeSlideIndex].icon,
                        contentDescription = null,
                        tint = slides[activeSlideIndex].themeColor,
                        modifier = Modifier.size(54.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = slides[activeSlideIndex].title,
                    color = GhostWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = slides[activeSlideIndex].desc,
                    color = GhostWhite.copy(alpha = 0.8f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }

        // Indicator dots & button row
        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Indicator row
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                slides.forEachIndexed { idx, slide ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(height = 8.dp, width = if (activeSlideIndex == idx) 24.dp else 8.dp)
                            .background(
                                color = if (activeSlideIndex == idx) slide.themeColor else MutedSlate.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }

            // Nav actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (activeSlideIndex > 0) {
                    TextButton(onClick = { activeSlideIndex-- }) {
                        Text("BACK", color = MutedSlate, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Spacer(modifier = Modifier.width(60.dp))
                }

                Button(
                    onClick = {
                        if (activeSlideIndex < slides.size - 1) {
                            activeSlideIndex++
                        } else {
                            viewModel.navigateTo(Screen.Registration)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = slides[activeSlideIndex].themeColor),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.width(140.dp)
                ) {
                    Text(
                        text = if (activeSlideIndex == slides.size - 1) "FOUND ACCOUNT" else "NEXT",
                        fontWeight = FontWeight.Bold,
                        color = CharcoalObsidian
                    )
                }
            }
        }
    }
}

// ============================================================================
// 3. REGISTRATION VIEW (Noble Dark Symmetrical Form + OTP Verification)
// ============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationView(viewModel: AppViewModel) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var dob by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Male") }
    var selectedFlag by remember { mutableStateOf("🇮🇳") }

    val isEmailValid = remember(email) {
        email.isEmpty() || email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"))
    }

    // OTP verification modal state
    var showOtpDialog by remember { mutableStateOf(false) }
    var inputOtp by remember { mutableStateOf("") }
    var isVerified by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Icon(
            imageVector = Icons.Default.AccountBalance,
            contentDescription = null,
            tint = RegalGold,
            modifier = Modifier.size(56.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Create Citizen Dossier",
            color = GhostWhite,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Secure your place in the planetary ledger",
            color = MutedSlate,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        // Symmetrical Gold Styled Inputs
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Legal Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = RegalGold) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RegalGold,
                unfocusedBorderColor = MutedSlate.copy(alpha = 0.5f),
                focusedLabelColor = RegalGold,
                unfocusedLabelColor = MutedSlate,
                focusedTextColor = GhostWhite,
                unfocusedTextColor = GhostWhite
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Unique Handle (@username)") },
            leadingIcon = { Icon(Icons.Default.AlternateEmail, contentDescription = null, tint = RegalGold) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RegalGold,
                unfocusedBorderColor = MutedSlate.copy(alpha = 0.5f),
                focusedLabelColor = RegalGold,
                unfocusedLabelColor = MutedSlate,
                focusedTextColor = GhostWhite,
                unfocusedTextColor = GhostWhite
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Secure Email Address") },
            leadingIcon = { Icon(Icons.Default.Mail, contentDescription = null, tint = RegalGold) },
            isError = !isEmailValid,
            supportingText = if (!isEmailValid) {
                { Text("Please enter a complete email address (e.g., citizen@oneearth.io)") }
            } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RegalGold,
                unfocusedBorderColor = MutedSlate.copy(alpha = 0.5f),
                focusedLabelColor = RegalGold,
                unfocusedLabelColor = MutedSlate,
                focusedTextColor = GhostWhite,
                unfocusedTextColor = GhostWhite,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error
            ),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = dob,
            onValueChange = { newValue ->
                val digitsAndDashes = newValue.filter { it.isDigit() || it == '-' }
                if (digitsAndDashes.length <= 10) {
                    dob = digitsAndDashes
                }
            },
            label = { Text("Date of Birth (YYYY-MM-DD)") },
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = RegalGold) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RegalGold,
                unfocusedBorderColor = MutedSlate.copy(alpha = 0.5f),
                focusedLabelColor = RegalGold,
                unfocusedLabelColor = MutedSlate,
                focusedTextColor = GhostWhite,
                unfocusedTextColor = GhostWhite
            ),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // Gender Selection Segment
        Text(
            text = "Gender Identity",
            style = androidx.compose.ui.text.TextStyle(
                color = MutedSlate,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp).align(Alignment.Start)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val genderOptions = listOf("Male", "Female", "Other")
            genderOptions.forEach { option ->
                val isSelected = selectedGender == option
                val emoji = when (option) {
                    "Male" -> "♂️"
                    "Female" -> "♀️"
                    else -> "✨"
                }
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) RegalGold.copy(alpha = 0.15f) else CharcoalObsidian
                    ),
                    border = BorderStroke(
                        width = 1.5.dp,
                        color = if (isSelected) RegalGold else MutedSlate.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clickable { selectedGender = option }
                        .testTag("gender_option_$option")
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(emoji, modifier = Modifier.padding(end = 4.dp), fontSize = 14.sp)
                            Text(
                                text = option,
                                color = if (isSelected) RegalGold else GhostWhite,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("System Entry Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = RegalGold) },
            trailingIcon = {
                val icon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(icon, contentDescription = "Toggle password visibility", tint = RegalGold)
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RegalGold,
                unfocusedBorderColor = MutedSlate.copy(alpha = 0.5f),
                focusedLabelColor = RegalGold,
                unfocusedLabelColor = MutedSlate,
                focusedTextColor = GhostWhite,
                unfocusedTextColor = GhostWhite
            ),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // Territory / Country Flag Selection
        Text(
            text = "Broadcast Territory Flag",
            style = androidx.compose.ui.text.TextStyle(
                color = MutedSlate,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp).align(Alignment.Start)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val flags = listOf(
                Pair("🇮🇳", "India"),
                Pair("🇫🇷", "France"),
                Pair("🇰🇪", "Kenya"),
                Pair("🇧🇷", "Brazil"),
                Pair("🇯🇵", "Japan"),
                Pair("🇺🇸", "US")
            )
            flags.forEach { pair ->
                val isSelected = selectedFlag == pair.first
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) RegalGold.copy(alpha = 0.15f) else CharcoalObsidian
                    ),
                    border = BorderStroke(
                        width = 1.5.dp,
                        color = if (isSelected) RegalGold else MutedSlate.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clickable { selectedFlag = pair.first }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(pair.first, fontSize = 20.sp)
                        Text(pair.second, color = if (isSelected) RegalGold else MutedSlate, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Activation Button (One Word)
        Button(
            onClick = {
                if (name.isBlank() || username.isBlank() || email.isBlank() || dob.isBlank() || password.isBlank()) {
                    return@Button
                }
                viewModel.tempName = name
                viewModel.tempUsername = username
                viewModel.tempEmail = email
                viewModel.tempDob = dob
                viewModel.tempGender = selectedGender
                viewModel.selectedFlag = selectedFlag
                viewModel.selectedTerritory = when (selectedFlag) {
                    "🇮🇳" -> "India"
                    "🇫🇷" -> "France"
                    "🇰🇪" -> "Kenya"
                    "🇧🇷" -> "Brazil"
                    "🇯🇵" -> "Japan"
                    "🇺🇸" -> "United States"
                    else -> "India"
                }
                showOtpDialog = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = RegalGold),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = name.isNotBlank() && username.isNotBlank() && email.isNotBlank() && isEmailValid && email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"))
        ) {
            Text(
                "Register",
                fontWeight = FontWeight.Bold,
                color = CharcoalObsidian,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already registered? ", color = MutedSlate, fontSize = 12.sp)
            Text(
                "Login", 
                color = RegalGold, 
                fontSize = 12.sp, 
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { viewModel.navigateTo(Screen.Login) }
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Identity Verification Dialog Modal
        if (showOtpDialog) {
            Dialog(onDismissRequest = { showOtpDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = VelvetCard),
                    border = BorderStroke(1.dp, RegalGold),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = RegalGold,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Asynchronous OTP Gateway",
                            color = GhostWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "A simulated cryptographic code has been sent to your email to verify account validity.",
                            color = MutedSlate,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )

                        Text(
                            "Enter Gateway Key: '1234'",
                            color = RegalGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = inputOtp,
                            onValueChange = { inputOtp = it },
                            placeholder = { Text("Code") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RegalGold,
                                focusedTextColor = GhostWhite,
                                unfocusedTextColor = GhostWhite
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.width(130.dp)
                        )

                        if (hasError) {
                            Text(
                                "Invalid Cryptographic Key. Try '1234'",
                                color = CrimsonRep,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            TextButton(onClick = { showOtpDialog = false }) {
                                Text("CANCEL", color = MutedSlate)
                            }

                            Button(
                                onClick = {
                                    if (inputOtp == "1234") {
                                        isVerified = true
                                        showOtpDialog = false
                                        viewModel.navigateTo(Screen.TerritorySelection)
                                    } else {
                                        hasError = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RegalGold)
                            ) {
                                Text("VALIDATE", color = CharcoalObsidian)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ============================================================================
// 3B. LOGIN SYSTEM (Noble Dark Symmetrical Form + Symmetrical Profiles)
// ============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(viewModel: AppViewModel) {
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Icon(
            imageVector = Icons.Default.AccountBalance,
            contentDescription = null,
            tint = RegalGold,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome Back, Citizen",
            color = GhostWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Sign in to synchronize your merit index",
            color = MutedSlate,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
        )

        // Symmetrical Gold Styled Inputs
        OutlinedTextField(
            value = identifier,
            onValueChange = { identifier = it },
            label = { Text("Email Address or Handle") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = RegalGold) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RegalGold,
                unfocusedBorderColor = MutedSlate.copy(alpha = 0.5f),
                focusedLabelColor = RegalGold,
                unfocusedLabelColor = MutedSlate,
                focusedTextColor = GhostWhite,
                unfocusedTextColor = GhostWhite
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Account Passphrase") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = RegalGold) },
            trailingIcon = {
                val icon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(icon, contentDescription = "Toggle password visibility", tint = RegalGold)
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RegalGold,
                unfocusedBorderColor = MutedSlate.copy(alpha = 0.5f),
                focusedLabelColor = RegalGold,
                unfocusedLabelColor = MutedSlate,
                focusedTextColor = GhostWhite,
                unfocusedTextColor = GhostWhite
            ),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )

        // Login Action Button (One Word)
        Button(
            onClick = {
                if (identifier.isBlank()) {
                    Toast.makeText(context, "Please enter your legal identifier.", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                
                viewModel.performLogin(identifier) { success ->
                    if (success) {
                        Toast.makeText(context, "Merit index synchronized!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Access authorized for new session.", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = RegalGold),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = identifier.isNotBlank()
        ) {
            Text(
                "Login",
                fontWeight = FontWeight.Bold,
                color = CharcoalObsidian,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("New to the Empire? ", color = MutedSlate, fontSize = 12.sp)
            Text(
                "Register", 
                color = RegalGold, 
                fontSize = 12.sp, 
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { viewModel.navigateTo(Screen.Registration) }
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

// ============================================================================
// 4. INTERACTIVE TERRITORY MAP SELECTION
// ============================================================================
@Composable
fun TerritorySelectionView(viewModel: AppViewModel) {
    val territories = listOf(
        TerritoryItem("Territory of India", "🇮🇳", "Asia-Pacific", "1.4 Billion", VelvetCard),
        TerritoryItem("Territory of France", "🇫🇷", "Europe", "67 Million", VelvetCard),
        TerritoryItem("Territory of Kenya", "🇰🇪", "East Africa", "53 Million", VelvetCard),
        TerritoryItem("Territory of Brazil", "🇧🇷", "South America", "214 Million", VelvetCard),
        TerritoryItem("Territory of Japan", "🇯🇵", "East Asia", "125 Million", VelvetCard),
        TerritoryItem("Territory of United States", "🇺🇸", "North America", "330 Million", VelvetCard)
    )

    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Icon(
            imageVector = Icons.Default.Public,
            contentDescription = null,
            tint = RegalGold,
            modifier = Modifier.size(54.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Geopolitical Alignment",
            color = GhostWhite,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Select your home territory. In One Earth, all territories are equal and united under one Empire.",
            color = MutedSlate,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        // Territory Option Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(territories.size) { index ->
                val territory = territories[index]
                val isSelected = selectedIndex == index
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clickable {
                            selectedIndex = index
                            viewModel.selectedTerritory = territory.name.replace("Territory of ", "")
                            viewModel.selectedFlag = territory.flag
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) RegalGold.copy(alpha = 0.15f) else VelvetCard
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) RegalGold else MutedSlate.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = territory.flag,
                                fontSize = 36.sp
                            )
                            if (isSelected) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = RegalGold,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = territory.name,
                                color = GhostWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Pop: ${territory.population}",
                                color = MutedSlate,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (selectedIndex != null) {
                    viewModel.navigateTo(Screen.PersonalitySetup)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = RegalGold),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = selectedIndex != null
        ) {
            Text(
                "Select",
                color = CharcoalObsidian,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

// ============================================================================
// 5. PERSONALITY MATRIX SETUP
// ============================================================================
@Composable
fun PersonalitySetupView(viewModel: AppViewModel) {
    val traitsAvailable = listOf(
        "Explorer", "Creator", "Teacher", "Leader", "Scientist",
        "Artist", "Philosopher", "Builder", "Visionary", "Humanitarian"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Icon(
            imageVector = Icons.Default.FilterVintage,
            contentDescription = null,
            tint = RegalGold,
            modifier = Modifier.size(54.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Personality Alignment",
            color = GhostWhite,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "You must select exactly five (5) defining traits to complete your profile structure.",
            color = MutedSlate,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
        )

        // Trait Selected counter
        Card(
            colors = CardDefaults.cardColors(containerColor = VelvetCard),
            border = BorderStroke(1.dp, RegalGold),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = RegalGold, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Selected Matrix: ${viewModel.selectedTraitsList.size} / 5",
                    color = GhostWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Large select options
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(traitsAvailable) { trait ->
                val isSelected = viewModel.selectedTraitsList.contains(trait)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isSelected) {
                                viewModel.selectedTraitsList.remove(trait)
                            } else {
                                if (viewModel.selectedTraitsList.size < 5) {
                                    viewModel.selectedTraitsList.add(trait)
                                }
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) RegalGold.copy(alpha = 0.15f) else VelvetCard
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) RegalGold else MutedSlate.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = trait,
                            color = GhostWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) RegalGold else MutedSlate,
                                    shape = CircleShape
                                )
                                .background(
                                    color = if (isSelected) RegalGold else Color.Transparent,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = CharcoalObsidian,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (viewModel.selectedTraitsList.size == 5) {
                    viewModel.navigateTo(Screen.CitizenOath)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = RegalGold),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = viewModel.selectedTraitsList.size == 5
        ) {
            Text(
                "Lock",
                color = CharcoalObsidian,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

// ============================================================================
// 6. THE CITIZEN OATH
// ============================================================================
@Composable
fun CitizenOathView(viewModel: AppViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Gavel,
                contentDescription = null,
                tint = RegalGold,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "The Covenant Oath",
                color = GhostWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Text(
                text = "Every citizen must bind themselves to the values of our digital civilization prior to entry.",
                color = MutedSlate,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // Oath Text Box Panel
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(2.dp, RegalGold),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.FormatQuote,
                        contentDescription = null,
                        tint = RegalGold.copy(alpha = 0.5f),
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "\"I belong to One Earth and One Family. I shall respect all citizens. I shall contribute positively. I shall seek knowledge and truth. I shall help build a better future.\"",
                        color = GhostWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )
                }
            }
        }

        // Action Entrance Button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        ) {
            Text(
                "By tapping below, your signature is permanently sealed",
                color = MutedSlate,
                fontSize = 11.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Button(
                onClick = { viewModel.completeOnboarding() },
                colors = ButtonDefaults.buttonColors(containerColor = RegalGold),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, LightGold),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Accept",
                    fontWeight = FontWeight.Bold,
                    color = CharcoalObsidian,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

// ============================================================================
// 7. MAIN DASHBOARD VIEW (With Top bar stats, 5 tabs, dialogs)
// ============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboardView(viewModel: AppViewModel, snackbarHostState: SnackbarHostState) {
    val currentTab by viewModel.currentTab.collectAsState()
    val me by viewModel.currentUserFlow.collectAsState()
    val selectedId by viewModel.selectedRoomId.collectAsState()
    val commentsPostId by viewModel.selectedCommentsPostId.collectAsState()
    val selectedProfileUser by viewModel.selectedProfileUser.collectAsState()

    val showWelcome by viewModel.showDailyWelcome.collectAsState()
    val showExit by viewModel.showExitSummary.collectAsState()

    Scaffold(
        containerColor = DeepOceanSapphire,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left Identity
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                viewModel.selectTab(DashboardTab.ProfileAndElections)
                                viewModel.setShowEditProfileDialog(true)
                            }
                        ) {
                            Text(
                                text = me?.flagEmoji ?: "🌍",
                                fontSize = 28.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = me?.name ?: "Noble Citizen",
                                    color = GhostWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = RegalGold.copy(alpha = 0.15f)),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "${me?.currentRank ?: "Citizen"} Rank",
                                        color = RegalGold,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // Right Metrics counters
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // KC
                            Row(
                                modifier = Modifier
                                    .background(CharcoalObsidian, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🧠 ", fontSize = 12.sp)
                                Text(
                                    "${me?.knowledgeCredits ?: 0} KC",
                                    color = GhostWhite,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            // CC
                            Row(
                                modifier = Modifier
                                    .background(CharcoalObsidian, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🤝 ", fontSize = 12.sp)
                                Text(
                                    "${me?.contributionCredits ?: 0} CC",
                                    color = RegalGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            // Royal Welcome Trigger Bell
                            IconButton(onClick = { viewModel.showWelcomeDialog() }) {
                                Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = RegalGold)
                            }

                            // Top Right 3-dot Dropdown Menu with Theme Toggle & Logout
                            var showMenu by remember { mutableStateOf(false) }
                            Box {
                                IconButton(onClick = { showMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Menu",
                                        tint = RegalGold
                                    )
                                }
                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false },
                                    modifier = Modifier.background(VelvetCard)
                                ) {
                                    val currentTheme = viewModel.themeMode.collectAsState().value
                                    val themeLabel = when (currentTheme) {
                                        AppThemeMode.TWILIGHT_DUSK -> "Theme: Twilight Dusk 🌅"
                                        AppThemeMode.SPACE_ABYSS_DARK -> "Theme: Space Abyss 🌌"
                                        AppThemeMode.MINIMALIST_SLATE_LIGHT -> "Theme: Slate Light ☀️"
                                    }
                                    val themeIcon = when (currentTheme) {
                                        AppThemeMode.TWILIGHT_DUSK -> Icons.Default.LightMode
                                        AppThemeMode.SPACE_ABYSS_DARK -> Icons.Default.DarkMode
                                        AppThemeMode.MINIMALIST_SLATE_LIGHT -> Icons.Default.LightMode
                                    }
                                    DropdownMenuItem(
                                        text = { Text(themeLabel, color = GhostWhite) },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = themeIcon,
                                                contentDescription = "Theme Toggle",
                                                tint = RegalGold
                                            )
                                        },
                                        onClick = {
                                            showMenu = false
                                            viewModel.toggleTheme()
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Logout", color = CrimsonRep) },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Logout,
                                                contentDescription = "Logout",
                                                tint = CrimsonRep
                                            )
                                        },
                                        onClick = {
                                            showMenu = false
                                            viewModel.showExitDialog()
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VelvetCard
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = VelvetCard,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == DashboardTab.PublicSquare,
                    onClick = { viewModel.selectTab(DashboardTab.PublicSquare) },
                    icon = { Icon(Icons.Default.Feed, contentDescription = null) },
                    label = { Text("Feed", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RegalGold,
                        selectedTextColor = RegalGold,
                        unselectedIconColor = MutedSlate,
                        unselectedTextColor = MutedSlate,
                        indicatorColor = RegalGold.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = currentTab == DashboardTab.KnowledgeArena,
                    onClick = { viewModel.selectTab(DashboardTab.KnowledgeArena) },
                    icon = { Icon(Icons.Default.School, contentDescription = null) },
                    label = { Text("Arena", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RegalGold,
                        selectedTextColor = RegalGold,
                        unselectedIconColor = MutedSlate,
                        unselectedTextColor = MutedSlate,
                        indicatorColor = RegalGold.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = currentTab == DashboardTab.Messaging,
                    onClick = { viewModel.selectTab(DashboardTab.Messaging) },
                    icon = { Icon(Icons.Default.QuestionAnswer, contentDescription = null) },
                    label = { Text("Chats", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RegalGold,
                        selectedTextColor = RegalGold,
                        unselectedIconColor = MutedSlate,
                        unselectedTextColor = MutedSlate,
                        indicatorColor = RegalGold.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = currentTab == DashboardTab.ProfileAndElections,
                    onClick = { viewModel.selectTab(DashboardTab.ProfileAndElections) },
                    icon = { Icon(Icons.Default.HowToVote, contentDescription = null) },
                    label = { Text("Elections", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RegalGold,
                        selectedTextColor = RegalGold,
                        unselectedIconColor = MutedSlate,
                        unselectedTextColor = MutedSlate,
                        indicatorColor = RegalGold.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = currentTab == DashboardTab.MissionsAndLegends,
                    onClick = { viewModel.selectTab(DashboardTab.MissionsAndLegends) },
                    icon = { Icon(Icons.Default.WorkspacePremium, contentDescription = null) },
                    label = { Text("Missions", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RegalGold,
                        selectedTextColor = RegalGold,
                        unselectedIconColor = MutedSlate,
                        unselectedTextColor = MutedSlate,
                        indicatorColor = RegalGold.copy(alpha = 0.1f)
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (currentTab) {
                DashboardTab.PublicSquare -> PublicSquareTab(viewModel)
                DashboardTab.KnowledgeArena -> KnowledgeArenaTab(viewModel)
                DashboardTab.Messaging -> MessagingTab(viewModel)
                DashboardTab.ProfileAndElections -> ElectionsAndProfileTab(viewModel)
                DashboardTab.MissionsAndLegends -> MissionsTab(viewModel)
            }

            // Floating logout button removed to prevent overlap with send/input fields. Access via top-right 3-dot menu.
        }
    }

    // Modal: The Daily Royal Welcome Notification
    if (showWelcome) {
        Dialog(onDismissRequest = { viewModel.dismissWelcomeDialog() }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(1.5.dp, RegalGold),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = RegalGold, modifier = Modifier.size(54.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "The Daily Royal Welcome",
                        color = GhostWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Good Evening, Citizen.\n\nToday your profile received 14 new visits, you accumulated 32 Knowledge Credits, and your Territory rose to Rank #3 worldwide.\n\nThe general elections commence in 2 days.",
                        color = GhostWhite.copy(alpha = 0.85f),
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.dismissWelcomeDialog() },
                        colors = ButtonDefaults.buttonColors(containerColor = RegalGold)
                    ) {
                        Text("Honor", color = CharcoalObsidian, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Modal: The Exit Summary
    if (showExit) {
        Dialog(onDismissRequest = { viewModel.dismissExitDialog() }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(1.5.dp, RegalGold),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Timeline, contentDescription = null, tint = RegalGold, modifier = Modifier.size(54.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Daily Progress Ledger",
                        color = GhostWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Real-time Reflection Analytics:\n\n+32 KC (Knowledge Credits)\n+15 CC (Contribution Credits)\n\nRank Progress: +7% closer to Contributor Tier!\n\nReturn tomorrow to unlock your next Imperial Mission.",
                        color = GhostWhite.copy(alpha = 0.85f),
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        TextButton(onClick = { viewModel.dismissExitDialog() }) {
                            Text("Stay", color = MutedSlate)
                        }
                        Button(
                            onClick = {
                                viewModel.dismissExitDialog()
                                viewModel.navigateTo(Screen.Splash)
                            },
                            colors = ButtonColors(
                                containerColor = CrimsonRep,
                                contentColor = GhostWhite,
                                disabledContainerColor = MutedSlate,
                                disabledContentColor = MutedSlate
                            )
                        ) {
                            Text("Logout", color = GhostWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    selectedProfileUser?.let { selectedUser ->
        ProfileDisplayDialog(user = selectedUser, viewModel = viewModel, onClose = { viewModel.closeProfileDialog() })
    }
}

@Composable
fun ProfileDisplayDialog(user: UserEntity, viewModel: AppViewModel, onClose: () -> Unit) {
    var isEditingProfile by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(user.name) }
    var editUsername by remember { mutableStateOf(user.username.removePrefix("@")) }
    var editBio by remember { mutableStateOf(user.bio) }
    var editTerritory by remember { mutableStateOf(user.territory) }
    var editFlagEmoji by remember { mutableStateOf(user.flagEmoji) }

    Dialog(onDismissRequest = onClose) {
        Card(
            colors = CardDefaults.cardColors(containerColor = VelvetCard),
            border = BorderStroke(1.5.dp, RegalGold),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(CharcoalObsidian)
                            .border(1.5.dp, RegalGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (editName.isNotBlank()) editName.take(1).uppercase() else user.name.take(1).uppercase(),
                            color = RegalGold,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 10.dp)
                            .background(CharcoalObsidian, CircleShape)
                            .border(1.dp, RegalGold.copy(alpha = 0.5f), CircleShape)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(editFlagEmoji, fontSize = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isEditingProfile) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Display Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GhostWhite,
                            unfocusedTextColor = GhostWhite,
                            focusedBorderColor = RegalGold,
                            unfocusedBorderColor = MutedSlate
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = editUsername,
                        onValueChange = { editUsername = it },
                        label = { Text("Handle (Username)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GhostWhite,
                            unfocusedTextColor = GhostWhite,
                            focusedBorderColor = RegalGold,
                            unfocusedBorderColor = MutedSlate
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text("Mission Bio") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GhostWhite,
                            unfocusedTextColor = GhostWhite,
                            focusedBorderColor = RegalGold,
                            unfocusedBorderColor = MutedSlate
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = editTerritory,
                        onValueChange = { editTerritory = it },
                        label = { Text("Territory Representation") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GhostWhite,
                            unfocusedTextColor = GhostWhite,
                            focusedBorderColor = RegalGold,
                            unfocusedBorderColor = MutedSlate
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = editFlagEmoji,
                        onValueChange = { editFlagEmoji = it },
                        label = { Text("Flag Emoji") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GhostWhite,
                            unfocusedTextColor = GhostWhite,
                            focusedBorderColor = RegalGold,
                            unfocusedBorderColor = MutedSlate
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { isEditingProfile = false },
                            colors = ButtonDefaults.buttonColors(containerColor = MutedSlate),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("CANCEL", color = GhostWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = {
                                viewModel.updateUserProfile(
                                    name = editName,
                                    username = editUsername,
                                    bio = editBio,
                                    territory = editTerritory,
                                    flagEmoji = editFlagEmoji,
                                    profilePhoto = ""
                                )
                                isEditingProfile = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RegalGold),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Save", color = CharcoalObsidian, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Text(
                        text = user.name,
                        color = GhostWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Row(
                        modifier = Modifier.padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = user.username,
                            color = MutedSlate,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        val genderEmoji = when (user.gender) {
                            "Male" -> "♂️"
                            "Female" -> "♀️"
                            else -> "✨"
                        }
                        Box(
                            modifier = Modifier
                                .background(MutedSlate.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = genderEmoji, fontSize = 10.sp)
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(text = user.gender.uppercase(), color = GhostWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = RegalGold.copy(alpha = 0.15f)),
                        border = BorderStroke(0.5.dp, RegalGold),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = "${user.currentRank} Tier",
                            color = RegalGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "KC (Knowledge)", color = MutedSlate, fontSize = 10.sp)
                            Text(
                                text = "${user.knowledgeCredits}",
                                color = ElectricBlue,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "CC (Contribution)", color = MutedSlate, fontSize = 10.sp)
                            Text(
                                text = "${user.contributionCredits}",
                                color = LustrousAmber,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Reputation", color = MutedSlate, fontSize = 10.sp)
                            Text(
                                text = "${user.reputationScore}%",
                                color = EmeraldSuccess,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Paradigm Bio:",
                        color = RegalGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Text(
                        text = user.bio,
                        color = GhostWhite.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(top = 4.dp, bottom = 12.dp)
                    )

                    if (user.isCandidate) {
                        Divider(color = MutedSlate.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = "Sovereign Campaign Vision:",
                            color = RegalGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Text(
                            text = user.campaignVision,
                            color = GhostWhite,
                            fontSize = 11.sp,
                            modifier = Modifier.align(Alignment.Start).padding(top = 2.dp)
                        )
                    }

                    if (user.id == "me") {
                        Button(
                            onClick = { isEditingProfile = true },
                            colors = ButtonDefaults.buttonColors(containerColor = RegalGold.copy(alpha = 0.15f)),
                            border = BorderStroke(1.dp, RegalGold),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = RegalGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("EDIT PERSONAL PROFILE", color = RegalGold, fontSize = 11.sp)
                        }
                    }
                }

                val posts by viewModel.rankedPosts.collectAsState()
                val userPosts = posts.filter { it.authorName == user.name || it.authorId == user.id || (user.id == "me" && it.authorId == "me") }

                Divider(color = MutedSlate.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 12.dp))
                Text(
                    text = "Broadcast Archives:",
                    color = RegalGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )

                if (userPosts.isEmpty()) {
                    Text(
                        text = "No broadcast entries verified on the scroll ledger.",
                        color = MutedSlate,
                        fontSize = 11.sp,
                        modifier = Modifier.align(Alignment.Start).padding(vertical = 8.dp)
                    )
                } else {
                    userPosts.forEach { post ->
                        var isEditingPost by remember { mutableStateOf(false) }
                        var editedContent by remember { mutableStateOf(post.content) }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = CharcoalObsidian),
                            border = BorderStroke(0.5.dp, MutedSlate.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = post.category, color = RegalGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)

                                    if (user.id == "me") {
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            IconButton(
                                                onClick = { isEditingPost = !isEditingPost },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.Edit, contentDescription = "Edit Post", tint = RegalGold, modifier = Modifier.size(14.dp))
                                            }
                                            IconButton(
                                                onClick = { viewModel.deletePost(post.id) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "Delete Post", tint = CrimsonRep, modifier = Modifier.size(14.dp))
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                if (isEditingPost) {
                                    OutlinedTextField(
                                        value = editedContent,
                                        onValueChange = { editedContent = it },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = GhostWhite,
                                            unfocusedTextColor = GhostWhite,
                                            focusedBorderColor = RegalGold,
                                            unfocusedBorderColor = MutedSlate
                                        ),
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        TextButton(onClick = { isEditingPost = false }) {
                                            Text("CANCEL", color = MutedSlate, fontSize = 9.sp)
                                        }
                                        TextButton(onClick = {
                                            viewModel.updatePostContent(post.id, editedContent)
                                            isEditingPost = false
                                        }) {
                                            Text("SAVE", color = RegalGold, fontSize = 9.sp)
                                        }
                                    }
                                } else {
                                    Text(text = post.content, color = GhostWhite, fontSize = 11.sp, lineHeight = 15.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onClose,
                    colors = ButtonDefaults.buttonColors(containerColor = RegalGold),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(42.dp)
                ) {
                    Text("CLOSE PARADIGM", color = CharcoalObsidian, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ============================================================================
// TAB 1: PUBLIC SQUARE (Home feed with merit reactions, Custom composer)
// ============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicSquareTab(viewModel: AppViewModel) {
    val posts by viewModel.rankedPosts.collectAsState()
    var postContent by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Inquiry") }

    val commentsPostId by viewModel.selectedCommentsPostId.collectAsState()
    val commentsList by viewModel.currentPostComments.collectAsState()
    var commentText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Merit ranked Scroll feed
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(posts) { post ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = VelvetCard),
                    border = BorderStroke(0.5.dp, MutedSlate.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Post author identity block
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable {
                                    viewModel.showProfileForUserByName(post.authorName, post.authorFlag, post.authorRank, post.authorTerritory)
                                }
                            ) {
                                Text(post.authorFlag, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "${post.authorName} (${post.authorUsername})",
                                        color = GhostWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Row {
                                        Text(
                                            text = post.authorRank,
                                            color = RegalGold,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Reputation: ${post.reputationImpact}%",
                                            color = EmeraldSuccess,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            // Category badge tag
                            Card(
                                colors = CardDefaults.cardColors(containerColor = RegalGold.copy(alpha = 0.1f)),
                                border = BorderStroke(0.5.dp, RegalGold),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = post.category,
                                    color = RegalGold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        // Content
                        Text(
                            text = post.content,
                            color = GhostWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )

                        // Custom Reactions row (Rejects generic dopamine likes)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CharcoalObsidian.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Wise - adds KC to author
                            ReactionButton(
                                iconText = "🧠 Wise",
                                count = post.knowledgeValue,
                                active = post.reactedWiseUsers.split(",").contains("me"),
                                color = RegalGold,
                                onClick = { viewModel.reactToPost(post.id, "Wise") }
                            )

                            // Helpful - adds CC to author
                            ReactionButton(
                                iconText = "🤝 Helpful",
                                count = post.contributionProof,
                                active = post.reactedHelpfulUsers.split(",").contains("me"),
                                color = LustrousAmber,
                                onClick = { viewModel.reactToPost(post.id, "Helpful") }
                            )

                            // Inspiring - elevates reputation score % of author
                            ReactionButton(
                                iconText = "✨ Inspiring",
                                count = (post.reputationImpact - 90).coerceAtLeast(0),
                                active = post.reactedInspiringUsers.split(",").contains("me"),
                                color = ElectricBlue,
                                onClick = { viewModel.reactToPost(post.id, "Inspiring") }
                            )

                            // Comment trigger
                            IconButton(onClick = { viewModel.selectPostForComments(post.id) }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Comment, contentDescription = "Comments", tint = MutedSlate, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Chat", fontSize = 11.sp, color = MutedSlate)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick composer panel
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = VelvetCard),
            border = BorderStroke(1.dp, RegalGold.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Broadcast Entry to Public Square",
                    color = GhostWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = postContent,
                    onValueChange = { postContent = it },
                    placeholder = { Text("Draft long-form inquiry, article or societal debate...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RegalGold,
                        unfocusedBorderColor = MutedSlate.copy(alpha = 0.3f),
                        focusedTextColor = GhostWhite,
                        unfocusedTextColor = GhostWhite
                    ),
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category selector row
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Inquiry", "Article", "Debate").forEach { cat ->
                            val isSel = cat == selectedCategory
                            Card(
                                modifier = Modifier.clickable { selectedCategory = cat },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSel) RegalGold else MutedSlate.copy(alpha = 0.2f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = cat,
                                    color = if (isSel) CharcoalObsidian else GhostWhite,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    // Send Button
                    IconButton(
                        onClick = {
                            if (postContent.isNotBlank()) {
                                viewModel.createPost(postContent, selectedCategory)
                                postContent = ""
                            }
                        },
                        modifier = Modifier
                            .background(RegalGold, CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = CharcoalObsidian, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }

    // Modal comments sliding tray
    if (commentsPostId != null) {
        Dialog(onDismissRequest = { viewModel.selectPostForComments(null) }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(1.dp, RegalGold),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Public Dialogue Comments",
                        color = GhostWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nested list of comment bodies
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (commentsList.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No nested comments. Be the first to add a wise inquiry.", color = MutedSlate, fontSize = 12.sp, textAlign = TextAlign.Center)
                                }
                            }
                        } else {
                            items(commentsList) { comment ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = CharcoalObsidian.copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.clickable {
                                                viewModel.showProfileForUserByName(comment.authorName, comment.authorFlag, comment.authorRank)
                                            }
                                        ) {
                                            Text(comment.authorFlag, fontSize = 16.sp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(comment.authorName, color = RegalGold, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(comment.authorRank, color = MutedSlate, fontSize = 9.sp)
                                        }
                                        Text(comment.content, color = GhostWhite, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp))
                                    }
                                }
                            }
                        }
                    }

                    // Keyboard input row
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            placeholder = { Text("Add constructive comment...") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = GhostWhite,
                                unfocusedTextColor = GhostWhite,
                                focusedBorderColor = RegalGold,
                                unfocusedBorderColor = MutedSlate
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        IconButton(
                            onClick = {
                                val trimmedMsg = commentText.trim()
                                if (trimmedMsg.isNotBlank()) {
                                    commentsPostId?.let { postId ->
                                        viewModel.addComment(postId, trimmedMsg)
                                    }
                                    commentText = ""
                                    viewModel.selectPostForComments(null) // Close public comment dialog on send
                                }
                            },
                            modifier = Modifier
                                .background(RegalGold, CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Publish", tint = CharcoalObsidian, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReactionButton(
    iconText: String,
    count: Int,
    active: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (active) color.copy(alpha = 0.15f) else Color.Transparent
        ),
        border = BorderStroke(0.5.dp, if (active) color else Color.Transparent)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$iconText ($count)",
                color = if (active) color else MutedSlate,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ============================================================================
// TAB 2: KNOWLEDGE ARENA (Interactive System Trivia Exams)
// ============================================================================
@Composable
fun KnowledgeArenaTab(viewModel: AppViewModel) {
    val currentQuestionIdx by viewModel.currentQuizIndex.collectAsState()
    val isQuizCompleted by viewModel.quizCompleted.collectAsState()
    val selectedAnswers by viewModel.selectedAnswers.collectAsState()

    val currentQuestion = viewModel.quizQuestions[currentQuestionIdx]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Topic Title Badge
        Card(
            colors = CardDefaults.cardColors(containerColor = VelvetCard),
            border = BorderStroke(1.dp, RegalGold),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.School, contentDescription = null, tint = RegalGold, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Imperial Evaluation Hub",
                    color = GhostWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isQuizCompleted) {
            Text(
                "Verify Domain Excellence",
                color = GhostWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                "Earn Knowledge Credits (KC) by responding accurately below.",
                color = MutedSlate,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Current question panel
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(1.5.dp, RegalGold),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        // Question Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Question ${currentQuestionIdx + 1} of ${viewModel.quizQuestions.size}",
                                color = RegalGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Card(
                                colors = CardDefaults.cardColors(containerColor = ElectricBlue.copy(alpha = 0.2f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = currentQuestion.subject,
                                    color = ElectricBlue,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Question Text
                        Text(
                            text = currentQuestion.question,
                            color = GhostWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 24.sp
                        )
                    }

                    // Answer Options Column
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        currentQuestion.options.forEachIndexed { optionIdx, option ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.answerQuizQuestion(currentQuestion.id, optionIdx)
                                    },
                                colors = CardDefaults.cardColors(containerColor = CharcoalObsidian.copy(alpha = 0.5f)),
                                border = BorderStroke(1.dp, MutedSlate.copy(alpha = 0.3f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .border(1.dp, RegalGold, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = ('A' + optionIdx).toString(),
                                            color = RegalGold,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = option,
                                        color = GhostWhite,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Exam success congratulations panel
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 40.dp),
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(2.dp, RegalGold),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.School, contentDescription = null, tint = RegalGold, modifier = Modifier.size(72.dp))
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Evaluation Successful!",
                        color = GhostWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Your domain expertise has been recorded in the open ledger. You have completed this evaluation loop and boosted your territory's global intellectual ranking.",
                        color = MutedSlate,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    Button(
                        onClick = { viewModel.resetQuiz() },
                        colors = ButtonDefaults.buttonColors(containerColor = RegalGold)
                    ) {
                        Text("LAUNCH NEW PRACTICE MODULE", color = CharcoalObsidian, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ============================================================================
// TAB 3: MERCANTILE CHATS (The Three-Connection Rule direct messenger)
// ============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagingTab(viewModel: AppViewModel) {
    val activeRooms by viewModel.activeChatRooms.collectAsState()
    val waitingRooms by viewModel.waitingChatRooms.collectAsState()
    val selectedId by viewModel.selectedRoomId.collectAsState()
    val messagesList by viewModel.currentChatMessages.collectAsState()

    var chatMessageText by remember { mutableStateOf("") }

    if (selectedId == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Connection Limitation Warning Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(1.dp, LustrousAmber),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.SafetyCheck, contentDescription = null, tint = LustrousAmber, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "The Three-Connection Rule",
                            color = GhostWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "To preserve conversational focus, you may hold exactly 3 active chats. Others reside in the waiting ledger below.",
                            color = MutedSlate,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Divider: Active Slots count
            Text(
                "Active Connections (Slots Used: ${activeRooms.size} / 3)",
                color = RegalGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // ACTIVE CHATS LIST
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (activeRooms.isEmpty()) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = VelvetCard.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        ) {
                            Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                                Text("No active slots. Swap a room from the wait pool below.", color = MutedSlate, fontSize = 12.sp)
                            }
                        }
                    }
                } else {
                    items(activeRooms) { room ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.swapOrActivateRoom(room.id) },
                            colors = CardDefaults.cardColors(containerColor = VelvetCard),
                            border = BorderStroke(1.dp, RegalGold.copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(room.participantFlag, fontSize = 28.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(room.participantName, color = GhostWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                        Text(room.lastMessage, color = MutedSlate, fontSize = 12.sp, maxLines = 1)
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Archive slot action button
                                    IconButton(onClick = { viewModel.archiveRoom(room.id) }) {
                                        Icon(Icons.Default.Inventory, contentDescription = "Archive Slot", tint = MutedSlate, modifier = Modifier.size(20.dp))
                                    }
                                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = RegalGold)
                                }
                            }
                        }
                    }
                }

                // Divider: Waiting ledger queue
                item {
                    Text(
                        "Incoming Waiting Queue (${waitingRooms.size} requests)",
                        color = MutedSlate,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
                    )
                }

                // BLURRED / LOCKED WAITING QUEUE LIST
                if (waitingRooms.isEmpty()) {
                    item {
                        Text(
                            "Waiting matrix is completely clear.",
                            color = MutedSlate,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(24.dp)
                        )
                    }
                } else {
                    items(waitingRooms) { room ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.swapOrActivateRoom(room.id)
                                },
                            colors = CardDefaults.cardColors(containerColor = VelvetCard.copy(alpha = 0.4f)),
                            border = BorderStroke(0.5.dp, MutedSlate.copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.blur(if (activeRooms.size >= 3) 4.dp else 0.dp) // Blur effect representing structural limits
                                ) {
                                    Text(room.participantFlag, fontSize = 24.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(room.participantName, color = GhostWhite, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                        Text("[Locked queue: swap to read]", color = MutedSlate, fontSize = 11.sp)
                                    }
                                }

                                Button(
                                    onClick = { viewModel.swapOrActivateRoom(room.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = RegalGold),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("ACTIVATE", color = CharcoalObsidian, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        // Individual Chat messaging room interface view
        val activeRoom = (activeRooms + waitingRooms).find { it.id == selectedId }
        Column(modifier = Modifier.fillMaxSize()) {
            // Room Header strip - allows clicking profile details of chat partner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VelvetCard)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.selectRoom(null) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = RegalGold)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Row(
                        modifier = Modifier.clickable {
                            activeRoom?.let { room ->
                                viewModel.showProfileForUserByName(room.participantName, room.participantFlag, room.participantRank, room.participantTerritory)
                            }
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(activeRoom?.participantFlag ?: "🌍", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(activeRoom?.participantName ?: "Conversation partner", color = GhostWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(activeRoom?.participantRank ?: "Citizen", color = RegalGold, fontSize = 11.sp)
                        }
                    }
                }

                // Fast archive trigger inside room
                TextButton(
                    onClick = {
                        activeRoom?.id?.let { id ->
                            viewModel.archiveRoom(id)
                        }
                    }
                ) {
                    Text("ARCHIVE", color = CrimsonRep, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            val listState = rememberLazyListState()
            LaunchedEffect(messagesList.size) {
                if (messagesList.isNotEmpty()) {
                    listState.animateScrollToItem(messagesList.size - 1)
                }
            }

            // Messaging thread log with auto scrolls to bottom
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
            ) {
                items(messagesList) { message ->
                    val isMe = message.senderId == "me"
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        val clipboardManager = LocalClipboardManager.current
                        val localContext = LocalContext.current
                        val textToCopy = message.messageText.trim()
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isMe) RegalGold.copy(alpha = 0.15f) else VelvetCard
                            ),
                            border = BorderStroke(0.5.dp, if (isMe) RegalGold else MutedSlate.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (isMe) 16.dp else 0.dp,
                                bottomEnd = if (isMe) 0.dp else 16.dp
                            ),
                            modifier = Modifier
                                .widthIn(max = 280.dp)
                                .clickable {
                                    clipboardManager.setText(AnnotatedString(textToCopy))
                                    Toast.makeText(localContext, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                                }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = textToCopy,
                                    color = GhostWhite,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy message",
                                        tint = RegalGold.copy(alpha = 0.6f),
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "Click to copy",
                                        color = MutedSlate,
                                        fontSize = 8.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Lock typing unless the connection is fully active
            val isConnectionActive = activeRoom?.isActive == true

            // Keyboard bottom write tray
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VelvetCard)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = if (isConnectionActive) chatMessageText else "",
                    onValueChange = { if (isConnectionActive) chatMessageText = it },
                    placeholder = { 
                        Text(
                            if (isConnectionActive) "Compile constructive message..." 
                            else "Archived slot: Swap from queue to activate & send"
                        ) 
                    },
                    enabled = isConnectionActive,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = GhostWhite,
                        unfocusedTextColor = GhostWhite,
                        focusedBorderColor = RegalGold,
                        unfocusedBorderColor = MutedSlate,
                        disabledTextColor = MutedSlate,
                        disabledPlaceholderColor = MutedSlate.copy(alpha = 0.6f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                    onClick = {
                        if (isConnectionActive && chatMessageText.isNotBlank()) {
                            activeRoom?.id?.let { id ->
                                viewModel.sendMessage(id, chatMessageText)
                            }
                            chatMessageText = ""
                        }
                    },
                    enabled = isConnectionActive,
                    modifier = Modifier
                        .background(if (isConnectionActive) RegalGold else MutedSlate.copy(alpha = 0.3f), CircleShape)
                        .size(46.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Shoot", tint = if (isConnectionActive) CharcoalObsidian else MutedSlate)
                }
            }
        }
    }
}

// ============================================================================
// TAB 4: PROFILE & DEMOCRATIC monarchy elections
// ============================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElectionsAndProfileTab(viewModel: AppViewModel) {
    val me by viewModel.currentUserFlow.collectAsState()
    val visitors by viewModel.profileVisitors.collectAsState()
    val candidates by viewModel.electionCandidates.collectAsState()
    val leaderboardUsers by viewModel.leaderboardUsers.collectAsState()

    var showCampaignModalByMe by remember { mutableStateOf(false) }
    var campaignVision by remember { mutableStateOf("") }
    var campaignManifesto by remember { mutableStateOf("") }

    var editingPostState by remember { mutableStateOf<PostEntity?>(null) }
    var editPostContentText by remember { mutableStateOf("") }

    // Edit Profile state flows
    val showEditProfileModal by viewModel.showEditProfileDialog.collectAsState()
    var editName by remember { mutableStateOf(me?.name ?: "") }
    var editUsername by remember { mutableStateOf(me?.username?.removePrefix("@") ?: "") }
    var editBio by remember { mutableStateOf(me?.bio ?: "") }
    var editTerritory by remember { mutableStateOf(me?.territory ?: "") }
    var editFlagEmoji by remember { mutableStateOf(me?.flagEmoji ?: "🌍") }
    var editProfilePhoto by remember { mutableStateOf(me?.profilePhoto ?: "") }

    LaunchedEffect(showEditProfileModal) {
        if (showEditProfileModal && me != null) {
            editName = me!!.name
            editUsername = me!!.username.removePrefix("@")
            editBio = me!!.bio
            editTerritory = me!!.territory
            editFlagEmoji = me!!.flagEmoji
            editProfilePhoto = me!!.profilePhoto
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Real-Time Profile Ledger Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(1.dp, RegalGold),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column {
                    // Accent cover panel
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(RegalGold.copy(alpha = 0.4f), ElectricBlue.copy(alpha = 0.4f))
                                )
                            )
                    )

                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            // Circular Avatar and Flag Overlay
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .offset(y = (-20).dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(CharcoalObsidian)
                                        .border(2.dp, RegalGold, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (me?.profilePhoto?.isNotBlank() == true) me!!.profilePhoto.take(1).uppercase() else (me?.name ?: "N").take(1).uppercase(),
                                        color = RegalGold,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .offset(y = 10.dp)
                                        .background(CharcoalObsidian, CircleShape)
                                        .border(1.dp, RegalGold.copy(alpha = 0.5f), CircleShape)
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(me?.flagEmoji ?: "🌍", fontSize = 14.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        me?.name ?: "Noble Citizen",
                                        color = GhostWhite,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = RegalGold.copy(alpha = 0.15f)),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            me?.currentRank ?: "Citizen",
                                            color = RegalGold,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    me?.username ?: "@username",
                                    color = MutedSlate,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Personality Traits selection array
                        Text(
                            "Personality Blueprint Traits",
                            color = RegalGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            me?.personalityTraits?.split(",")?.forEach { trait ->
                                if (trait.isNotBlank()) {
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = CharcoalObsidian),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = trait,
                                            color = GhostWhite,
                                            fontSize = 9.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Biography Content Field
                        Text(
                            text = me?.bio ?: "Dedicated to open collaborative work across territories.",
                            color = GhostWhite.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(top = 6.dp)
                        )

                        // Edit Personal Profile Synchronization Button
                        Button(
                            onClick = { viewModel.setShowEditProfileDialog(true) },
                            colors = ButtonDefaults.buttonColors(containerColor = RegalGold.copy(alpha = 0.15f)),
                            border = BorderStroke(1.dp, RegalGold),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = RegalGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Edit", color = RegalGold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // MERITOCRATIC RANK ADVANCEMENT CARD
        item {
            val meUser = me
            val currentRank = meUser?.currentRank ?: "Citizen"
            val kc = meUser?.knowledgeCredits ?: 0
            val cc = meUser?.contributionCredits ?: 0
            val totalCredits = kc + cc

            val (nextRank, targetCredits, prevThreshold) = when {
                totalCredits < 100 -> Triple("Guardian", 100, 0)
                totalCredits < 250 -> Triple("Noble", 250, 100)
                totalCredits < 500 -> Triple("Prince", 500, 250)
                else -> Triple("Arch-Prince", 1000, 500)
            }

            val progressInLevel = (totalCredits - prevThreshold).coerceAtLeast(0)
            val levelSpan = targetCredits - prevThreshold
            val progressFraction = if (levelSpan > 0) {
                progressInLevel.toFloat() / levelSpan.toFloat()
            } else {
                1.0f
            }.coerceIn(0.0f, 1.0f)

            val creditsNeeded = (targetCredits - totalCredits).coerceAtLeast(0)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(1.dp, RegalGold.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Meritocratic Rank Status",
                                color = GhostWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Sovereign Advancement Pathway",
                                color = MutedSlate,
                                fontSize = 11.sp
                            )
                        }
                        
                        // Active tier badge
                        Box(
                            modifier = Modifier
                                .background(RegalGold.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "TIER ${when(currentRank) {
                                    "Citizen" -> "I"
                                    "Guardian" -> "II"
                                    "Noble" -> "III"
                                    "Prince" -> "IV"
                                    else -> "MAX"
                                }}",
                                color = RegalGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress indicators
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "Current: $currentRank ${when(currentRank) {
                                    "Citizen" -> "🌱"
                                    "Guardian" -> "🛡️"
                                    "Noble" -> "⚜️"
                                    "Prince" -> "👑"
                                    else -> "💎"
                                }}",
                                color = GhostWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "$totalCredits / $targetCredits credits accumulated",
                                color = MutedSlate,
                                fontSize = 11.sp
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Next: $nextRank",
                                color = RegalGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$creditsNeeded remaining",
                                color = MutedSlate,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(MutedSlate.copy(alpha = 0.15f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progressFraction)
                                .clip(RoundedCornerShape(5.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(RegalGold, LustrousAmber)
                                    )
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Key details checklist / Benefits of Next Tier
                    Text(
                        text = "Next Tier Privileges & Missions",
                        color = RegalGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val missionDescription = when (nextRank) {
                            "Guardian" -> listOf(
                                "🛡️ Register as political candidate",
                                "🛡️ Unlock secondary messaging forums",
                                "🛡️ Gain 20% faster reputation recovery"
                            )
                            "Noble" -> listOf(
                                "⚜️ Propose formal bills in Public Square",
                                "⚜️ Set up collaborative service quests",
                                "⚜️ Moderate lower citizen arbitration disputes"
                            )
                            "Prince" -> listOf(
                                "👑 Issue sovereign structural decrees",
                                "👑 Command special elite legions",
                                "👑 Secure high advisory executive seats"
                            )
                            else -> listOf(
                                "💎 Arch-status elite merit achieved",
                                "💎 Full cosmic sovereignty active",
                                "💎 Influence global policy and protocols"
                            )
                        }

                        missionDescription.forEach { benefit ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 1.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = EmeraldSuccess,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = benefit,
                                    color = GhostWhite.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Boost Rank Action Button (One Word)
                    Button(
                        onClick = { viewModel.selectTab(DashboardTab.KnowledgeArena) },
                        colors = ButtonDefaults.buttonColors(containerColor = RegalGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Earn",
                            color = CharcoalObsidian,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // TRANSPARENT RECIPROCAL VISIBILITY LEDGER
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(0.5.dp, MutedSlate.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Reciprocal Profile Visitors Ledger",
                            color = GhostWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(Icons.Default.Visibility, contentDescription = null, tint = RegalGold, modifier = Modifier.size(16.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    visitors.forEach { visitor ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable {
                                    viewModel.showProfileForUserByName(visitor.visitorName, visitor.visitorFlag, visitor.visitorRank, visitor.visitorTerritory)
                                }
                            ) {
                                Box(
                                    modifier = Modifier.size(46.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(CharcoalObsidian)
                                            .border(1.dp, RegalGold, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = visitor.visitorName.take(1).uppercase(),
                                            color = RegalGold,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .offset(y = 4.dp)
                                            .background(CharcoalObsidian, CircleShape)
                                            .border(0.5.dp, RegalGold.copy(alpha = 0.5f), CircleShape)
                                            .padding(horizontal = 3.dp, vertical = 1.dp)
                                    ) {
                                        Text(visitor.visitorFlag, fontSize = 10.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(visitor.visitorName, color = GhostWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Text("${visitor.visitorRank} • ${visitor.visitorTerritory}", color = MutedSlate, fontSize = 9.sp)
                                }
                            }

                            Text("Checked now", color = RegalGold, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // My Broadcasted Entries Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(0.5.dp, RegalGold.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "My Broadcasted Entries",
                            color = GhostWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(Icons.Default.Feed, contentDescription = null, tint = RegalGold, modifier = Modifier.size(16.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val allPosts by viewModel.rankedPosts.collectAsState()
                    val myPosts = remember(allPosts) { allPosts.filter { it.authorId == "me" } }

                    if (myPosts.isEmpty()) {
                        Text(
                            text = "No entries broadcasted to the Public Square yet.",
                            color = MutedSlate,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        myPosts.forEach { post ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = CharcoalObsidian),
                                border = BorderStroke(0.5.dp, MutedSlate.copy(alpha = 0.2f)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = RegalGold.copy(alpha = 0.15f)),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = post.category,
                                                color = RegalGold,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }

                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            IconButton(
                                                onClick = {
                                                    editingPostState = post
                                                    editPostContentText = post.content
                                                },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.Edit, contentDescription = "Edit Post", tint = ElectricBlue, modifier = Modifier.size(14.dp))
                                            }

                                            IconButton(
                                                onClick = {
                                                    viewModel.deletePost(post.id)
                                                },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "Delete Post", tint = Color.Red.copy(alpha = 0.8f), modifier = Modifier.size(14.dp))
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = post.content,
                                        color = GhostWhite,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("🧠 ${post.knowledgeValue} Wise", color = MutedSlate, fontSize = 10.sp)
                                        Text("🤝 ${post.contributionProof} Helpful", color = MutedSlate, fontSize = 10.sp)
                                        Text("✨ ${(post.reputationImpact - 90).coerceAtLeast(0)} Inspiring", color = MutedSlate, fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // MONARCHY AND DEMOCRATIC CREDENTIALS LEADERBOARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(1.dp, RegalGold),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Monarch Sovereign & Merit Leaderboard",
                            color = GhostWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = RegalGold, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // King and Queen highlights
                    val kingUser = leaderboardUsers.firstOrNull { it.gender.equals("Male", ignoreCase = true) }
                        ?: leaderboardUsers.getOrNull(0)
                    val queenUser = leaderboardUsers.firstOrNull { it.gender.equals("Female", ignoreCase = true) && it.id != kingUser?.id }
                        ?: leaderboardUsers.getOrNull(1)

                    if (kingUser != null) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = CharcoalObsidian),
                            border = BorderStroke(1.dp, RegalGold),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clickable { viewModel.showProfileForUser(kingUser.id) }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("👑", fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("KING OF THE REALM", color = RegalGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    Text(kingUser.name, color = GhostWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text("${kingUser.currentRank} • ${kingUser.flagEmoji} ${kingUser.territory}", color = MutedSlate, fontSize = 10.sp)
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Text("${kingUser.knowledgeCredits + kingUser.contributionCredits} Credits", color = RegalGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    if (queenUser != null) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = CharcoalObsidian),
                            border = BorderStroke(1.dp, RegalGold.copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                                .clickable { viewModel.showProfileForUser(queenUser.id) }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("👑", fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("QUEEN OF THE REALM", color = RegalGold.copy(alpha = 0.8f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    Text(queenUser.name, color = GhostWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text("${queenUser.currentRank} • ${queenUser.flagEmoji} ${queenUser.territory}", color = MutedSlate, fontSize = 10.sp)
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Text("${queenUser.knowledgeCredits + queenUser.contributionCredits} Credits", color = RegalGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Divider(color = MutedSlate.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        "Merit Rank Ledger:",
                        color = RegalGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    leaderboardUsers.forEachIndexed { index, user ->
                        val totalCredits = user.knowledgeCredits + user.contributionCredits
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clickable { viewModel.showProfileForUser(user.id) },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${index + 1}.",
                                    color = RegalGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(24.dp)
                                )
                                Text(user.flagEmoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(user.name, color = GhostWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("${user.currentRank} • ${user.territory}", color = MutedSlate, fontSize = 9.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = ElectricBlue.copy(alpha = 0.15f)),
                                            border = BorderStroke(0.5.dp, ElectricBlue.copy(alpha = 0.5f)),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = getAutoSuggestedRole(user),
                                                color = ElectricBlue,
                                                fontSize = 7.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            Text(
                                text = "$totalCredits Credits",
                                color = if (index < 2) RegalGold else GhostWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // CONSTITUTION ELECTION CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(1.dp, RegalGold),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Sovereign House of Representatives Election",
                        color = GhostWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Power is not dynastic. Our King and Queen are elected democratically using merit weights:",
                        color = MutedSlate,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Formula visualization
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CharcoalObsidian),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Leadership_Score Formula:",
                                color = RegalGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Score = (0.70 * KnowledgeCredits_Normalized) +\n            (0.30 * PublicVoting_Normalized)",
                                color = GhostWhite,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Active Candidates Pool",
                            color = GhostWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Register candidacy switch button
                        if (me?.isCandidate == false) {
                            Button(
                                onClick = { showCampaignModalByMe = true },
                                colors = ButtonDefaults.buttonColors(containerColor = RegalGold),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("RUN FOR CROWN", color = CharcoalObsidian, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = EmeraldSuccess.copy(alpha = 0.15f)),
                                border = BorderStroke(0.5.dp, EmeraldSuccess),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "REGISTERED",
                                    color = EmeraldSuccess,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Candidate card lists
                    candidates.forEach { candidate ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = CharcoalObsidian.copy(alpha = 0.4f)),
                            border = BorderStroke(0.5.dp, MutedSlate.copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable {
                                            viewModel.showProfileForUser(candidate.id)
                                        }
                                    ) {
                                        Text(candidate.flagEmoji, fontSize = 22.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(candidate.name, color = GhostWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            Text("Sovereign Candidate • Pop: ${candidate.votesCount} votes", color = RegalGold, fontSize = 10.sp)
                                        }
                                    }

                                    Button(
                                        onClick = { viewModel.castVote(candidate.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = RegalGold),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(28.dp),
                                        enabled = me?.hasVoted == false
                                    ) {
                                        Text("CAST VOTE", color = CharcoalObsidian, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Text(
                                    text = "Vision: ${candidate.campaignVision}",
                                    color = GhostWhite,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(top = 8.dp)
                                )

                                Text(
                                    text = candidate.campaignManifesto,
                                    color = MutedSlate,
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal: Register candidacy compiler
    if (showCampaignModalByMe) {
        Dialog(onDismissRequest = { showCampaignModalByMe = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(1.dp, RegalGold),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Dossier: Royal Sovereign Crown Setup",
                        color = GhostWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = campaignVision,
                        onValueChange = { campaignVision = it },
                        label = { Text("Planetary Vision Statement") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GhostWhite,
                            unfocusedTextColor = GhostWhite,
                            focusedBorderColor = RegalGold,
                            unfocusedBorderColor = MutedSlate
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = campaignManifesto,
                        onValueChange = { campaignManifesto = it },
                        label = { Text("Campaign Manifesto & Methods") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GhostWhite,
                            unfocusedTextColor = GhostWhite,
                            focusedBorderColor = RegalGold,
                            unfocusedBorderColor = MutedSlate
                        ),
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(onClick = { showCampaignModalByMe = false }) {
                            Text("ABORT", color = MutedSlate)
                        }

                        Button(
                            onClick = {
                                if (campaignVision.isNotBlank() && campaignManifesto.isNotBlank()) {
                                    viewModel.registerElectionsCandidate(campaignManifesto, campaignVision)
                                    showCampaignModalByMe = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RegalGold)
                        ) {
                            Text("SUBMIT BALLOT DOSSIER", color = CharcoalObsidian)
                        }
                    }
                }
            }
        }
    }

    if (showEditProfileModal) {
        Dialog(onDismissRequest = { viewModel.setShowEditProfileDialog(false) }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(1.dp, RegalGold),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Synchronize Imperial Profile",
                        color = GhostWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Noble Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GhostWhite,
                            unfocusedTextColor = GhostWhite,
                            focusedBorderColor = RegalGold,
                            unfocusedBorderColor = MutedSlate
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = editUsername,
                        onValueChange = { editUsername = it },
                        label = { Text("System Handle / Username") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GhostWhite,
                            unfocusedTextColor = GhostWhite,
                            focusedBorderColor = RegalGold,
                            unfocusedBorderColor = MutedSlate
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text("Personal Biography / Credo") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GhostWhite,
                            unfocusedTextColor = GhostWhite,
                            focusedBorderColor = RegalGold,
                            unfocusedBorderColor = MutedSlate
                        ),
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = editTerritory,
                        onValueChange = { editTerritory = it },
                        label = { Text("Territory Assigned") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GhostWhite,
                            unfocusedTextColor = GhostWhite,
                            focusedBorderColor = RegalGold,
                            unfocusedBorderColor = MutedSlate
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = editFlagEmoji,
                        onValueChange = { editFlagEmoji = it },
                        label = { Text("Territory Emoji Flag") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GhostWhite,
                            unfocusedTextColor = GhostWhite,
                            focusedBorderColor = RegalGold,
                            unfocusedBorderColor = MutedSlate
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = editProfilePhoto,
                        onValueChange = { editProfilePhoto = it },
                        label = { Text("Profile Photo URL or Avatar") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GhostWhite,
                            unfocusedTextColor = GhostWhite,
                            focusedBorderColor = RegalGold,
                            unfocusedBorderColor = MutedSlate
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(onClick = { viewModel.setShowEditProfileDialog(false) }) {
                            Text("ABORT", color = MutedSlate)
                        }

                        Button(
                            onClick = {
                                if (editName.isNotBlank() && editUsername.isNotBlank()) {
                                    viewModel.updateUserProfile(
                                        name = editName,
                                        username = editUsername,
                                        bio = editBio,
                                        territory = editTerritory,
                                        flagEmoji = editFlagEmoji,
                                        profilePhoto = editProfilePhoto
                                    )
                                    viewModel.setShowEditProfileDialog(false)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RegalGold)
                        ) {
                            Text("SYNCHRONIZE", color = CharcoalObsidian)
                        }
                    }
                }
            }
        }
    }

    if (editingPostState != null) {
        Dialog(onDismissRequest = { editingPostState = null }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = VelvetCard),
                border = BorderStroke(1.dp, RegalGold),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Edit Public Square Entry",
                        color = GhostWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = editPostContentText,
                        onValueChange = { editPostContentText = it },
                        label = { Text("What would you like to say?") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GhostWhite,
                            unfocusedTextColor = GhostWhite,
                            focusedBorderColor = RegalGold,
                            unfocusedBorderColor = MutedSlate
                        ),
                        modifier = Modifier.fillMaxWidth().height(120.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(onClick = { editingPostState = null }) {
                            Text("CANCEL", color = MutedSlate)
                        }

                        Button(
                            onClick = {
                                if (editPostContentText.isNotBlank()) {
                                    viewModel.updatePostContent(editingPostState!!.id, editPostContentText)
                                    editingPostState = null
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RegalGold)
                        ) {
                            Text("UPDATE", color = CharcoalObsidian)
                        }
                    }
                }
            }
        }
    }
}

// ============================================================================
// TAB 5: ACTIVE MISSION BOARDS & HISTORIC HALL OF LEGENDS
// ============================================================================
@Composable
fun MissionsTab(viewModel: AppViewModel) {
    val missions by viewModel.activeMissions.collectAsState()
    val legends by viewModel.hallOfLegends.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Imperial Active Missions Panel
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Active Imperial Missions",
                    color = GhostWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Collaborative environmental and intellectual work to earn credit points.",
                    color = MutedSlate,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                missions.forEach { mission ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = VelvetCard),
                        border = BorderStroke(0.5.dp, MutedSlate.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    mission.title,
                                    color = GhostWhite,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = RegalGold.copy(alpha = 0.15f)),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = mission.targetMetric,
                                        color = RegalGold,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Text(
                                text = mission.description,
                                color = MutedSlate,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            // Numerical progress metrics
                            val progressPercent = (mission.currentProgress.toFloat() / mission.targetValue.toFloat())
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Progress: ${mission.currentProgress} / ${mission.targetValue}",
                                    color = GhostWhite,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    "${(progressPercent * 100).toInt()}% completed",
                                    color = EmeraldSuccess,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            LinearProgressIndicator(
                                progress = progressPercent,
                                color = RegalGold,
                                trackColor = CharcoalObsidian,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )

                            // Allocation button trigger
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = { viewModel.contributeToMission(mission.id, 10) },
                                    colors = ButtonDefaults.buttonColors(containerColor = RegalGold),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text("ALLOCATE 10 CC", color = CharcoalObsidian, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // HISTORICAL HALL OF LEGENDS BOARD
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    "Imperial Hall of Legends",
                    color = GhostWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Permanent immutable archive of legendary monarchs, grand educators, and founders.",
                    color = MutedSlate,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                legends.forEach { legend ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = VelvetCard),
                        border = BorderStroke(1.dp, RegalGold.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Stars,
                                contentDescription = null,
                                tint = RegalGold,
                                modifier = Modifier.size(36.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        legend.name,
                                        color = GhostWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = ElectricBlue.copy(alpha = 0.15f)),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = legend.achievement,
                                            color = ElectricBlue,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = legend.role,
                                    color = RegalGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(top = 2.dp)
                                )

                                Text(
                                    text = legend.details,
                                    color = MutedSlate,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Data models for layout presentation
data class OnboardingSlide(
    val title: String,
    val desc: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val themeColor: androidx.compose.ui.graphics.Color
)

data class TerritoryItem(
    val name: String,
    val flag: String,
    val region: String,
    val population: String,
    val color: androidx.compose.ui.graphics.Color
)

fun getAutoSuggestedRole(user: UserEntity): String {
    val traits = user.personalityTraits.lowercase()
    val bio = user.bio.lowercase()
    return when {
        user.id == "gandhi_avatar" || user.name.contains("Arjun") -> "Ecological Sovereign"
        user.id == "clara_nobel" || user.name.contains("Clara") -> "Quantum Archon"
        user.id == "kenya_leader" || user.name.contains("Kofi") -> "Solar Marshal"
        traits.contains("scientist") || traits.contains("teacher") || bio.contains("physics") || bio.contains("education") -> "Chief Intellect Adviser"
        traits.contains("humanitarian") || traits.contains("leader") || bio.contains("recycle") || bio.contains("waste") || bio.contains("eco") -> "Sovereign Green Architect"
        traits.contains("builder") || bio.contains("solar") || bio.contains("infra") || bio.contains("power") -> "Imperial Grid Marshal"
        traits.contains("philosopher") || bio.contains("wisdom") || bio.contains("plato") -> "High Philosopher Guide"
        user.knowledgeCredits > user.contributionCredits -> "Grand Knowledge Archivist"
        user.contributionCredits >= user.knowledgeCredits -> "Supreme Social Director"
        else -> "Civic Envoy"
    }
}
