package com.nirmal.aidetectorapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nirmal.aidetectorapp.ApiResponse
import com.nirmal.aidetectorapp.RetrofitClient
import com.nirmal.aidetectorapp.TextRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun TextDetectionScreen() {
    var inputText by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AI Text Detector",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter text to analyze") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5,
            maxLines = 10
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                isLoading = true
                RetrofitClient.api.detectText(TextRequest(inputText))
                    .enqueue(object : Callback<ApiResponse> {
                        override fun onResponse(
                            call: Call<ApiResponse>,
                            response: Response<ApiResponse>
                        ) {
                            isLoading = false
                            if (response.isSuccessful) {
                                val prediction = response.body()?.result?.get(0)
                                resultText = "${prediction?.label} (Confidence: ${(prediction?.score ?: 0f) * 100}%)"
                            } else {
                                resultText = "API Error: ${response.code()}"
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                            isLoading = false
                            resultText = "Connection Failed: ${t.message}"
                        }
                    })
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = inputText.isNotBlank() && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Analyze Text")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (resultText.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (resultText.contains("AI")) 
                        MaterialTheme.colorScheme.errorContainer 
                    else 
                        MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = resultText,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
