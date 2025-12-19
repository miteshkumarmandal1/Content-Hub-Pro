package com.mkm.contenthubpro

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.mkm.contenthubpro.ui.theme.ContentHubProTheme
import com.mkm.contenthubpro.ui.ReelScreen
import com.mkm.contenthubpro.viewmodel.VideoViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: VideoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionToRequest =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_VIDEO
            else
                Manifest.permission.READ_EXTERNAL_STORAGE

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                viewModel.loadVideos(contentResolver)
            } else {
                // User denied permission
                println("⚠️ Permission denied — cannot load videos")
            }
        }

        // Request permission when activity starts
        permissionLauncher.launch(permissionToRequest)

        setContent {
            ContentHubProTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ReelScreen(viewModel)
                }
            }
        }
    }
}
