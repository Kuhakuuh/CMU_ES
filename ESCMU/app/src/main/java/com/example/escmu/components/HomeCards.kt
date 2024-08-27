package com.example.escmu.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.escmu.R


@Composable
fun OverviewCards(
    expense:Int,
    revenue:Int,
    ){

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ElevatedCard(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                        Text(
                            text = "Expenses" ,
                            fontWeight= FontWeight.Bold,
                            fontSize = 30.sp)


                    Row(
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = expense.toString() ,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp)
                        Spacer(modifier = Modifier.padding(5.dp))
                        Icon(painter = painterResource(id = R.drawable.baseline_euro_24), contentDescription ="euro icon" )
                    }
                }

            }
        }

        ElevatedCard(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    Text(
                        text = "Revenues" ,
                        fontWeight= FontWeight.Bold,
                        fontSize = 30.sp)


                    Row(
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = revenue.toString() ,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp)
                        Spacer(modifier = Modifier.padding(5.dp))
                        Icon(painter = painterResource(id = R.drawable.baseline_euro_24), contentDescription ="euro icon" )
                    }
                }
            }
        }
    }

}