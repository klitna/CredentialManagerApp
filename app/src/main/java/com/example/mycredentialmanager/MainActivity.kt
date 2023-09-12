package com.example.mycredentialmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
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
import com.example.mycredentialmanager.ui.screens.PasswordLoginScreen
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

                if(loginViewModel.isSignIn.value) {
                    if (isLoggedIn) {
                        AuthenticationSuccessDialog( onDismiss = {  })
                        loginViewModel.signOut()
                    } else {
                        PasswordLoginScreen(viewModel = loginViewModel, activity = this)
                    }
                }
                else{
                    if (isLoggedIn) {
                        LoginSuccessScreen(loginViewModel)
                    } else {
                        LoginScreen(loginViewModel, this)
                    }
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

@Composable
fun AuthenticationSuccessDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Authentication Successful")
        },
        text = {
            Text(text = "You have successfully logged in.")
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(text = "OK")
            }
        }
    )
}

