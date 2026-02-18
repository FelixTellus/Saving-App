package com.savingapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.savingapp.data.model.DailyExpense

@Dao
interface DailyExpenseDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: DailyExpense): Long
    
    @Update
    suspend fun update(expense: DailyExpense)
    
    @Delete
    suspend fun delete(expense: DailyExpense)
    
    @Query("SELECT * FROM daily_expenses WHERE id = :id")
    suspend fun getExpenseById(id: Long): DailyExpense?
    
    @Query("SELECT * FROM daily_expenses WHERE monthlyGoalId = :goalId ORDER BY day")
    fun getExpensesByGoal(goalId: Long): LiveData<List<DailyExpense>>
    
    @Query("SELECT * FROM daily_expenses WHERE month = :month AND year = :year ORDER BY day")
    fun getExpensesByMonthAndYear(month: Int, year: Int): LiveData<List<DailyExpense>>
    
    @Query("SELECT SUM(amount) FROM daily_expenses WHERE monthlyGoalId = :goalId")
    suspend fun getTotalExpensesByGoal(goalId: Long): Double?
    
    @Query("DELETE FROM daily_expenses WHERE monthlyGoalId = :goalId")
    suspend fun deleteExpensesByGoal(goalId: Long)
}