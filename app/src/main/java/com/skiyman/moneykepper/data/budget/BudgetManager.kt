import android.content.Context
import android.content.SharedPreferences
import com.skiyman.moneykepper.data.db.Category
import java.time.LocalDate
import kotlin.math.floor


class BudgetManager(context: Context) {
    private val budgetPrefs: SharedPreferences = context.getSharedPreferences("BudgetPrefs", Context.MODE_PRIVATE)
    private val categoryPrefs: SharedPreferences =
        context.getSharedPreferences("CategoryPrefs", Context.MODE_PRIVATE)


    private val totalBudgetKey = "total_budget"
    private val dailyBudgetKey = "daily_budget"
    private val lastUpdatedKey = "last_updated"
    private val remainingDaysKey = "remaining_days"

    var totalBudget: Float
        get() = (floor(budgetPrefs.getFloat(totalBudgetKey, 0f) * 100.0) / 100.0).toFloat()
        private set(value) {
            budgetPrefs.edit().putFloat(totalBudgetKey, value).apply()
        }

    var dailyBudget: Float
        get() = (floor(budgetPrefs.getFloat(dailyBudgetKey, 0f) * 100.0) / 100.0).toFloat()
        private set(value) {
            budgetPrefs.edit().putFloat(dailyBudgetKey, value).apply()
        }

    private var lastUpdated: String
        get() = budgetPrefs.getString(lastUpdatedKey, LocalDate.now().toString())!!
        private set(value) {
            budgetPrefs.edit().putString(lastUpdatedKey, value).apply()
        }

    var remainingDays: Int
        get() = budgetPrefs.getInt(remainingDaysKey, 1)
        private set(value) {
            budgetPrefs.edit().putInt(remainingDaysKey, value).apply()
        }

    fun initialize(totalBudget: Float, days: Int) {
        this.clear()
        this.totalBudget = totalBudget
        this.remainingDays = days
        this.dailyBudget = if (days != 0) totalBudget / days else totalBudget
        this.lastUpdated = LocalDate.now().toString()
    }

    private fun addExpenseCategory(category: String, amount: Float) {
        val currentTotal = categoryPrefs.getFloat(category, 0f)
        val newTotal = currentTotal + amount

        categoryPrefs.edit()
            .putFloat(category, newTotal)
            .apply()
    }

    fun addExpense(amount: Float, category: Category): Boolean {
        if (amount <= dailyBudget) {
            dailyBudget -= amount
            totalBudget -= amount

            addExpenseCategory(category = category.toString(), amount)

            return true
        }

        val remainingFromDaily = dailyBudget
        dailyBudget = 0f
        val fromTotal = amount - remainingFromDaily

        return if (fromTotal <= totalBudget) {
            totalBudget -= amount
            addExpenseCategory(category = category.toString(), amount)
            true
        } else {
            false // Expense exceeds available budget
        }
    }

    fun clearAllExpenses() {
        categoryPrefs.edit().clear().apply()
    }

    fun removeCategory(category: String) {
        categoryPrefs.edit().remove(category).apply()
    }


    fun getAllExpenses(): Map<String, Float> {
        val allExpenses = mutableMapOf<String, Float>()
        categoryPrefs.all.forEach { (key, value) ->
            if (value is Float) {
                allExpenses[key] = value
            }
        }
        return allExpenses
    }

    fun updateForNewDay(strategy: BudgetUpdateStrategy) {
        val today = LocalDate.now()
        if (lastUpdated == today.toString()) return // Already updated for today

        val remainingForToday = dailyBudget
        dailyBudget = when (strategy) {
            BudgetUpdateStrategy.ADD_TO_TODAY -> remainingForToday + totalBudget / remainingDays
            BudgetUpdateStrategy.DISTRIBUTE -> totalBudget / remainingDays
        }
        remainingDays = maxOf(remainingDays - 1, 1)
        lastUpdated = today.toString()
    }

    fun clear() {
        budgetPrefs.edit().clear().apply()
    }

    enum class BudgetUpdateStrategy {
        ADD_TO_TODAY,
        DISTRIBUTE
    }
}