package com.example.mycredentialmanager

import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CreatePasswordResponse
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.security.SecureRandom
import androidx.lifecycle.viewModelScope
import com.example.mycredentialmanager.ui.screens.PasswordLoginScreen

class LoginViewModel : ViewModel() {
    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    private val _isSignIn = mutableStateOf(false)
    val isSignIn: State<Boolean> = _isSignIn

    private val _isLogin = mutableStateOf(false)
    val isLogin: State<Boolean> = _isLogin

    private val _username = mutableStateOf("")
    val username: State<String> = _username

    private val _password = mutableStateOf("")
    val password: State<String> = _password


    private lateinit var credentialManager: CredentialManager

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    fun initViewModel(activity: ComponentActivity){
        credentialManager = androidx.credentials.CredentialManager.create(/* context */ activity)
    }

    fun logout() {
        _isLoggedIn.value = false
    }

    fun loginButtonClicked(activity: androidx.activity.ComponentActivity) {
        viewModelScope.launch {
            signInWithCredentialManager(activity)
            //logInTest(activity)
        }
    }

    fun signIn() {
        _isLogin.value = false
        _isSignIn.value = true
    }

    fun signOut() {
        _isSignIn.value = false
    }

    fun passwordLoginButtonClicked(activity: androidx.activity.ComponentActivity, username: String, password: String) {

        viewModelScope.launch {
            signInWithPassword(activity, username, password)
            //logInTest(activity)
        }
        //_isPassword.value = true
    }



    fun createPasskeyButtonClicked(activity: androidx.activity.ComponentActivity) {
        activity.lifecycleScope.launch {
            signUpWithCredentialManager(activity)
        }
    }

    private suspend fun signUpWithCredentialManager(activity: ComponentActivity): CreatePublicKeyCredentialResponse? {
        val request = CreatePublicKeyCredentialRequest(getRequestSignUp(activity))
        var response: CreatePublicKeyCredentialResponse? = null
        try {
            response = credentialManager.createCredential(
                request = request,
                activity = activity
            ) as CreatePublicKeyCredentialResponse
            _isLoggedIn.value = true
            _isLogin.value = false
        } catch (e: CreateCredentialException) {
            Log.e("login", "unable to login")
        }
        return response
    }

    private fun getRequestLogIn(activity: ComponentActivity): String {
        return activity.applicationContext.readFromAsset("passkeyrequest.json")
    }

    private fun getRequestSignUp(activity: ComponentActivity): String {
        return activity.applicationContext.readFromAsset("userinfo.json")
    }

    private suspend fun signInWithCredentialManager(activity: androidx.activity.ComponentActivity): GetCredentialResponse? {
        val getPublicKeyCredentialOption =
            GetPublicKeyCredentialOption(getRequestLogIn(activity), null, true)
        val getPasswordOption = GetPasswordOption()
        val result = try {
            credentialManager.getCredential(
                GetCredentialRequest(
                    listOf(
                        getPublicKeyCredentialOption,
                        getPasswordOption
                    )
                ),
                activity
            )
        } catch (e: GetCredentialException) {
            Log.e("Login", "error: " + e.message.toString())
            handleFailure(e)
            return null
        }
        handleSignIn(result)
        return result
    }

    private suspend fun signInWithPassword(activity: androidx.activity.ComponentActivity, username: String, password: String) {
        createPassword(activity, username, password)
    }

    private suspend fun createPassword(activity: androidx.activity.ComponentActivity,username: String, password: String) {
        val request = CreatePasswordRequest(username, password)
        try {
            credentialManager.createCredential(request, activity) as CreatePasswordResponse
            _isLoggedIn.value = true
            _isLogin.value = true
        } catch (e: Exception) {
            Log.e("Auth", "createPassword failed with exception: " + e.message)
            _isLoggedIn.value = false
        }
    }

    fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential
        _isLogin.value = true
        _isLoggedIn.value = true
    }

    fun handleFailure(e: androidx.credentials.exceptions.GetCredentialException) {
        Log.e("login error", e.toString())
        _isLoggedIn.value = false
    }

    fun readFileFromAsset(fileName: String, activity: ComponentActivity): String {
        val assetManager = activity.assets
        val inputStream: InputStream = assetManager.open(fileName)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?

        try {
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bufferedReader.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return stringBuilder.toString()
    }

    private fun generateId(): String {
        val random = SecureRandom()
        val bytes = ByteArray(64)
        random.nextBytes(bytes)
        return Base64.encodeToString(
            bytes,
            Base64.NO_WRAP or Base64.URL_SAFE or Base64.NO_PADDING
        )
    }

    private fun generateChallenge(): String {
        val random = SecureRandom()
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return Base64.encodeToString(
            bytes,
            Base64.NO_WRAP or Base64.URL_SAFE or Base64.NO_PADDING
        )
    }
}