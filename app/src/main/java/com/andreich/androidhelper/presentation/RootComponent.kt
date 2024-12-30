package com.andreich.androidhelper.presentation

import com.andreich.androidhelper.presentation.add_question.AddQuestionComponent
import com.andreich.androidhelper.presentation.game_screen.GameScreenComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        class Home : Child

        class Game(val component: GameScreenComponent) : Child

        class GameResult : Child

        class AddQuestion(val component: AddQuestionComponent) : Child

        class BestResults : Child
    }
}