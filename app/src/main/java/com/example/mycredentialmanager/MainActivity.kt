package com.example.mycredentialmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.mycredentialmanager.ui.screens.LoginScreen
import com.example.mycredentialmanager.ui.screens.LoginSuccessScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        loginViewModel.initViewModel(this)
        super.onCreate(savedInstanceState)
        /*lifecycleScope.launch {
            loginViewModel.logInTest(this@MainActivity)
        }*/
        setContent {
            MyApp {
                val isLoggedIn = loginViewModel.isLoggedIn.value

                if (isLoggedIn) {
                    LoginSuccessScreen(loginViewModel)
                } else {
                    LoginScreen(loginViewModel, this)
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}

/*@Composable
fun MyApp(activity: ComponentActivity, viewModel: LoginViewModel) {
    var passkeyResult by remember { mutableStateOf("") }

    LoginScreen(viewModel = viewModel, activity = activity)
}*/


suspend fun signInWithCredentialManager(activity: ComponentActivity) {
    val credentialManager = CredentialManager.create(/* context */ activity)
    val getPasswordOption = GetPasswordOption()
    val getPublicKeyCredentialOption = GetPublicKeyCredentialOption(
        requestJson = "{\"challenge\":\"T1xCsnxM2DNL2KdK5CLa6fMhD7OBqho6syzInk_n-Uo\",\"allowCredentials\":[],\"timeout\":1800000,\"userVerification\":\"required\",\"rpId\":\"https://julio.dev\"}",
        preferImmediatelyAvailableCredentials = true
    )
    val getCredRequest = GetCredentialRequest(
        listOf(getPasswordOption, getPublicKeyCredentialOption)
    )

    try {
        val result: GetCredentialResponse = credentialManager.getCredential(
            request = getCredRequest,
            activity = activity
        )
        handleSignIn(result)
    } catch (e: GetCredentialException) {
        handleFailure(e)
    }
}

fun handleSignIn(result: GetCredentialResponse) {
    val credential = result.credential

    when (credential) {
        is PublicKeyCredential -> {
            // Handle public key credential
            val responseJson = credential.authenticationResponseJson
            // TODO: Authenticate with server using responseJson
        }
        is PasswordCredential -> {
            // Handle password credential
            val username = credential.id
            val password = credential.password
            // TODO: Authenticate with server using username and password
        }
        else -> {
            // Catch any unrecognized credential type here.
            // TODO: Handle unrecognized credential type
        }
    }
}

fun handleFailure(e: GetCredentialException) {
    // Handle credential retrieval failure
    // TODO: Handle failure case
}
