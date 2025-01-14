package com.andreich.androidhelper.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreich.androidhelper.R
import com.andreich.androidhelper.presentation.game_screen.AnswerCard
import com.andreich.androidhelper.presentation.game_screen.GameScreenComponent

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
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                AnswerCard(
                    answer = answers[0],
                    onAnswerClickListener = { component.onAnswerClick(it, question.answer, model.excludedIds) },
                    isClickable = model.isClickable
                )

                AnswerCard(
                    answer = answers[1],
                    onAnswerClickListener = { component.onAnswerClick(it, question.answer, model.excludedIds) },
                    isClickable = model.isClickable
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                AnswerCard(
                    answer = answers[2],
                    onAnswerClickListener = { component.onAnswerClick(it, question.answer, model.excludedIds) },
                    isClickable = model.isClickable
                )
                AnswerCard(
                    answer = answers[3],
                    onAnswerClickListener = { component.onAnswerClick(it, question.answer, model.excludedIds) },
                    isClickable = model.isClickable
                )
            }
            Spacer(modifier = Modifier.padding(24.dp))
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), elevation = CardDefaults.cardElevation(8.dp)) {
                Text(stringResource(R.string.remaining_time, model.remainTime), fontSize = 20.sp, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun RowScope.AnswerCard(
    answer: AnswerCard,
    onAnswerClickListener: (String) -> Unit,
    isClickable: Boolean,
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp)
            .clickable(enabled = isClickable) {
                onAnswerClickListener(answer.title)

            }, colors = CardDefaults.cardColors(containerColor = answer.color)
    ) {
        Text(modifier = Modifier.padding(4.dp), text = answer.title)
    }
}