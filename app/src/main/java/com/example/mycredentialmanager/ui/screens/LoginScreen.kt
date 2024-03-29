package com.example.mycredentialmanager.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mycredentialmanager.LoginViewModel
import com.example.mycredentialmanager.R

@Composable
fun LoginScreen(viewModel: LoginViewModel, activity: androidx.activity.ComponentActivity) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val passwordPainter: Painter = painterResource(id = R.drawable.password)

        Image(
            painter = passwordPainter,
            contentDescription = "Password Icon",
            modifier = Modifier.size(150.dp) // Adjust size as needed
        )

        /*OutlinedTextField(
            value = viewModel.username.value,
            onValueChange = { newUsername -> viewModel.updateUsername(newUsername) },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )*/

        /*Button(
            onClick = { viewModel.createPasskeyButtonClicked(activity) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .size(width = 90.dp, height = 48.dp)
                .background(color = Color.Green)
        ) {
            Text(text = "Create passkey")
        }*/
        Button(
            onClick = { viewModel.loginButtonClicked(activity) },
            modifier = Modifier
                .padding(16.dp)
                .size(width = 150.dp, height = 48.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "Log in")
        }

        Button(
            onClick = { viewModel.signIn() },
            modifier = Modifier
                .padding(16.dp)
                .size(width = 150.dp, height = 48.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "Sign in")
        }
        /*Button(
            onClick = { viewModel.passwordLoginButtonClicked(activity, "user1408", "dU8B4A3T0u4") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .size(width = 90.dp, height = 48.dp)
                .background(color = Color.Green)
        ) {
            Text(text = "Create account")
        }*/
    }

}

