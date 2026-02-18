package com.savingapp.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.savingapp.databinding.ActivityAddEditExpenseBinding
import com.savingapp.data.model.DailyExpense
import com.savingapp.ui.viewmodel.SavingViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class AddEditExpenseActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAddEditExpenseBinding
    private lateinit var viewModel: SavingViewModel
    private var expenseId: Long = -1L
    private var goalId: Long = -1L
    private var isEditMode = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel = ViewModelProvider(this)[SavingViewModel::class.java]
        
        expenseId = intent.getLongExtra("EXPENSE_ID", -1L)
        goalId = intent.getLongExtra("GOAL_ID", -1L)
        isEditMode = expenseId != -1L
        
        if (goalId == -1L && !isEditMode) {
            finish()
            return
        }
        
        setupUI()
        if (isEditMode) {
            loadExpense()
        }
        
        setupListeners()
    }
    
    private fun setupUI() {
        supportActionBar?.title = if (isEditMode) "Editar Gasto" else "Nuevo Gasto"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Icon spinner
        val icons = arrayOf(
            "Comida", "Transporte", "Entretenimiento", "Salud", "Servicios",
            "Ropa", "Educación", "Hogar", "Otro"
        )
        val iconAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, icons)
        iconAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerIcon.adapter = iconAdapter
        
        // Set current day as default
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        binding.editTextDay.setText(currentDay.toString())
    }
    
    private fun loadExpense() {
        lifecycleScope.launch {
            viewModel.repository.getExpenseById(expenseId)?.let { expense ->
                binding.editTextName.setText(expense.name)
                binding.editTextDescription.setText(expense.description)
                binding.editTextAmount.setText(expense.amount.toString())
                binding.editTextDay.setText(expense.day.toString())
                goalId = expense.monthlyGoalId
            }
        }
    }
    
    private fun setupListeners() {
        binding.buttonSave.setOnClickListener {
            saveExpense()
        }
        
        binding.buttonCancel.setOnClickListener {
            finish()
        }
    }
    
    private fun saveExpense() {
        val name = binding.editTextName.text.toString()
        val description = binding.editTextDescription.text.toString()
        val amountStr = binding.editTextAmount.text.toString()
        val dayStr = binding.editTextDay.text.toString()
        val iconName = binding.spinnerIcon.selectedItem.toString()
        
        // Validation
        if (name.isBlank()) {
            binding.editTextName.error = "Ingrese un nombre"
            return
        }
        
        if (amountStr.isBlank()) {
            binding.editTextAmount.error = "Ingrese un monto"
            return
        }
        
        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            binding.editTextAmount.error = "Monto inválido"
            return
        }
        
        val day = dayStr.toIntOrNull()
        if (day == null || day < 1 || day > 31) {
            binding.editTextDay.error = "Día inválido"
            return
        }
        
        val calendar = Calendar.getInstance()
        
        lifecycleScope.launch {
            // Get goal to extract month and year
            val goal = viewModel.repository.getGoalById(goalId)
            if (goal == null) {
                Toast.makeText(this@AddEditExpenseActivity, "Error: meta no encontrada", Toast.LENGTH_SHORT).show()
                return@launch
            }
            
            val expense = DailyExpense(
                id = if (isEditMode) expenseId else 0,
                monthlyGoalId = goalId,
                name = name,
                description = description,
                amount = amount,
                iconName = iconName,
                day = day,
                month = goal.month,
                year = goal.year,
                updatedAt = System.currentTimeMillis()
            )
            
            if (isEditMode) {
                viewModel.updateExpense(expense)
                Toast.makeText(this@AddEditExpenseActivity, "Gasto actualizado", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.insertExpense(expense)
                Toast.makeText(this@AddEditExpenseActivity, "Gasto registrado", Toast.LENGTH_SHORT).show()
            }
            
            finish()
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}