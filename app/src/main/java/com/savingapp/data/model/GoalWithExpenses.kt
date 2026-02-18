package com.savingapp.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class GoalWithExpenses(
    @Embedded val goal: MonthlyGoal,
    @Relation(
        parentColumn = "id",
        entityColumn = "monthlyGoalId"
    )
    val expenses: List<DailyExpense>
) {
    fun getTotalExpenses(): Double = expenses.sumOf { it.amount }
    
    fun getRemainingAmount(): Double = goal.targetAmount - getTotalExpenses()
    
    fun getProgressPercentage(): Double {
        if (goal.targetAmount == 0.0) return 0.0
        return (getTotalExpenses() / goal.targetAmount) * 100
    }
    
    fun isOverBudget(): Boolean = getTotalExpenses() > goal.targetAmount
    
    fun isOnTrack(): Boolean {
        val progress = getProgressPercentage()
        return progress <= 100 && progress >= 0
    }
}