package com.mkm.contenthubpro.ui
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mkm.contenthubpro.viewmodel.VideoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState

@Composable
fun ReelScreen(viewModel: VideoViewModel) {
    val videos by viewModel.videos.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { videos.size })
    val coroutineScope = rememberCoroutineScope()

    val randomNames = listOf(
        "Urban Vibes",
        "Daily Highlights",
        "Creative Moment",
        "Inspiration Clip",
        "Tech Insight",
        "Lifestyle Snap",
        "Cinematic Shot",
        "Motivation Reel",
        "Skill Showcase",
        "Quick Insight"
    )


    if (videos.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading reels from storage...", color = Color.White)
        }
    } else {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val video = videos[page]
            val isPlaying = pagerState.currentPage == page

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                ReelPlayer(videoUri = video.path, isPlaying = isPlaying)

                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                            )
                        )
                )

                // Right-side icons
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Like", tint = Color.White)
                    }
                    Text("123K", color = Color.White, fontSize = 12.sp)

                    Spacer(Modifier.height(20.dp))

                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Email, contentDescription = "Comment", tint = Color.White)
                    }
                    Text("876", color = Color.White, fontSize = 12.sp)

                    Spacer(Modifier.height(20.dp))

                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Star, contentDescription = "Views", tint = Color.White)
                    }
                    Text("2.1M", color = Color.White, fontSize = 12.sp)
                }

                // Bottom-left title
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = randomNames[page % randomNames.size], // use 'page', not 'index'
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Reel #${page + 1}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

            }
        }
    }
}
