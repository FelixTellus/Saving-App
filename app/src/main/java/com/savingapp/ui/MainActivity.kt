package com.savingapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.savingapp.R
import com.savingapp.databinding.ActivityMainBinding
import com.savingapp.ui.adapter.GoalAdapter
import com.savingapp.ui.viewmodel.SavingViewModel
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SavingViewModel
    private lateinit var goalAdapter: GoalAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel = ViewModelProvider(this)[SavingViewModel::class.java]
        
        setupRecyclerView()
        setupFab()
        observeData()
    }
    
    private fun setupRecyclerView() {
        goalAdapter = GoalAdapter(
            onItemClick = { goalWithExpenses ->
                val intent = Intent(this, ExpenseActivity::class.java)
                intent.putExtra("GOAL_ID", goalWithExpenses.goal.id)
                intent.putExtra("GOAL_NAME", goalWithExpenses.goal.name)
                startActivity(intent)
            },
            onEditClick = { goal ->
                val intent = Intent(this, AddEditGoalActivity::class.java)
                intent.putExtra("GOAL_ID", goal.id)
                startActivity(intent)
            },
            onDeleteClick = { goal ->
                viewModel.deleteGoal(goal)
            }
        )
        
        binding.recyclerViewGoals.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = goalAdapter
        }
    }
    
    private fun setupFab() {
        binding.fabAddGoal.setOnClickListener {
            val intent = Intent(this, AddEditGoalActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun observeData() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        
        viewModel.allGoalsWithExpenses.observe(this) { goalsWithExpenses ->
            goalAdapter.submitList(goalsWithExpenses)
            
            if (goalsWithExpenses.isEmpty()) {
                binding.textViewEmpty.visibility = android.view.View.VISIBLE
                binding.recyclerViewGoals.visibility = android.view.View.GONE
            } else {
                binding.textViewEmpty.visibility = android.view.View.GONE
                binding.recyclerViewGoals.visibility = android.view.View.VISIBLE
            }
        }
    }
}