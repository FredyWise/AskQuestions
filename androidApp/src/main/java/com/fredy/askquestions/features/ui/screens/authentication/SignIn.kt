package com.fredy.mysavings.Feature.Presentation.Screens.Authentication

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fredy.askquestions.R
import com.fredy.askquestions.features.data.apis.ApiConfiguration
import com.fredy.askquestions.features.data.enums.AuthMethod
import com.fredy.askquestions.features.domain.util.Resource.Resource
import com.fredy.askquestions.features.ui.navigation.NavigationRoute
import com.fredy.askquestions.features.ui.screens.authentication.CustomTextField
import com.fredy.askquestions.features.ui.screens.authentication.GoogleButton
import com.fredy.askquestions.features.ui.util.isValidLogin
import com.fredy.askquestions.features.ui.util.isValidPhoneNumber
import com.fredy.askquestions.features.ui.viewmodels.AuthViewModel.AuthEvent
import com.fredy.askquestions.features.ui.viewmodels.AuthViewModel.AuthState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignIn(
    navController: NavHostController,
    buttonColor: Color = MaterialTheme.colorScheme.primary.copy(
        0.4f
    ),
    onButtonColor: Color = MaterialTheme.colorScheme.onPrimary,
    onBackgroundColor: Color = MaterialTheme.colorScheme.onBackground,
    isUsingBioAuth: Boolean = false,
    state: AuthState,
    onEvent: (AuthEvent) -> Unit
) {
    val context = LocalContext.current
    var phoneNumber by rememberSaveable {
        mutableStateOf(
            ""
        )
    }
    var otpValue by rememberSaveable {
        mutableStateOf(
            ""
        )
    }
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        val account = GoogleSignIn.getSignedInAccountFromIntent(
            it.data
        )
        try {
            val result = account.getResult(
                ApiException::class.java
            )
            val credentials = GoogleAuthProvider.getCredential(
                result.idToken, null
            )
            onEvent(
                AuthEvent.GoogleAuth(
                    credentials
                )
            )
        } catch (it: ApiException) {
            Timber.e("SignIn Error: " + it)
            print(it)
        }
    }


    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            onDismissRequest = {
                isSheetOpen = false
            },
            dragHandle = {},
        ) {
            OTPScreen(
                isLoading = state.authResource is Resource.Loading && state.authType == AuthMethod.PhoneOTP,
                phoneNumber = phoneNumber,
                onResendOtp = {
                    onEvent(
                        AuthEvent.SendOtp(
                            context,
                            phoneNumber,
                            onCodeSent = {
                                isSheetOpen = true
                            },
                        )
                    )
                },
                onOtpValueChange = { value -> otpValue = value },
                onOtpSignInClick = {
                    onEvent(
                        AuthEvent.VerifyPhoneNumber(
                            context, otpValue
                        )
                    )
                    isSheetOpen = false
                },
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 30.dp, end = 30.dp
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign In",
            style = MaterialTheme.typography.headlineMedium,
            color = onBackgroundColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Enter your credential's to sign in",
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.titleMedium,
            color = onBackgroundColor.copy(0.7f),
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            label = "Phone Number",
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            placeholder = "type your phone number...",
            keyboardType = KeyboardType.Phone
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (isValidPhoneNumber(
                            phoneNumber
                        )) {
                        onEvent(
                            AuthEvent.SendOtp(
                                context,
                                phoneNumber,
                                onCodeSent = {
                                    isSheetOpen = true
                                },
                            )
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Phone Number should contain country code",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = buttonColor
                ),
                modifier = Modifier.weight(0.8f),
                shape = RoundedCornerShape(15.dp)
            ) {
                if (state.authResource is Resource.Loading && (state.authType == AuthMethod.Email || state.authType == AuthMethod.SendOTP)) {
                    CircularProgressIndicator(
                        color = onButtonColor
                    )
                } else {
                    Text(
                        text = "Get OTP",
                        style = MaterialTheme.typography.titleMedium,
                        color = onButtonColor,
                        modifier = Modifier.padding(
                            10.dp
                        )
                    )
                }
            }
            if (isUsingBioAuth) {
                Spacer(
                    modifier = Modifier.weight(
                        0.02f
                    )
                )
                Button(
                    onClick = {
                        onEvent(AuthEvent.BioAuth)
                    },
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = buttonColor
                    ),
                    modifier = Modifier.weight(
                        0.2f
                    ),
                    contentPadding = PaddingValues(
                        10.dp
                    ),
                    shape = RoundedCornerShape(15.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = "fingerprint",
                        modifier = Modifier.size(
                            35.dp
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.08f
            )
        )
        Text(
            text = "or connect with",
            fontWeight = FontWeight.Medium,
            color = onBackgroundColor.copy(0.7f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        GoogleButton(
            text = "Sign In With Google",
            loadingText = "Signing In ...",
            isLoading = state.authResource is Resource.Loading && state.authType == AuthMethod.Google
        ) {
            if (state.signedInUser == null) {
                val gso = GoogleSignInOptions.Builder(
                    GoogleSignInOptions.DEFAULT_SIGN_IN
                ).requestEmail().requestIdToken(
                    ApiConfiguration.WebClient.ID
                ).build()

                val googleSignInClient = GoogleSignIn.getClient(
                    context, gso
                )
                launcher.launch(googleSignInClient.signInIntent)
            } else {
                Toast.makeText(
                    context,
                    "You have a session please use biometrics to sign in",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "New User? Sign Up ",
            modifier = Modifier.clickable {
                navController.navigate(
                    NavigationRoute.SignUp.route
                )
            },
            fontWeight = FontWeight.Bold,
            color = onBackgroundColor,
        )
    }
}