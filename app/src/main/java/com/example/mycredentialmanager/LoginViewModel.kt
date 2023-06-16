package com.example.mycredentialmanager

import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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

class LoginViewModel : ViewModel() {
    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    private val _username = mutableStateOf("")
    val username: State<String> = _username

    private lateinit var credentialManager: CredentialManager

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun initViewModel(activity: ComponentActivity){
        credentialManager = androidx.credentials.CredentialManager.create(/* context */ activity)
    }

    fun login() {
        _isLoggedIn.value = true
    }

    fun logout() {
        _isLoggedIn.value = false
    }

    suspend fun logInTest(activity: ComponentActivity) {
        val credentialManager = CredentialManager.create(/* context */ activity)
        val getPasswordOption = GetPasswordOption()
        val getPublicKeyCredentialOption = GetPublicKeyCredentialOption(
            requestJson = "{\"challenge\":\"HjBbH__fbLuzy95AGR31yEARA0EMtKlY0NrV5oy3NQw\",\"timeout\":1800000,\"userVerification\":\"\",\"rpId\":\"www.julio.dev\"}",
            preferImmediatelyAvailableCredentials = true
        )
        val getCredRequest = GetCredentialRequest(
            listOf(getPublicKeyCredentialOption, getPasswordOption)
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

    fun loginButtonClicked(activity: androidx.activity.ComponentActivity) {
        viewModelScope.launch {
            signInWithCredentialManager(activity)
            //logInTest(activity)
        }
    }

    fun createPasskeyButtonClicked(activity: androidx.activity.ComponentActivity) {
        activity.lifecycleScope.launch {
            signUpWithCredentialManager(activity)
        }
    }

    private suspend fun signUpWithCredentialManager(activity: androidx.activity.ComponentActivity): CreatePublicKeyCredentialResponse? {
        val request = CreatePublicKeyCredentialRequest(getRequestLogIn(activity))
        var response: CreatePublicKeyCredentialResponse? = null
        try {
            response = credentialManager.createCredential(
                request = request,
                activity = activity
            ) as CreatePublicKeyCredentialResponse
        } catch (e: CreateCredentialException) {
            //configureProgress(View.INVISIBLE)
            Log.e("login", "unable to login")
        }
        return response
    }

    private fun getRequest(activity: ComponentActivity): String {

        val response = activity.applicationContext.readFromAsset("userinfo")

        return response
    }

    private fun getRequestLogIn(activity: ComponentActivity): String {
        return activity.applicationContext.readFromAsset("passkeyrequest.json")
    }

    private fun getResponse(activity: ComponentActivity): String{
        val requestJson = activity.applicationContext.assets.open("userinfo.json").bufferedReader().use {
            it.readText()
        }
        return (requestJson.replace("<userId>", generateId())
            .replace("<userName>", username.value)
            .replace("<userDisplayName>", username.value)
            .replace("<challenge>", generateChallenge()))
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
        } catch (e: Exception) {
            Log.e("Login", "error: " + e.message.toString())
            return null
        }
        return result
    }

    fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential
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