import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skiyman.moneykepper.data.view_model.BudgetViewModel
import com.skiyman.moneykepper.screens.HomeScreen
import com.skiyman.moneykepper.utils.Routes

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val budget = BudgetManager(LocalContext.current)


    NavHost(
        navController = navController,
        startDestination = Routes.MAIN_SCREEN,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) }
    ) {
        // Экран Home
        composable(Routes.MAIN_SCREEN) {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column {
                    TopBar(budget, Modifier.clickable {
                        navController.navigate(Routes.BUDGET_SET_SCREEN)
                    })
                    HomeScreen(budget, Modifier.padding(innerPadding))
                }
            }
        }

        // Экран Details
        composable(Routes.BUDGET_SET_SCREEN) {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column {
                    BudgetSetScreen(
                        onSave = { totalBudget, daysAmount ->
                            BudgetViewModel.getInstance(budget)?.initialize(totalBudget, daysAmount)
                            navController.popBackStack()
                            println("Бюджет: $budget, Дата: $daysAmount")
                        },
                        onCancel = {
                            navController.popBackStack()
                            println("Отмена изменений")
                        },
                        onClearStatistics = {
                            budget.clearAllExpenses()
                            println("Статистика очищена")
                            return@BudgetSetScreen budget.getAllExpenses()
                        },
                        statsByCategory = budget.getAllExpenses(),
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}