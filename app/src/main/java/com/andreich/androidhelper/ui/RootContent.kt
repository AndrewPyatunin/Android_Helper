package com.andreich.androidhelper.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.andreich.androidhelper.presentation.RootComponent
import com.andreich.androidhelper.ui.theme.AndroidHelperTheme
import com.arkivanov.decompose.extensions.compose.stack.Children

@Composable
fun RootContent(
    component: RootComponent
) {

    AndroidHelperTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Children(component.stack) {
                when (val instance = it.instance) {

                    is RootComponent.Child.AddQuestion -> {
                        AddQuestionScreen(instance.component)
                    }
                    is RootComponent.Child.BestResults -> TODO()
                    is RootComponent.Child.Game -> {
                        GameScreen(instance.component)
                    }

                    is RootComponent.Child.GameResult -> {
                        GameResultScreen(instance.component)
                    }
                    is RootComponent.Child.Home -> {
                        HomeScreen(instance.component)
                    }
                }
            }
        }
    }

}