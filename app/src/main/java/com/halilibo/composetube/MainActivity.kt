package com.halilibo.composetube

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.remember
import androidx.ui.core.ContextAmbient
import androidx.ui.core.setContent
import androidx.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.halilibo.composetube.ui.ComposeTubeTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTubeTheme {
                VideoPlayer()
            }
        }
    }
}


@Composable
fun VideoPlayer() {
    // This is the official way to access current context from Composable functions
    val context = ContextAmbient.current

    // Do not recreate the player everytime this Composable commits
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(context,
                Util.getUserAgent(context, context.packageName))

            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(
                    // Big Buck Bunny from Blender Project
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                ))

            this.prepare(source)
        }
    }

    AndroidView(resId = R.layout.surface) {
        val exoPlayerView = it.findViewById<PlayerView>(R.id.player_view)

        exoPlayerView.player = exoPlayer
        exoPlayer.playWhenReady = true
    }
}