package com.savingapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.savingapp.data.model.GoalWithExpenses
import com.savingapp.data.model.MonthlyGoal

@Dao
interface MonthlyGoalDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: MonthlyGoal): Long
    
    @Update
    suspend fun update(goal: MonthlyGoal)
    
    @Delete
    suspend fun delete(goal: MonthlyGoal)
    
    @Query("SELECT * FROM monthly_goals WHERE id = :id")
    suspend fun getGoalById(id: Long): MonthlyGoal?
    
    @Query("SELECT * FROM monthly_goals ORDER BY year DESC, month DESC")
    fun getAllGoals(): LiveData<List<MonthlyGoal>>
    
    @Query("SELECT * FROM monthly_goals WHERE year = :year ORDER BY month")
    fun getGoalsByYear(year: Int): LiveData<List<MonthlyGoal>>
    
    @Query("SELECT * FROM monthly_goals WHERE month = :month AND year = :year")
    suspend fun getGoalByMonthAndYear(month: Int, year: Int): MonthlyGoal?
    
    @Transaction
    @Query("SELECT * FROM monthly_goals WHERE id = :goalId")
    fun getGoalWithExpenses(goalId: Long): LiveData<GoalWithExpenses?>
    
    @Transaction
    @Query("SELECT * FROM monthly_goals ORDER BY year DESC, month DESC")
    fun getAllGoalsWithExpenses(): LiveData<List<GoalWithExpenses>>
    
    @Transaction
    @Query("SELECT * FROM monthly_goals WHERE year = :year ORDER BY month")
    fun getGoalsWithExpensesByYear(year: Int): LiveData<List<GoalWithExpenses>>
}