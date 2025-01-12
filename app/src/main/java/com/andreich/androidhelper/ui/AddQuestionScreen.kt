package com.andreich.androidhelper.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreich.androidhelper.R
import com.andreich.androidhelper.presentation.AnswerType
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
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.add_new_question), fontSize = 22.sp)
        }
        CardWithEditText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            text = model.questionTitle,
            onValueChange = component::onQuestionTitleChange,
            label = stringResource(R.string.question),
        )
        CardWithEditText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            text = model.answer,
            onValueChange = { component.onAnswerChange(it, AnswerType.RIGHT) },
            label = stringResource(R.string.answer),
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            stringResource(R.string.wrong_answers),
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
        CardWithEditText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            text = model.wrongAnswer1,
            onValueChange = { component.onAnswerChange(it, AnswerType.WRONG_1) },
            label = "Ответ №1"
        )
        CardWithEditText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            text = model.wrongAnswer2,
            onValueChange = { component.onAnswerChange(it, AnswerType.WRONG_2) },
            label = "Ответ №2"
        )
        CardWithEditText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            text = model.wrongAnswer3,
            onValueChange = { component.onAnswerChange(it, AnswerType.WRONG_3) },
            label = "Ответ №3"
        )
        SpinnerSample(
            listOf(Subject_Java, Subject_Kotlin, Subject_AndroidSdk),
            Subject_Java,
            onSelectionChanged = component::onSubjectChange,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        SpinnerSample(
            listOf(Type_Types, Type_Objects, Type_Functions, Type_Collections, Type_Generic),
            Type_Types,
            onSelectionChanged = component::onTypeChange,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                if (model.questionTitle == "" || model.answer == "" || model.wrongAnswer1 == "" || model.wrongAnswer2 == "" || model.wrongAnswer3 == "") {
                    Toast.makeText(context,
                        context.getString(R.string.fill_in_fields), Toast.LENGTH_SHORT).show()
                } else {
                    component.onSaveClick(
                        QuestionUi(
                            System.currentTimeMillis(),
                            model.questionTitle,
                            model.subjectType,
                            model.answer,
                            model.type,
                            model.wrongAnswer1,
                            model.wrongAnswer2,
                            model.wrongAnswer3
                        )
                    )
                }
            }) {
            Text(stringResource(R.string.save))
        }
    }
}

@Composable
fun SpinnerSample(
    list: List<String>,
    preselected: String,
    onSelectionChanged: (myData: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) }

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
                modifier = Modifier.fillMaxWidth()
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
    Card(modifier, elevation = CardDefaults.cardElevation(4.dp)) {
        OutlinedTextField(
            modifier = Modifier.padding(5.dp),
            value = text,
            onValueChange = onValueChange,
            label = { Text(label) }
        )
    }
}