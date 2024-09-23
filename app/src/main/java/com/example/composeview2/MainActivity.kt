package com.example.composeview2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeview2.ui.theme.COMPOSEVIEW2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            COMPOSEVIEW2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun LoginScreen() {
    var name by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var submittedData by remember { mutableStateOf<Pair<String, String>?>(null) }

    val context = LocalContext.current

    val isFormValid = name.isNotBlank() && nim.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Name Icon",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(
                        BorderStroke(1.dp, Color.Gray),
                        RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp), // Rounded corners
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            TextField(
                value = nim,
                onValueChange = { nim = it },
                label = { Text("NIM") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock, // Using Lock icon as a placeholder
                        contentDescription = "NIM Icon",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(
                        BorderStroke(1.dp, Color.Gray),
                        RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp), // Rounded corners
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    // Display the submitted data on screen when the button is clicked
                    submittedData = Pair(name, nim)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = {
                            // Display a Toast with name and nim on long click
                            Toast.makeText(context, "Name: $name\nNIM: $nim", Toast.LENGTH_LONG).show()
                        }
                    ),
                shape = RoundedCornerShape(24.dp),
                enabled = isFormValid,  // Enable or disable the button based on form validity
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) Color(0xFF6C63FF) else Color.Gray // Change color based on form validity
                )
            ) {
                Text(
                    "Submit",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Display the submitted data if available
            submittedData?.let { (name, nim) ->
                Text(
                    text = "Submitted Data: \nName: $name \nNIM: $nim",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    COMPOSEVIEW2Theme {
        LoginScreen()
    }
}
