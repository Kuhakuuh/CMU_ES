package com.example.escmu.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun TabRowProfile() {
    var periodIndex by remember {
        mutableStateOf(0)
    }
    val periodLabels = listOf(
        "Info",
        "MyExpenses",
    )

    Column {
        // TabRow com as abas
        TabRow(
            selectedTabIndex = periodIndex,
            indicator = { tabPositions ->
                if (periodIndex < tabPositions.size) {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[periodIndex]),
                        shape = RoundedCornerShape(
                            topStart = 3.dp,
                            topEnd = 3.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp,
                        ),
                    )
                }
            },
        ) {
            periodLabels.forEachIndexed { index, title ->
                Tab(
                    selected = periodIndex == index,
                    onClick = {
                        periodIndex = index
                    },
                    text = {
                        Text(
                            text = title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                )
            }
        }
        // Conteúdo baseado na aba selecionada
        when (periodIndex) {
            0 -> {
               // InfoTabContent()
            }
            1 -> {
               // MyExpensesTabContent()
            }
        }
    }
}