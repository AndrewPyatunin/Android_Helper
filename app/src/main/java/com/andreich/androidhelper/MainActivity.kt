package com.andreich.androidhelper

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andreich.androidhelper.presentation.DefaultRootComponent
import com.andreich.androidhelper.presentation.add_question.AddQuestionStoreFactory
import com.andreich.androidhelper.presentation.game_result_screen.GameResultStoreFactory
import com.andreich.androidhelper.presentation.game_screen.GameStoreFactory
import com.andreich.androidhelper.presentation.home_screen.HomeStoreFactory
import com.andreich.androidhelper.ui.RootContent
import com.andreich.androidhelper.ui.theme.AndroidHelperTheme
import com.arkivanov.decompose.defaultComponentContext
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    private val component by lazy { (application as MyApp).component }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("MainActivity", throwable.message, throwable)
    }

    @Inject
    lateinit var gameStoreFactory: GameStoreFactory

    @Inject
    lateinit var addQuestionStoreFactory: AddQuestionStoreFactory

    @Inject
    lateinit var homeStoreFactory: HomeStoreFactory

    @Inject
    lateinit var gameResultStoreFactory: GameResultStoreFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        val rootComponent = DefaultRootComponent(gameStoreFactory, addQuestionStoreFactory, homeStoreFactory, gameResultStoreFactory, defaultComponentContext())
        setContent {
            AndroidHelperTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootContent(rootComponent)
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
    AndroidHelperTheme {
        Greeting("Android")
    }
}