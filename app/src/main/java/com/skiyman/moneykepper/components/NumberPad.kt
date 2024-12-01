import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skiyman.moneykepper.components.CategorySelector
import com.skiyman.moneykepper.data.db.Category


@Composable
fun NumberPad(onConfirm: (Float, Category) -> Unit, onDelete: () -> Unit) {
    var input: String by remember { mutableStateOf("") }
    val scroll: ScrollState = rememberScrollState(0)
    var selectedCategory by remember { mutableStateOf(Category.entries.first()) }

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Поле для ввода
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Right
        ) {
            Text(
                text = input.ifEmpty { "0" },
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .horizontalScroll(scroll)
            )
        }
        CategorySelector({category ->
            selectedCategory = category
            Log.d("CategorySelector", selectedCategory.toString())
        })

        // Клавиатура и кнопки управления
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Цифровая клавиатура
            Column(
                modifier = Modifier
                    .weight(3f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(
                    listOf("7", "8", "9"),
                    listOf("4", "5", "6"),
                    listOf("1", "2", "3"),
                    listOf("0", ".")
                ).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        row.forEachIndexed { index, text ->
                            NumberButton(
                                text = text,
                                onClick = {
                                    if (text == "." && input.contains(".")) {
                                        // Ничего не делаем, если точка уже есть
                                    } else if (input.contains(".") && input.split(".")[1].length > 1) {
                                        // Ничего не делаем, если после точки уже два символа
                                    } else if (text == "0" && input == "0") {
                                        // Ничего не делаем, если первый символ уже 0
                                    } else {
                                        input = if (input != "0") input + text  else text
                                    }
                                },
                                modifier = Modifier.weight(if (text == "0" && index == 0) 2f else 1f)
                            )
                        }
                    }
                }
            }

            // Управляющие кнопки
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                InteractionButton(
                    icon = Icons.Filled.Clear, // Иконка "Очистить" (крестик)
                    onClick = { input = "" },
                    modifier = Modifier
                )

                InteractionButton(
                    icon = Icons.Filled.ArrowBack, // Иконка "Удалить последний символ" (стрелка)
                    onClick = {
                        if (input.isNotEmpty()) {
                            input = input.dropLast(1)
                            onDelete()
                        }
                    },
                    modifier = Modifier
                )

                InteractionButton(
                    icon = Icons.Filled.Check, // Иконка "Подтвердить" (галочка)
                    onClick = {
                        onConfirm(input.toFloatOrNull() ?: 0f, selectedCategory)
                        input = "" // Очистка после подтверждения
                    },
                    modifier = Modifier.size(148.dp),
                    isHighlighted = true
                )
            }
        }
    }
}


@Composable
fun NumberButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier // Кастомный параметр
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .size(72.dp) // Квадратная кнопка
            .padding(2.dp),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary, // Основной цвет
            contentColor = MaterialTheme.colorScheme.onSecondary // Цвет текста/иконок
        )
    ) {
        Text(text = text, fontSize = 32.sp)
    }
}

@Composable
fun InteractionButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .size(72.dp)
            .padding(2.dp), // Пропорции кнопок
        shape = RoundedCornerShape(4.dp),
        colors = if (isHighlighted) {
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary, // Основной цвет
                contentColor = MaterialTheme.colorScheme.onPrimary // Цвет текста/иконок
            )
        } else {
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary, // Основной цвет
                contentColor = MaterialTheme.colorScheme.onSecondary // Цвет текста/иконок
            )
        }
    ) {
        Icon(imageVector = icon, contentDescription = null)
    }
}
