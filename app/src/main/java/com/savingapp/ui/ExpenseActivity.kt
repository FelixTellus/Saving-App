package com.savingapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.savingapp.databinding.ActivityExpenseBinding
import com.savingapp.ui.adapter.ExpenseAdapter
import com.savingapp.ui.viewmodel.SavingViewModel
import java.text.NumberFormat
import java.util.Locale

class ExpenseActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityExpenseBinding
    private lateinit var viewModel: SavingViewModel
    private lateinit var expenseAdapter: ExpenseAdapter
    private var goalId: Long = -1L
    private var goalName: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        goalId = intent.getLongExtra("GOAL_ID", -1L)
        goalName = intent.getStringExtra("GOAL_NAME") ?: ""
        
        if (goalId == -1L) {
            finish()
            return
        }
        
        viewModel = ViewModelProvider(this)[SavingViewModel::class.java]
        
        setupUI()
        setupRecyclerView()
        setupFab()
        observeData()
    }
    
    private fun setupUI() {
        supportActionBar?.title = "Gastos de $goalName"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    
    private fun setupRecyclerView() {
        expenseAdapter = ExpenseAdapter(
            onEditClick = { expense ->
                val intent = Intent(this, AddEditExpenseActivity::class.java)
                intent.putExtra("EXPENSE_ID", expense.id)
                intent.putExtra("GOAL_ID", goalId)
                startActivity(intent)
            },
            onDeleteClick = { expense ->
                viewModel.deleteExpense(expense)
            }
        )
        
        binding.recyclerViewExpenses.apply {
            layoutManager = LinearLayoutManager(this@ExpenseActivity)
            adapter = expenseAdapter
        }
    }
    
    private fun setupFab() {
        binding.fabAddExpense.setOnClickListener {
            val intent = Intent(this, AddEditExpenseActivity::class.java)
            intent.putExtra("GOAL_ID", goalId)
            startActivity(intent)
        }
    }
    
    private fun observeData() {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        
        viewModel.getGoalWithExpenses(goalId).observe(this) { goalWithExpenses ->
            goalWithExpenses?.let {
                binding.textViewGoalAmount.text = currencyFormat.format(it.goal.targetAmount)
                binding.textViewTotalExpenses.text = currencyFormat.format(it.getTotalExpenses())
                binding.textViewRemaining.text = currencyFormat.format(it.getRemainingAmount())
                
                val progress = it.getProgressPercentage().toInt()
                binding.progressBarGoal.progress = progress
                binding.textViewProgressPercentage.text = "$progress%"
                
                // Update expenses list
                expenseAdapter.submitList(it.expenses)
                
                if (it.expenses.isEmpty()) {
                    binding.textViewEmptyExpenses.visibility = android.view.View.VISIBLE
                    binding.recyclerViewExpenses.visibility = android.view.View.GONE
                } else {
                    binding.textViewEmptyExpenses.visibility = android.view.View.GONE
                    binding.recyclerViewExpenses.visibility = android.view.View.VISIBLE
                }
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}