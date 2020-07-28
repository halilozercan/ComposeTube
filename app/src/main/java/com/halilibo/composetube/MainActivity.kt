package com.halilibo.composetube

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.tooling.preview.Preview
import com.halilibo.composetube.ui.ComposeTubeTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTubeTheme {

                val (count, setCount) = state { 1 }

                Surface(color = MaterialTheme.colors.background) {
                    Column {
                        Button(onClick = {
                            setCount(count+1)
                        }) {
                            Text("Add")
                        }
                        (1..count).forEach {
                            Greeting("Android")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTubeTheme {
        Greeting("Android")
    }
}