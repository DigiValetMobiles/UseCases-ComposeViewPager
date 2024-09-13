package com.paragon.composechallenges.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paragon.composechallenges.EMPTY

@Preview
@Composable
fun Counter(onClick: (Int) -> Unit = {}) {
    var count by remember { mutableIntStateOf(1) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            Icon(
                modifier = Modifier.clickable {
                    if (count > 1) {
                        count--
                        onClick(count)
                    }
                },
                imageVector = Icons.Default.Remove,
                contentDescription = EMPTY
            )

            Text(
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(2.dp),
                text = "$count",
                fontSize = 20.sp
            )
            Icon(
                modifier = Modifier.clickable {
                    count++
                    onClick(count)
                },
                imageVector = Icons.Default.Add,
                contentDescription = EMPTY
            )
        }
    }
}