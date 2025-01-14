package com.andreich.androidhelper.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreich.androidhelper.R
import com.andreich.androidhelper.presentation.game_result_screen.GameResultComponent

@Composable
fun GameResultScreen(component: GameResultComponent) {

    val model by component.model.collectAsState()

    val count = model.count
    val countRight = model.countRightAnswers
    val percent = model.rightPercent

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            stringResource(R.string.game_result), fontSize = 20.sp, modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally))
        Text(stringResource(R.string.game_question_count, count), fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        Text(stringResource(R.string.game_right_answer_count, countRight), fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        Text(
            stringResource(
                R.string.game_right_answer_percent,
                percent
            ), fontSize = 18.sp, modifier = Modifier.padding(8.dp))
    }
}