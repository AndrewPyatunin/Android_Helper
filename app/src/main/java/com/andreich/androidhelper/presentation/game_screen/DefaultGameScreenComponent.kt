package com.andreich.androidhelper.presentation.game_screen

import com.andreich.androidhelper.core.componentScope
import com.andreich.androidhelper.domain.model.Question
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class DefaultGameScreenComponent @Inject constructor(
    componentContext: ComponentContext,
    private val question: Question,
    private val answers: List<Question>,
    val onAnswerClick: (answerId: Int, questionId: Int) -> Unit,
) : GameScreenComponent, ComponentContext by componentContext {

    private val coroutineScope = componentScope()

    private val _model = MutableStateFlow(
        stateKeeper.consume(KEY, GameScreenComponent.Model.serializer())
            ?: GameScreenComponent.Model(question.id, question.title, answers)
    )

    override val model: StateFlow<GameScreenComponent.Model>
        get() = _model.asStateFlow()

    init {
        stateKeeper.register(
            key = KEY,
            strategy = GameScreenComponent.Model.serializer()
        ) {
            model.value
        }
    }

    override fun onAnswerClick(answerId: Int) {
        onAnswerClick(answerId, question.id)
    }

    private companion object {

        private const val KEY = "DefaultGameScreenComponent"

    }
}