package com.halilibo.composetube

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.*
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.Spacer
import androidx.ui.layout.width
import androidx.ui.material.Button
import androidx.ui.unit.dp
import com.halilibo.composetube.ui.ComposeTubeTheme
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTubeTheme {
                Demo()
            }
        }
    }
}

// Simply put the Demo composable in a Compose tree and observe the logs
// Focus on how dependency changes affect the recomposition.
// How does Ambient work?
// When basic callbacks fire: onCommit, launchInComposition, onActive, remember

@Composable
fun Demo() {
    var textCounter by state { 0 }

    Column {
        TestComposable("Counter: $textCounter")

        Button(onClick = { textCounter++ }) {
            Text("Increase")
        }
    }
}

val ProvidableCounterAmbient = ambientOf { "default" }

@Composable
fun TestComposable(text: String) {
    // only first commit
    onActive {
        Log.d("ComposeCallbacks", "onActive $text")
    }

    // When this composer commit each time
    onCommit {
        Log.d("ComposeCallbacks", "onCommit $text")
    }

    launchInComposition {
        Log.d("ComposeCallbacks", "launched in composition")
        delay(2000)
        Log.d("ComposeCallbacks", "launched in composition after 2 seconds")
    }

    val textRemembered = remember { text }

    Log.d("ComposeCallbacks", "onCompose remembered $textRemembered")


    Providers(ProvidableCounterAmbient provides text) {
        Row {
            ShowTextAmbient()
            Spacer(modifier = Modifier.width(16.dp))
            ShowTextRegular(text)
        }
    }
}

@Composable
fun ShowTextAmbient() {
    onCommit {
        Log.d("ComposeCallbacks", "ShowTextAmbient commits")
    }
    ShowTextAmbient2()
}

@Composable
fun ShowTextAmbient2() {
    onCommit {
        Log.d("ComposeCallbacks", "ShowTextAmbient2 commits")
    }
    ShowTextAmbient3()
}

@Composable
fun ShowTextAmbient3() {
    onCommit {
        Log.d("ComposeCallbacks", "ShowTextAmbient3 commits")
    }
    Text(ProvidableCounterAmbient.current)
}


@Composable
fun ShowTextRegular(text: String) {
    onCommit {
        Log.d("ComposeCallbacks", "ShowTextRegular commits")
    }
    ShowTextRegular2(text)
}

@Composable
fun ShowTextRegular2(text: String) {
    onCommit {
        Log.d("ComposeCallbacks", "ShowTextRegular2 commits")
    }
    ShowTextRegular3(text)
}

@Composable
fun ShowTextRegular3(text: String) {
    onCommit {
        Log.d("ComposeCallbacks", "ShowTextRegular3 commits")
    }
    Text(text)
}