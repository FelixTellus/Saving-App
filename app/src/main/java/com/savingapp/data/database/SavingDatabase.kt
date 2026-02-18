package com.savingapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.savingapp.data.dao.DailyExpenseDao
import com.savingapp.data.dao.MonthlyGoalDao
import com.savingapp.data.model.DailyExpense
import com.savingapp.data.model.MonthlyGoal

@Database(
    entities = [MonthlyGoal::class, DailyExpense::class],
    version = 1,
    exportSchema = false
)
abstract class SavingDatabase : RoomDatabase() {
    
    abstract fun monthlyGoalDao(): MonthlyGoalDao
    abstract fun dailyExpenseDao(): DailyExpenseDao
    
    companion object {
        @Volatile
        private var INSTANCE: SavingDatabase? = null
        
        fun getDatabase(context: Context): SavingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SavingDatabase::class.java,
                    "saving_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}