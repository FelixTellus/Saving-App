package com.savingapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.savingapp.data.database.SavingDatabase
import com.savingapp.data.model.DailyExpense
import com.savingapp.data.model.GoalWithExpenses
import com.savingapp.data.model.MonthlyGoal
import com.savingapp.data.repository.SavingRepository
import kotlinx.coroutines.launch

class SavingViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: SavingRepository
    
    init {
        val database = SavingDatabase.getDatabase(application)
        repository = SavingRepository(database.monthlyGoalDao(), database.dailyExpenseDao())
    }
    
    // Goals
    val allGoals: LiveData<List<MonthlyGoal>> = repository.getAllGoals()
    val allGoalsWithExpenses: LiveData<List<GoalWithExpenses>> = repository.getAllGoalsWithExpenses()
    
    fun getGoalsByYear(year: Int): LiveData<List<MonthlyGoal>> = repository.getGoalsByYear(year)
    
    fun getGoalWithExpenses(goalId: Long): LiveData<GoalWithExpenses?> = repository.getGoalWithExpenses(goalId)
    
    fun insertGoal(goal: MonthlyGoal) = viewModelScope.launch {
        repository.insertGoal(goal)
    }
    
    fun updateGoal(goal: MonthlyGoal) = viewModelScope.launch {
        repository.updateGoal(goal)
    }
    
    fun deleteGoal(goal: MonthlyGoal) = viewModelScope.launch {
        repository.deleteGoal(goal)
    }
    
    // Expenses
    fun getExpensesByGoal(goalId: Long): LiveData<List<DailyExpense>> = repository.getExpensesByGoal(goalId)
    
    fun insertExpense(expense: DailyExpense) = viewModelScope.launch {
        repository.insertExpense(expense)
    }
    
    fun updateExpense(expense: DailyExpense) = viewModelScope.launch {
        repository.updateExpense(expense)
    }
    
    fun deleteExpense(expense: DailyExpense) = viewModelScope.launch {
        repository.deleteExpense(expense)
    }
}