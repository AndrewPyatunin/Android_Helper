package com.andreich.androidhelper.presentation.add_question

import com.andreich.androidhelper.domain.model.Question
import com.andreich.androidhelper.domain.model.SubType
import com.andreich.androidhelper.domain.model.SubjectType
import com.andreich.androidhelper.domain.usecase.InsertQuestionUseCase
import com.andreich.androidhelper.presentation.AnswerType
import com.andreich.androidhelper.presentation.QuestionUi
import com.andreich.androidhelper.presentation.add_question.AddQuestionStore.Intent
import com.andreich.androidhelper.presentation.add_question.AddQuestionStore.Label
import com.andreich.androidhelper.presentation.add_question.AddQuestionStore.State
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddQuestionStoreFactory @Inject constructor(
    storeFactory: StoreFactory,
    private val insertQuestionUseCase: InsertQuestionUseCase
) {

    fun create(): AddQuestionStore = object : AddQuestionStore by store {}

    private val store: AddQuestionStore =
        object : AddQuestionStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AddQuestionStore",
            initialState = State("", "", Type_Types, Subject_Java, "", "", ""),
            bootstrapper = null,
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl,
        ) {}

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Message, Label>() {

        private fun String.mapSubType(): SubType? {
            return when (this) {
                Type_Collections -> SubType.Collections
                Type_Functions -> SubType.Functions
                Type_Generic -> SubType.Generic
                Type_Objects -> SubType.Objects
                Type_Types -> SubType.Types
                else -> null
            }
        }

        private fun String.mapSubjectType(): SubjectType {
            return when (this) {
                Subject_AndroidSdk -> SubjectType.AndroidSdk
                Subject_Java -> SubjectType.Java
                Subject_Kotlin -> SubjectType.Kotlin
                else -> throw RuntimeException()
            }
        }

        private fun QuestionUi.mapQuestion(): Question {
            return Question(
                id = System.currentTimeMillis(),
                title = title,
                answer = answer,
                subject = subject.mapSubjectType(),
                subType = subType.mapSubType(),
                wrongAnswer1 = wrongAnswer1,
                wrongAnswer2 = wrongAnswer2,
                wrongAnswer3 = wrongAnswer3
            )
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.SaveQuestion -> {
                    scope.launch {
                        val unit = insertQuestionUseCase(intent.question.mapQuestion())
                        unit.run {
                            publish(Label.SaveSuccessful)
                        }

                    }
                }

                is Intent.ChangeAnswer -> {
                    dispatch(Message.ChangeAnswer(intent.answer, intent.answerType))
                }

                is Intent.ChangeQuestionTitle -> {
                    dispatch(Message.ChangeQuestionTitle(intent.title))
                }

                is Intent.ChangeSubject -> {
                    dispatch(Message.ChangeSubject(intent.subjectType))
                }

                is Intent.ChangeType -> {
                    dispatch(Message.ChangeType(intent.type))
                }

                is Intent.TryToSave -> {

                }
            }
        }
    }


    object ReducerImpl : Reducer<State, Message> {

        override fun State.reduce(msg: Message): State = when (msg) {
            is Message.ChangeAnswer -> {
                when(msg.answerType) {
                    AnswerType.RIGHT -> copy(answer = msg.answer)
                    AnswerType.WRONG_1 -> copy(wrongAnswer1 = msg.answer)
                    AnswerType.WRONG_2 -> copy(wrongAnswer2 = msg.answer)
                    AnswerType.WRONG_3 -> copy(wrongAnswer3 = msg.answer)
                }
            }

            is Message.ChangeQuestionTitle -> {
                copy(questionTitle = msg.title)
            }

            is Message.ChangeSubject -> {
                copy(subjectType = msg.subjectType)
            }

            is Message.ChangeType -> {
                copy(type = msg.type)
            }
        }
    }

    sealed interface Message {

        class ChangeQuestionTitle(val title: String) : Message

        class ChangeAnswer(val answer: String, val answerType: AnswerType) : Message

        class ChangeType(val type: String) : Message

        class ChangeSubject(val subjectType: String) : Message
    }

    sealed interface Action
}