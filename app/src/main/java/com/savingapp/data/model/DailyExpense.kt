package com.savingapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "daily_expenses",
    foreignKeys = [
        ForeignKey(
            entity = MonthlyGoal::class,
            parentColumns = ["id"],
            childColumns = ["monthlyGoalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("monthlyGoalId")]
)
data class DailyExpense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val monthlyGoalId: Long,
    val name: String,
    val description: String,
    val amount: Double,
    val iconName: String,
    val day: Int, // 1-31
    val month: Int, // 1-12
    val year: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)