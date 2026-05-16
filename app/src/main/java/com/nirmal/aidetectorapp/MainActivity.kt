package com.nirmal.aidetectorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.nirmal.aidetectorapp.ui.theme.AIDetectorAppTheme
import com.nirmal.aidetectorapp.ui.TextDetectionScreen
import com.nirmal.aidetectorapp.ui.VideoDetectionScreen
import com.nirmal.aidetectorapp.ui.AppDetectionScreen
import com.nirmal.aidetectorapp.ui.OtherDetectionScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AIDetectorAppTheme {
                AIDetectorApp()
            }
        }
    }
}

@Composable
fun AIDetectorApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Text", "Video", "Application", "Other")

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> TextDetectionScreen()
            1 -> VideoDetectionScreen()
            2 -> AppDetectionScreen()
            3 -> OtherDetectionScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    AIDetectorAppTheme {
        AIDetectorApp()
    }
}