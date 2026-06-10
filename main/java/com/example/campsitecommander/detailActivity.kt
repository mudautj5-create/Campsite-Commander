package com.example.campsitecommander

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve parallel arrays from Intent
        val names = intent.getStringArrayListExtra("names") ?: arrayListOf()
        val categories = intent.getStringArrayListExtra("categories") ?: arrayListOf()
        val quantities = intent.getIntegerArrayListExtra("quantities") ?: arrayListOf()
        val comments = intent.getStringArrayListExtra("comments") ?: arrayListOf()

        val tvDetailList = findViewById<TextView>(R.id.tvDetailList)
        val btnBack = findViewById<Button>(R.id.btnBackToBase)

        // Build detail list using a loop
        val sb = StringBuilder()
        val size = names.size
        for (i in 0 until size) {
            sb.append("Item ${i + 1}:\n")
            sb.append("Name: ${names[i]}\n")
            sb.append("Category: ${categories[i]}\n")
            sb.append("Quantity: ${if (i < quantities.size) quantities[i] else "N/A"}\n")
            sb.append("Comments: ${if (i < comments.size) comments[i] else "None"}\n\n")
        }

        tvDetailList.text = if (sb.isEmpty()) "No gear added yet." else sb.toString()

        // Back to Base - return to MainActivity
        btnBack.setOnClickListener {
            finish()
        }
    }
}
