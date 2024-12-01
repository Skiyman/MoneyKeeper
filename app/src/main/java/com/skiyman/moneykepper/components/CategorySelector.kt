package com.skiyman.moneykepper.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skiyman.moneykepper.data.db.Category

@Composable
fun CategorySelector(onCategorySelected: (Category) -> Unit) {
    val categories = Category.entries
    var selectedCategory by remember { mutableStateOf(Category.entries.first()) }
    val scroll: ScrollState = rememberScrollState(0)

    Column (modifier = Modifier.padding(horizontal = 16.dp)) {
        Row (modifier = Modifier
            .padding(horizontal = 4.dp)
            .horizontalScroll(scroll),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
            categories.forEach { category ->
                Button(
                    onClick = {
                        selectedCategory = category
                        onCategorySelected(category)
                    },
                    colors = ButtonDefaults.run {
                        val buttonColors = buttonColors(
                            if (category == selectedCategory)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondary
                        )
                        buttonColors
                    }
                ) {
                    Text(text = category.name, fontSize = 14.sp)
                }
            }
        }
    }
}
