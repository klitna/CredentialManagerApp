package com.example.mycredentialmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.mycredentialmanager.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: LoginViewModel, activity: androidx.activity.ComponentActivity) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login with passkey",
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = viewModel.username.value,
            onValueChange = { newUsername -> viewModel.updateUsername(newUsername) },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Button(
            onClick = { viewModel.loginButtonClicked(activity) },
            modifier = Modifier
                .fillMaxWidth()
                .size(width = 90.dp, height = 48.dp)
                .background(color = Color.Green)
        ) {
            Text(text = "Create passkey")
        }
        Button(
            onClick = { viewModel.loginButtonClicked(activity) },
            modifier = Modifier
                .fillMaxWidth()
                .size(width = 90.dp, height = 48.dp)
                .background(color = Color.Green)
        ) {
            Text(text = "Log in with a passkey")
        }
    }

}

