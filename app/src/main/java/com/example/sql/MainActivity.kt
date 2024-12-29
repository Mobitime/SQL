package com.example.sql

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppManager.addActivity(this)

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            startActivity(Intent(this, DatabaseActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.removeActivity(this)
    }
}