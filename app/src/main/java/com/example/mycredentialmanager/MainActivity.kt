package com.example.mycredentialmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MyApp(this)
            }
        }
    }
}

@Composable
fun MyApp(activity: ComponentActivity) {
    var passkeyResult by remember { mutableStateOf("") }

    Column {
        Text(text = passkeyResult)
        Button(onClick = { passkeyResult = "done" }) {
            Text(text = "Sign In")
        }



        LaunchedEffect(key1 = Unit) {
            signInWithCredentialManager(activity)
        }
    }
}

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
