package com.skiyman.moneykepper.screens

import BudgetManager
import NumberPad
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skiyman.moneykepper.data.view_model.BudgetViewModel

@Composable
fun HomeScreen(budget: BudgetManager, modifier: Modifier = Modifier) {
    val budgetViewModel = BudgetViewModel.getInstance(budget)
    Column (
        modifier = modifier.fillMaxHeight(),
    ) {
        Text(
            text = "Бюджет на сегодня",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )

        if (budgetViewModel != null) {
            BudgetText(budgetViewModel)
        }
        HorizontalDivider(thickness = 2.dp)

        NumberPad(
            onConfirm = { value, category ->
                if (budgetViewModel != null) {
                    budgetViewModel.addExpense(value, category)
                    Log.d("Test", budgetViewModel.dailyBudget.value.toString())
                    Log.d("Test", budget.getAllExpenses().toString())
                }
            },
            onDelete = { println("Delete pressed") }
        )
    }
}

@Composable
fun BudgetText(state: BudgetViewModel){
    Text(
        text = state.dailyBudget.value.toString(),
        fontSize = 46.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(16.dp)
    )

}
