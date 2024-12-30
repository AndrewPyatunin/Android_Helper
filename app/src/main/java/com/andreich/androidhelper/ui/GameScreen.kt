package com.andreich.androidhelper.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.andreich.androidhelper.domain.model.Question
import com.andreich.androidhelper.presentation.game_screen.GameScreenComponent
import kotlinx.coroutines.CoroutineExceptionHandler


private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Log.e("MainActivity", throwable.message, throwable)
}

@Composable
fun GameScreen(component: GameScreenComponent) {

    val model by component.model.collectAsState()
    val answers = model.answers

    model.question?.let { question ->
        Column(modifier = Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = question.title
                ) //"Основа объектно ориентированного программирования")
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                AnswerCard(
                    rightId = question.id,
                    answer = answers[0],
                    onAnswerClickListener = { component.onAnswerClick(question.id, it.id) })

                AnswerCard(
                    rightId = question.id,
                    answer = answers[1],
                    onAnswerClickListener = { component.onAnswerClick(question.id, it.id) })
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                AnswerCard(
                    rightId = question.id,
                    answer = answers[2],
                    onAnswerClickListener = { component.onAnswerClick(question.id, it.id) })
                AnswerCard(
                    rightId = question.id,
                    answer = answers[3],
                    onAnswerClickListener = { component.onAnswerClick(question.id, it.id) })
            }
        }
    }


}

@Composable
fun RowWithTwoCards(
    rightId: Long,
    answers: List<Question>,
    onAnswerClickListener: (Question) -> Unit
) {

    Row(modifier = Modifier.fillMaxWidth()) {
        AnswerCard(
            rightId = rightId,
            answer = answers[0],
            onAnswerClickListener = onAnswerClickListener
        )
        AnswerCard(
            rightId = rightId,
            answer = answers[1],
            onAnswerClickListener = onAnswerClickListener
        )
    }
}

@Composable
fun RowScope.AnswerCard(
    rightId: Long,
    answer: Question,
    onAnswerClickListener: (Question) -> Unit,
) {
    val color = remember { mutableStateOf(Color.White) }
    Card(modifier = Modifier
        .weight(1f)
        .padding(horizontal = 4.dp)
        .clickable {
            onAnswerClickListener(answer)
            color.value = if (rightId == answer.id) Color.Green else Color.Red
        }, colors = CardDefaults.cardColors(containerColor = color.value)
    ) {
        Text(modifier = Modifier.padding(4.dp), text = answer.answer)
    }
}