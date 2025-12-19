package com.mkm.contenthubpro.ui

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun ReelPlayer(
    videoUri: String,
    isPlaying: Boolean
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Create ExoPlayer instance for each video
    val exoPlayer = remember(videoUri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(videoUri)))
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
            prepare()
        }
    }

    // Track manual pause (tap)
    var isUserPaused by remember { mutableStateOf(false) }

    // Play / pause based on visibility and user input
    LaunchedEffect(isPlaying, isUserPaused) {
        if (isPlaying && !isUserPaused) {
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }

    // ✅ Pause or stop when app loses focus or backgrounded
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    // App not in focus or screen off
                    exoPlayer.pause()
                }
                Lifecycle.Event.ON_STOP -> {
                    // App fully backgrounded (or user pressed back/home)
                    exoPlayer.stop()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    // Cleanup
                    exoPlayer.release()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    // ✅ Handle back button press (pause/stop video before leaving)
    BackHandler {
        exoPlayer.stop()
        exoPlayer.release()
    }

    // Tap to play/pause
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    isUserPaused = !isUserPaused
                })
            }
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    keepScreenOn = true // ✅ Prevent sleep while playing
                }
            }
        )
    }
}
