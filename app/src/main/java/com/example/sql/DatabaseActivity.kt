package com.example.sql

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class DatabaseActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etName: EditText
    private lateinit var spinnerPosition: Spinner
    private lateinit var etPhone: EditText
    private lateinit var tvData: TextView

    private val positions = arrayOf("Менеджер", "Разработчик", "Дизайнер", "Тестировщик")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)
        AppManager.addActivity(this)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "База данных"


        etName = findViewById(R.id.etName)
        spinnerPosition = findViewById(R.id.spinnerPosition)
        etPhone = findViewById(R.id.etPhone)
        tvData = findViewById(R.id.tvData)


        ArrayAdapter(this, android.R.layout.simple_spinner_item, positions).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPosition.adapter = adapter
        }

        dbHelper = DatabaseHelper(this)


        findViewById<Button>(R.id.btnSave).setOnClickListener { saveData() }
        findViewById<Button>(R.id.btnLoad).setOnClickListener { loadData() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { deleteData() }
    }

    private fun saveData() {
        val employee = Employee(
            name = etName.text.toString(),
            position = spinnerPosition.selectedItem.toString(),
            phone = etPhone.text.toString()
        )
        dbHelper.insertEmployee(employee)
        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show()
        clearInputs()
    }

    private fun loadData() {
        val employees = dbHelper.getAllEmployees()
        if (employees.isEmpty()) {
            tvData.text = "Нет данных"
            return
        }

        val sb = StringBuilder()
        employees.forEach { employee ->
            sb.append("Имя: ${employee.name}\n")
            sb.append("Должность: ${employee.position}\n")
            sb.append("Телефон: ${employee.phone}\n")
            sb.append("----------------\n")
        }
        tvData.text = sb.toString()
    }

    private fun deleteData() {
        dbHelper.deleteAllEmployees()
        tvData.text = ""
        Toast.makeText(this, "Все данные удалены", Toast.LENGTH_SHORT).show()
    }

    private fun clearInputs() {
        etName.text.clear()
        spinnerPosition.setSelection(0)
        etPhone.text.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.removeActivity(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_exit -> {
                AlertDialog.Builder(this)
                    .setTitle("Выход")
                    .setMessage("Вы действительно хотите закрыть приложение?")
                    .setPositiveButton("Да") { _, _ ->
                        AppManager.finishAll()
                    }
                    .setNegativeButton("Нет", null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}