package com.savingapp.data.repository

import androidx.lifecycle.LiveData
import com.savingapp.data.dao.DailyExpenseDao
import com.savingapp.data.dao.MonthlyGoalDao
import com.savingapp.data.model.DailyExpense
import com.savingapp.data.model.GoalWithExpenses
import com.savingapp.data.model.MonthlyGoal

class SavingRepository(
    private val goalDao: MonthlyGoalDao,
    private val expenseDao: DailyExpenseDao
) {
    
    // Monthly Goals
    fun getAllGoals(): LiveData<List<MonthlyGoal>> = goalDao.getAllGoals()
    
    fun getGoalsByYear(year: Int): LiveData<List<MonthlyGoal>> = goalDao.getGoalsByYear(year)
    
    fun getAllGoalsWithExpenses(): LiveData<List<GoalWithExpenses>> = goalDao.getAllGoalsWithExpenses()
    
    fun getGoalWithExpenses(goalId: Long): LiveData<GoalWithExpenses?> = goalDao.getGoalWithExpenses(goalId)
    
    suspend fun insertGoal(goal: MonthlyGoal): Long = goalDao.insert(goal)
    
    suspend fun updateGoal(goal: MonthlyGoal) = goalDao.update(goal)
    
    suspend fun deleteGoal(goal: MonthlyGoal) = goalDao.delete(goal)
    
    suspend fun getGoalById(id: Long): MonthlyGoal? = goalDao.getGoalById(id)
    
    suspend fun getGoalByMonthAndYear(month: Int, year: Int): MonthlyGoal? = 
        goalDao.getGoalByMonthAndYear(month, year)
    
    // Daily Expenses
    fun getExpensesByGoal(goalId: Long): LiveData<List<DailyExpense>> = expenseDao.getExpensesByGoal(goalId)
    
    fun getExpensesByMonthAndYear(month: Int, year: Int): LiveData<List<DailyExpense>> = 
        expenseDao.getExpensesByMonthAndYear(month, year)
    
    suspend fun insertExpense(expense: DailyExpense): Long = expenseDao.insert(expense)
    
    suspend fun updateExpense(expense: DailyExpense) = expenseDao.update(expense)
    
    suspend fun deleteExpense(expense: DailyExpense) = expenseDao.delete(expense)
    
    suspend fun getExpenseById(id: Long): DailyExpense? = expenseDao.getExpenseById(id)
    
    suspend fun getTotalExpensesByGoal(goalId: Long): Double = expenseDao.getTotalExpensesByGoal(goalId) ?: 0.0
}