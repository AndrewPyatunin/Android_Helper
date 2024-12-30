package com.andreich.androidhelper.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.andreich.androidhelper.domain.model.SubType
import com.andreich.androidhelper.domain.model.SubjectType
import com.andreich.androidhelper.presentation.QuestionUi
import com.andreich.androidhelper.presentation.add_question.AddQuestionComponent
import com.andreich.androidhelper.presentation.add_question.Subject_AndroidSdk
import com.andreich.androidhelper.presentation.add_question.Subject_Java
import com.andreich.androidhelper.presentation.add_question.Subject_Kotlin
import com.andreich.androidhelper.presentation.add_question.Type_Collections
import com.andreich.androidhelper.presentation.add_question.Type_Functions
import com.andreich.androidhelper.presentation.add_question.Type_Generic
import com.andreich.androidhelper.presentation.add_question.Type_Objects
import com.andreich.androidhelper.presentation.add_question.Type_Types

@Composable
fun AddQuestionScreen(component: AddQuestionComponent) {

    val model by component.model.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        CardWithEditText(
            modifier = Modifier.fillMaxWidth(),
            text = model.questionTitle,
            onValueChange = component::onQuestionTitleChange,
            label = "Вопрос",
        )
        CardWithEditText(
            modifier = Modifier.fillMaxWidth(),
            text = model.answer,
            onValueChange = component::onAnswerChange,
            label = "Ответ",
        )
        SpinnerSample(
            listOf(Subject_Java, Subject_Kotlin, Subject_AndroidSdk),
            Subject_Java,
            onSelectionChanged = component::onSubjectChange,
        )
        SpinnerSample(
            listOf(Type_Types, Type_Objects, Type_Functions, Type_Collections, Type_Generic),
            Type_Types,
            onSelectionChanged = component::onTypeChange
        )
        Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = { component.onSaveClick(QuestionUi(System.currentTimeMillis(), model.questionTitle, model.subjectType, model.answer, model.type)) }) {
            Text("Сохранить")
        }
    }
}

fun mapSubType(type: SubType): String {
    return when(type) {
        SubType.Collections -> Type_Collections
        SubType.Functions -> Type_Functions
        SubType.Generic -> Type_Generic
        SubType.Objects -> Type_Objects
        SubType.Types -> Type_Types
    }
}

fun mapSubjectType(subject: SubjectType): String {
    return when(subject) {
        is SubjectType.AndroidSdk -> Subject_AndroidSdk
        SubjectType.Java -> Subject_Java
        SubjectType.Kotlin -> Subject_Kotlin
    }
}


@Composable
fun SpinnerSample(
    list: List<String>,
    preselected: String,
    onSelectionChanged: (myData: String) -> Unit,
    modifier: Modifier = Modifier,
) {
//    val listElements = when(preselected) {
//        is SubType -> {
//            (list as List<SubType>).map {
//                mapSubType(it)
//            }
//        }
//        is SubjectType -> {
//            (list as List<SubjectType>).map {
//                mapSubjectType(it)
//            }
//        }
//        else -> {
//            throw RuntimeException()
//        }
//    }

    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) } // initial value

    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        }
    ) {


        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {

            Text(
                text = selected,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()   // delete this modifier and use .wrapContentWidth() if you would like to wrap the dropdown menu around the content
            ) {
                list.forEach { listEntry ->

                    DropdownMenuItem(
                        onClick = {
                            selected = listEntry
                            expanded = false
                            onSelectionChanged(selected)
                        },
                        text = {
                            Text(
                                text = listEntry,
                                modifier = Modifier
                                    //.wrapContentWidth()  //optional instead of fillMaxWidth
                                    .fillMaxWidth()
                                    .align(Alignment.Start)
                            )
                        },
                    )
                }
            }

        }
    }
}

@Composable
fun CardWithEditText(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    Card(modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            label = { Text(label) }
        )
    }
}