package com.andreich.androidhelper.presentation.add_question

import com.andreich.androidhelper.core.componentScope
import com.andreich.androidhelper.domain.model.Question
import com.andreich.androidhelper.domain.model.SubType
import com.andreich.androidhelper.domain.model.SubjectType
import com.andreich.androidhelper.presentation.QuestionUi
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultAddQuestionComponent @Inject constructor(
    componentContext: ComponentContext,
    private val storeFactory: AddQuestionStoreFactory,
    val onSaveQuestion: () -> Unit
) : AddQuestionComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AddQuestionStore.State>
        get() = store.stateFlow

    init {
        componentScope().launch {
            store.labels.collect {
                when(it) {
                    AddQuestionStore.Label.SaveSuccessful -> {
                        onSaveQuestion()
                    }
                }
            }
        }
    }

    override fun onSaveClick(question: QuestionUi) {
        store.accept(AddQuestionStore.Intent.SaveQuestion(question))
    }

    override fun onAnswerChange(answer: String) {
        store.accept(AddQuestionStore.Intent.ChangeAnswer(answer))
    }

    override fun onQuestionTitleChange(title: String) {
        store.accept(AddQuestionStore.Intent.ChangeQuestionTitle(title))
    }

    override fun onTypeChange(type: String) {
        store.accept(AddQuestionStore.Intent.ChangeType(type))
    }

    override fun onSubjectChange(subjectType: String) {
        store.accept(AddQuestionStore.Intent.ChangeSubject(subjectType))
    }
}