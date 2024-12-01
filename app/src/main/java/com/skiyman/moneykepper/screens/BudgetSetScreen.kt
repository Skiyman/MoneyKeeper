import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skiyman.moneykepper.data.view_model.BudgetViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetSetScreen(
    onSave: (Float, Int) -> Unit,
    onCancel: () -> Unit,
    onClearStatistics: () -> Map<String, Float>,
    statsByCategory: Map<String, Float>,
    modifier: Modifier,
) {
    var budget by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(1) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var statsByCategoryView by remember { mutableStateOf(statsByCategory) }
    val focusManager = LocalFocusManager.current

    val dateOptions = List(40) { days ->
        val date = LocalDate.now().plusDays(days.toLong())
        days + 1 to if (days == 0) "Сегодня - ${days + 1} Дней" else "${date.toString()} - ${days + 1} Дней"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Заголовок
        Text(
            text = "Настройка бюджета",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = budget,
            onValueChange = { value ->
                if (value.matches(Regex("\\d*\\.?\\d{0,2}"))) {
                    budget = value
                }
            },
            label = { Text("Укажите бюджет") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done // Определяет действие по завершению ввода
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Выбор даты через селектор
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Text(text = "Дата окончания:", style = MaterialTheme.typography.labelMedium)

            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = dateOptions.first { it.first == selectedDate }.second,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Выберите дату") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                    modifier = Modifier.menuAnchor() // Это важно для работы DropdownMenu
                )

                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    dateOptions.forEach { (date, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                selectedDate = date
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }


        // Статистика по категориям
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Статистика трат по категориям:",
                style = MaterialTheme.typography.labelMedium
            )
            statsByCategoryView.forEach { (category, amount) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = category, fontSize = 16.sp)
                    Text(text = "$amount ₽", fontSize = 16.sp)
                }
            }
            TextButton(
                onClick = {
                    statsByCategoryView = onClearStatistics()
                          } ,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Очистить статистику")
            }
        }

        // Кнопки управления
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = { onSave(budget.toFloatOrNull() ?: 0f, selectedDate) },
                enabled = budget.isNotEmpty()
            ) {
                Text("Сохранить")
            }
            Button(onClick = onCancel, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)) {
                Text("Отменить")
            }
        }
    }
}
