package com.skiyman.moneykepper.utils

import BudgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.ListenableWorker.Result
import com.skiyman.moneykepper.data.view_model.BudgetViewModel

class BudgetReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("BudgetReceiver", "CALLED")
        val budgetManager = BudgetManager(context)
        val budgetViewModel = BudgetViewModel.getInstance(budgetManager)

        val strategy = BudgetManager.BudgetUpdateStrategy.DISTRIBUTE
        budgetViewModel?.updateForNewDay(strategy)
    }
}