package com.example.sql

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "EmployeeDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "employees"

        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_POSITION = "position"
        private const val COLUMN_PHONE = "phone"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_POSITION TEXT,
                $COLUMN_PHONE TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertEmployee(employee: Employee): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, employee.name)
            put(COLUMN_POSITION, employee.position)
            put(COLUMN_PHONE, employee.phone)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllEmployees(): List<Employee> {
        val employees = mutableListOf<Employee>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val employee = Employee(
                    id = getLong(getColumnIndexOrThrow(COLUMN_ID)),
                    name = getString(getColumnIndexOrThrow(COLUMN_NAME)),
                    position = getString(getColumnIndexOrThrow(COLUMN_POSITION)),
                    phone = getString(getColumnIndexOrThrow(COLUMN_PHONE))
                )
                employees.add(employee)
            }
        }
        cursor.close()
        return employees
    }

    fun deleteAllEmployees() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }
}