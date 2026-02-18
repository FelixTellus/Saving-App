package com.savingapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.savingapp.databinding.ItemGoalBinding
import com.savingapp.data.model.GoalWithExpenses
import com.savingapp.data.model.MonthlyGoal
import java.text.NumberFormat
import java.util.Locale

class GoalAdapter(
    private val onItemClick: (GoalWithExpenses) -> Unit,
    private val onEditClick: (MonthlyGoal) -> Unit,
    private val onDeleteClick: (MonthlyGoal) -> Unit
) : ListAdapter<GoalWithExpenses, GoalAdapter.GoalViewHolder>(GoalDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val binding = ItemGoalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GoalViewHolder(private val binding: ItemGoalBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        fun bind(goalWithExpenses: GoalWithExpenses) {
            val goal = goalWithExpenses.goal
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            
            binding.apply {
                // Basic info
                textViewGoalName.text = "${goal.getMonthName()} ${goal.year}"
                textViewDescription.text = goal.description
                
                // Amounts
                textViewTargetAmount.text = currencyFormat.format(goal.targetAmount)
                textViewTotalExpenses.text = currencyFormat.format(goalWithExpenses.getTotalExpenses())
                textViewRemaining.text = currencyFormat.format(goalWithExpenses.getRemainingAmount())
                
                // Progress
                val progress = goalWithExpenses.getProgressPercentage().toInt()
                progressBar.progress = progress
                textViewProgress.text = "$progress% del presupuesto"
                
                // Color coding
                when {
                    goalWithExpenses.isOverBudget() -> {
                        textViewRemaining.setTextColor(Color.RED)
                        textViewProgress.setTextColor(Color.RED)
                        progressBar.progressTintList = android.content.res.ColorStateList.valueOf(Color.RED)
                    }
                    progress >= 80 -> {
                        textViewRemaining.setTextColor(Color.rgb(255, 152, 0))
                        textViewProgress.setTextColor(Color.rgb(255, 152, 0))
                        progressBar.progressTintList = android.content.res.ColorStateList.valueOf(Color.rgb(255, 152, 0))
                    }
                    else -> {
                        textViewRemaining.setTextColor(Color.rgb(76, 175, 80))
                        textViewProgress.setTextColor(Color.rgb(76, 175, 80))
                        progressBar.progressTintList = android.content.res.ColorStateList.valueOf(Color.rgb(76, 175, 80))
                    }
                }
                
                // Click listeners
                root.setOnClickListener { onItemClick(goalWithExpenses) }
                buttonEdit.setOnClickListener { onEditClick(goal) }
                buttonDelete.setOnClickListener { onDeleteClick(goal) }
            }
        }
    }

    private class GoalDiffCallback : DiffUtil.ItemCallback<GoalWithExpenses>() {
        override fun areItemsTheSame(oldItem: GoalWithExpenses, newItem: GoalWithExpenses): Boolean {
            return oldItem.goal.id == newItem.goal.id
        }

        override fun areContentsTheSame(oldItem: GoalWithExpenses, newItem: GoalWithExpenses): Boolean {
            return oldItem == newItem
        }
    }
}