package com.andreich.androidhelper.presentation

import com.andreich.androidhelper.domain.model.Question
import com.andreich.androidhelper.domain.model.SubjectType
import com.andreich.androidhelper.presentation.game_screen.DefaultGameScreenComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.andreich.androidhelper.presentation.RootComponent.*
import kotlinx.serialization.Serializable
import javax.inject.Inject

class DefaultRootComponent @Inject constructor(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {


    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Home,
        serializer = null,
        handleBackButton = true,
        childFactory = ::child
    )

    @OptIn(DelicateDecomposeApi::class)
    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): Child {
        return when (config) {
            is Config.Game -> {
                val component = DefaultGameScreenComponent(
                    componentContext = componentContext,
                    question = Question(0, "", SubjectType.Java, ""),
                    answers = emptyList(),
                    onAnswerClick = { answerId, questionId ->
                        navigation.push(Config.GameResult)
                    }
                )
                Child.Game(component)
            }

            Config.AddQuestion -> TODO()
            Config.BestResults -> TODO()
            Config.GameResult -> TODO()
            Config.Home -> TODO()
        }
    }

    private sealed interface Config {

        @Serializable
        object Game : Config

        @Serializable
        object Home : Config

        @Serializable
        object GameResult : Config

        @Serializable
        object AddQuestion : Config

        @Serializable
        object BestResults : Config
    }
}