package com.example.mycredentialmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycredentialmanager.LoginViewModel

@Composable
fun PasswordLoginScreen(viewModel: LoginViewModel, activity: androidx.activity.ComponentActivity) {

    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = viewModel.username.value,
            onValueChange = { newUsername -> viewModel.updateUsername(newUsername) },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (isPasswordVisible) {
            OutlinedTextField(
                value = viewModel.password.value,
                onValueChange = { newPassword -> viewModel.updatePassword(newPassword) },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }

        Button(
            onClick = {
                if(isPasswordVisible)
                    viewModel.passwordLoginButtonClicked(
                        activity,
                        viewModel.username.value,
                        viewModel.password.value
                    )
                else
                    viewModel.createPasskeyButtonClicked(activity)
            },
            modifier = Modifier
                .padding(16.dp)
                .size(width = 150.dp, height = 48.dp)
        ) {
            Text(text = "Sign in")
        }

        Text(
            text = "I want to create a password",
            textDecoration = TextDecoration.Underline,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.clickable {
                isPasswordVisible = true
            }
        )
    }
}

