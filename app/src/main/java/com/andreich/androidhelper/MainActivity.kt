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
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.andreich.androidhelper.domain.model.Question
import com.andreich.androidhelper.domain.model.SubType
import com.andreich.androidhelper.domain.model.SubjectType
import com.andreich.androidhelper.presentation.DefaultRootComponent
import com.andreich.androidhelper.presentation.game_screen.GameScreenStore
import com.andreich.androidhelper.ui.RootContent
import com.andreich.androidhelper.ui.theme.AndroidHelperTheme
import com.arkivanov.decompose.defaultComponentContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import ru.tinkoff.kotea.android.storeViaViewModel
import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Store
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    private val component by lazy { (application as MyApp).component }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("MainActivity", throwable.message, throwable)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        val question = Question(0, "", SubjectType.Kotlin, "", SubType.Types)
        val component = DefaultRootComponent(defaultComponentContext())
        setContent {
            AndroidHelperTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootContent(component)
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