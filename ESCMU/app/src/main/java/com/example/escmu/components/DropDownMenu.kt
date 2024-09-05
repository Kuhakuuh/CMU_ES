package com.example.escmu.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(){

    var list = listOf("one","two","three")
    var isExpanded by remember {mutableStateOf(false) }
    var selectText by remember { mutableStateOf(list[0])}

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange ={isExpanded=!isExpanded}
        ) {
            OutlinedTextField(
                modifier =Modifier.menuAnchor(),
                value =selectText ,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)}
            )
            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded=false }) {
                list.forEachIndexed{index,text ->
                    DropdownMenuItem(
                        text = { Text(text = text) },
                        onClick = {
                            selectText= list[index]
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )

                }

            }

        }
        Text(text = "Currently selected: $selectText" )




}