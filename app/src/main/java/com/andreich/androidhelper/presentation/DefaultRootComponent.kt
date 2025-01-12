package com.andreich.androidhelper.presentation

import com.andreich.androidhelper.presentation.game_screen.DefaultGameScreenComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.andreich.androidhelper.presentation.RootComponent.*
import com.andreich.androidhelper.presentation.add_question.AddQuestionStoreFactory
import com.andreich.androidhelper.presentation.add_question.DefaultAddQuestionComponent
import com.andreich.androidhelper.presentation.game_screen.GameStoreFactory
import com.andreich.androidhelper.presentation.home_screen.DefaultHomeComponent
import com.andreich.androidhelper.presentation.home_screen.HomeStoreFactory
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import kotlinx.serialization.Serializable
import javax.inject.Inject

class DefaultRootComponent @Inject constructor(
    private val gameStoreFactory: GameStoreFactory,
    private val addQuestionStoreFactory: AddQuestionStoreFactory,
    private val homeStoreFactory: HomeStoreFactory,
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {


    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Home,
        serializer = Config.serializer(),
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
                    storeFactory = gameStoreFactory,
                    count = config.count,
                    onRestart = navigation::pop
                ) {
                    navigation.pop()
                }
                Child.Game(component)
            }

            Config.AddQuestion -> {
                val component = DefaultAddQuestionComponent(
                    componentContext = componentContext,
                    storeFactory = addQuestionStoreFactory
                ) {
                    navigation.pop()
                }
                Child.AddQuestion(component)
            }
            Config.BestResults -> {
                throw Exception()
            }
            Config.GameResult -> {
                throw Exception()
            }
            Config.Home -> {
                val component = DefaultHomeComponent(
                    componentContext = componentContext,
                    storeFactory = homeStoreFactory,
                    onAddQuestionClicked = {
                        navigation.push(Config.AddQuestion)
                    },
                    onStartAGameClicked = {
                        navigation.push(Config.Game(it))
                    }
                )
                Child.Home(component)
            }
        }
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data class Game(val count: Int) : Config

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