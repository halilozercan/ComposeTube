package com.halilibo.composetube

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.onCommit
import androidx.compose.remember
import androidx.compose.state
import androidx.ui.core.ContextAmbient
import androidx.ui.core.setContent
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.material.Button
import androidx.ui.material.IconButton
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.ArrowBack
import androidx.ui.material.icons.filled.ArrowForward
import androidx.ui.material.icons.filled.PlayArrow
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
                val (source, setSource) = state {
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                }

                Column {
                    val mediaPlayback = VideoPlayer(source)

                    Button(onClick = {
                        // Elephant Dream by Blender Foundation
                        setSource("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
                    }) {
                        Text("Another Video")
                    }

                    Row {
                        IconButton(onClick = {
                            mediaPlayback.rewind(10_000)
                        }) {
                            Icon(Icons.Filled.ArrowBack)
                        }

                        IconButton(onClick = {
                            mediaPlayback.playPause()
                        }) {
                            Icon(Icons.Filled.PlayArrow)
                        }

                        IconButton(onClick = {
                            mediaPlayback.forward(10_000)
                        }) {
                            Icon(Icons.Filled.ArrowForward)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun VideoPlayer(sourceUrl: String): MediaPlayback {
    // This is the official way to access current context from Composable functions
    val context = ContextAmbient.current

    // Do not recreate the player everytime this Composable commits
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build()
    }

    // We only want to react to changes in sourceUrl.
    // This callback will be execute at each commit phase if
    // [sourceUrl] has changed.
    onCommit(sourceUrl) {
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(context,
            Util.getUserAgent(context, context.packageName))

        val source = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(
                // Big Buck Bunny from Blender Project
                sourceUrl
            ))

        exoPlayer.prepare(source)
    }

    AndroidView(resId = R.layout.surface) {
        val exoPlayerView = it.findViewById<PlayerView>(R.id.player_view)

        exoPlayerView.player = exoPlayer
        exoPlayer.playWhenReady = true
    }

    return object: MediaPlayback {
        override fun playPause() {
            exoPlayer.playWhenReady = !exoPlayer.playWhenReady
        }

        override fun forward(durationInMillis: Long) {
            exoPlayer.seekTo(exoPlayer.currentPosition + durationInMillis)
        }

        override fun rewind(durationInMillis: Long) {
            exoPlayer.seekTo(exoPlayer.currentPosition - durationInMillis)
        }

    }
}

interface MediaPlayback {

    fun playPause()

    fun forward(durationInMillis: Long)

    fun rewind(durationInMillis: Long)
}