package com.savingapp.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.savingapp.R
import com.savingapp.databinding.ActivityAddEditGoalBinding
import com.savingapp.data.model.MonthlyGoal
import com.savingapp.ui.viewmodel.SavingViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class AddEditGoalActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAddEditGoalBinding
    private lateinit var viewModel: SavingViewModel
    private var goalId: Long = -1L
    private var isEditMode = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel = ViewModelProvider(this)[SavingViewModel::class.java]
        
        goalId = intent.getLongExtra("GOAL_ID", -1L)
        isEditMode = goalId != -1L
        
        setupUI()
        if (isEditMode) {
            loadGoal()
        }
        
        setupListeners()
    }
    
    private fun setupUI() {
        supportActionBar?.title = if (isEditMode) "Editar Meta" else "Nueva Meta"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Month spinner
        val months = arrayOf(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMonth.adapter = monthAdapter
        
        // Set current month as default
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        binding.spinnerMonth.setSelection(currentMonth)
        
        // Year
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        binding.editTextYear.setText(currentYear.toString())
        
        // Icon spinner
        val icons = arrayOf(
            "Ahorro", "Vivienda", "Transporte", "Comida", "Entretenimiento",
            "Salud", "Educación", "Ropa", "Otro"
        )
        val iconAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, icons)
        iconAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerIcon.adapter = iconAdapter
    }
    
    private fun loadGoal() {
        lifecycleScope.launch {
            val goal = viewModel.getGoalWithExpenses(goalId)
            goal.observe(this@AddEditGoalActivity) { goalWithExpenses ->
                goalWithExpenses?.goal?.let { goal ->
                    binding.editTextName.setText(goal.name)
                    binding.editTextDescription.setText(goal.description)
                    binding.editTextAmount.setText(goal.targetAmount.toString())
                    binding.spinnerMonth.setSelection(goal.month - 1)
                    binding.editTextYear.setText(goal.year.toString())
                }
            }
        }
    }
    
    private fun setupListeners() {
        binding.buttonSave.setOnClickListener {
            saveGoal()
        }
        
        binding.buttonCancel.setOnClickListener {
            finish()
        }
    }
    
    private fun saveGoal() {
        val name = binding.editTextName.text.toString()
        val description = binding.editTextDescription.text.toString()
        val amountStr = binding.editTextAmount.text.toString()
        val month = binding.spinnerMonth.selectedItemPosition + 1
        val yearStr = binding.editTextYear.text.toString()
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
        
        val year = yearStr.toIntOrNull()
        if (year == null || year < 2020 || year > 2100) {
            binding.editTextYear.error = "Año inválido"
            return
        }
        
        val goal = MonthlyGoal(
            id = if (isEditMode) goalId else 0,
            name = name,
            description = description,
            targetAmount = amount,
            iconName = iconName,
            month = month,
            year = year,
            updatedAt = System.currentTimeMillis()
        )
        
        if (isEditMode) {
            viewModel.updateGoal(goal)
            Toast.makeText(this, "Meta actualizada", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.insertGoal(goal)
            Toast.makeText(this, "Meta creada", Toast.LENGTH_SHORT).show()
        }
        
        finish()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}