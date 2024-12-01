package com.skiyman.moneykepper.data.view_model

import BudgetManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.skiyman.moneykepper.data.db.Category

class BudgetViewModel(val budgetManager: BudgetManager) : ViewModel() {
    private val _dailyBudget = mutableStateOf(budgetManager.dailyBudget)
    private val _totalBudget = mutableStateOf(budgetManager.totalBudget)
    private val _remainingDays = mutableStateOf(budgetManager.remainingDays)

    val dailyBudget: State<Float> = _dailyBudget
    val totalBudget: State<Float> = _totalBudget
    val remainingDays: State<Int> = _remainingDays

    fun addExpense(amount: Float, category: Category) {
        budgetManager.addExpense(amount, category)
        _dailyBudget.value = budgetManager.dailyBudget
        _totalBudget.value = budgetManager.totalBudget
        _remainingDays.value = budgetManager.remainingDays
    }

    fun initialize(totalBudget: Float, daysAmount: Int) {
        budgetManager.initialize(totalBudget, daysAmount)
        _dailyBudget.value = budgetManager.dailyBudget
        _totalBudget.value = budgetManager.totalBudget
    }

    fun updateForNewDay(strategy: BudgetManager.BudgetUpdateStrategy) {
        budgetManager.updateForNewDay(strategy)
        _dailyBudget.value = budgetManager.dailyBudget
        _totalBudget.value = budgetManager.totalBudget
        _remainingDays.value = budgetManager.remainingDays
    }


    companion object {
        private var INSTANCE: BudgetViewModel? = null

        fun getInstance(budgetManager: BudgetManager): BudgetViewModel? {
            if (INSTANCE == null) {
                synchronized(BudgetViewModel::class) {
                    INSTANCE = BudgetViewModel(budgetManager)
                }
            }
            return INSTANCE
        }
    }
}