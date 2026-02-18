package com.savingapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.savingapp.databinding.ItemExpenseBinding
import com.savingapp.data.model.DailyExpense
import java.text.NumberFormat
import java.util.Locale

class ExpenseAdapter(
    private val onEditClick: (DailyExpense) -> Unit,
    private val onDeleteClick: (DailyExpense) -> Unit
) : ListAdapter<DailyExpense, ExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ExpenseViewHolder(private val binding: ItemExpenseBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: DailyExpense) {
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            
            binding.apply {
                textViewExpenseName.text = expense.name
                textViewExpenseDescription.text = expense.description
                textViewExpenseAmount.text = currencyFormat.format(expense.amount)
                textViewExpenseDate.text = "Día ${expense.day}"
                
                buttonEdit.setOnClickListener { onEditClick(expense) }
                buttonDelete.setOnClickListener { onDeleteClick(expense) }
            }
        }
    }

    private class ExpenseDiffCallback : DiffUtil.ItemCallback<DailyExpense>() {
        override fun areItemsTheSame(oldItem: DailyExpense, newItem: DailyExpense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DailyExpense, newItem: DailyExpense): Boolean {
            return oldItem == newItem
        }
    }
}