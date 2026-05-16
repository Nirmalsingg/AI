package com.nirmal.aidetectorapp.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nirmal.aidetectorapp.ApiResponse
import com.nirmal.aidetectorapp.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@Composable
fun AppDetectionScreen() {
    var appUri by remember { mutableStateOf<Uri?>(null) }
    var resultText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val appPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        appUri = uri
        resultText = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AI Application Detector",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { appPickerLauncher.launch("application/vnd.android.package-archive") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select APK File")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (appUri != null) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Selected: ${appUri?.lastPathSegment}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isLoading = true
                    appUri?.let { uri ->
                        val file = File(uri.path ?: "")
                        val requestFile = file.asRequestBody("application/vnd.android.package-archive".toMediaTypeOrNull())
                        val body = MultipartBody.Part.createFormData("app", file.name, requestFile)

                        RetrofitClient.api.detectApp(body)
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
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Analyze Application")
                }
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
