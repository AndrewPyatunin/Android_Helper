package com.andreich.androidhelper.presentation.add_question

import com.andreich.androidhelper.domain.model.Question
import com.andreich.androidhelper.domain.model.SubType
import com.andreich.androidhelper.domain.model.SubjectType
import com.andreich.androidhelper.domain.usecase.InsertQuestionUseCase
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

    private val store: Store<Intent, State, Label> =
        object : AddQuestionStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AddQuestionStore",
            initialState = State("", "", Type_Types, Subject_Java),
            bootstrapper = null,
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl,
        ) {}

    fun create(): AddQuestionStore = object : AddQuestionStore by store as AddQuestionStore {}

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
                subType = subType.mapSubType()
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
                    dispatch(Message.ChangeAnswer(intent.answer))
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
            }
        }
    }


    object ReducerImpl : Reducer<State, Message> {

        private fun SubType.mapSubType(): String {
            return when (this) {
                SubType.Collections -> Type_Collections
                SubType.Functions -> Type_Functions
                SubType.Generic -> Type_Generic
                SubType.Objects -> Type_Objects
                SubType.Types -> Type_Types
            }
        }

        private fun SubjectType.mapSubjectType(): String {
            return when (this) {
                is SubjectType.AndroidSdk -> Subject_AndroidSdk
                SubjectType.Java -> Subject_Java
                SubjectType.Kotlin -> Subject_Kotlin
            }
        }

        override fun State.reduce(msg: Message): State = when (msg) {
            is Message.ChangeAnswer -> {
                copy(answer = msg.answer)
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

        class ChangeAnswer(val answer: String) : Message

        class ChangeType(val type: String) : Message

        class ChangeSubject(val subjectType: String) : Message
    }

    sealed interface Action
}