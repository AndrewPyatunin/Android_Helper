package com.andreich.androidhelper.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreich.androidhelper.R
import com.andreich.androidhelper.presentation.home_screen.HomeComponent

@Composable
fun HomeScreen(component: HomeComponent) {
    val model by component.model.collectAsState()
    val count = model.count

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Column {
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 8.dp, vertical = 4.dp)) {
                Text(
                    stringResource(R.string.who_want_to_be),
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Cursive,
                    fontSize = 22.sp
                )
            }
            Card(modifier = Modifier.padding(4.dp), elevation = CardDefaults.cardElevation(4.dp)) {
                Text(stringResource(R.string.question_count, count), modifier = Modifier.padding(4.dp))
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Button(onClick = {
                component.onStartAGameClick(count)
            }) {
                Text("Начать игру")
            }
        }
        FloatingActionButton(
            onClick = component::onAddQuestionClick,
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp)
        ) {
            Icon(Icons.Default.Add, "Add")
        }
    }
}