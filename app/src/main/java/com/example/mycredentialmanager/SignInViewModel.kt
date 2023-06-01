package com.example.mycredentialmanager

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    private val _passkeyResult = MutableLiveData<String>()
    val passkeyResult: LiveData<String> = _passkeyResult

    fun initializeSignIn(context: Context) {
        viewModelScope.launch {
            val context: Context = context
            signInWithCredentialManager(context)
        }
    }

    private suspend fun signInWithCredentialManager(context: Context) {
        val credentialManager = CredentialManager.create(/* context */ context)
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
                activity = TODO()
            )
            handleSignIn(result)
        } catch (e: GetCredentialException) {
            handleFailure(e)
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
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

    private fun handleFailure(e: GetCredentialException) {
        // Handle credential retrieval failure
        // TODO: Handle failure case
    }
}