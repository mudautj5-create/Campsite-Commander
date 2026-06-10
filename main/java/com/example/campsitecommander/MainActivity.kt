package com.example.campsitecommander

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Parallel arrays (pre-loaded with sample data)
    private val itemNames = mutableListOf("Tent", "Marshmallow", "Flashlight")
    private val itemCategories = mutableListOf("Shelter", "Food", "Safety")
    private val itemQuantities = mutableListOf(1, 2, 3)
    private val itemComments = mutableListOf(
        "4-person waterproof",
        "For S'mores (Mega size)",
        "Check batteries(AA)"
    )

    // UI references
    private lateinit var tvTotalItems: TextView
    private lateinit var tvQuickPreview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Link UI elements
        tvTotalItems = findViewById(R.id.tvTotalItems)
        tvQuickPreview = findViewById(R.id.tvQuickPreview)
        val btnAddGear = findViewById<Button>(R.id.btnAddGear)
        val btnViewList = findViewById<Button>(R.id.btnViewList)

        // Initial display
        updateDisplay()

        // Add Gear button - show input dialog
        btnAddGear.setOnClickListener { showAddGearDialog() }

        // View List button - go to DetailActivity
        btnViewList.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            // Pass arrays as ArrayList via Intent
            intent.putStringArrayListExtra("names", ArrayList(itemNames))
            intent.putStringArrayListExtra("categories", ArrayList(itemCategories))
            intent.putIntegerArrayListExtra("quantities", ArrayList(itemQuantities))
            intent.putStringArrayListExtra("comments", ArrayList(itemComments))
            startActivity(intent)
        }
    }

    // Calculate total number of items using a loop
    private fun calculateTotal(): Int {
        var total = 0
        for (quantity in itemQuantities) {
            total += quantity
        }
        return total
    }

    // Update the counter and quick preview
    private fun updateDisplay() {
        tvTotalItems.text = "Total Items Packed: ${calculateTotal()}"

        // Build quick preview using a loop
        val sb = StringBuilder()
        for (i in itemNames.indices) {
            sb.append("${i + 1}. ${itemNames[i]} (${itemCategories[i]})\n")
        }
        tvQuickPreview.text = if (sb.isEmpty()) "No items added yet." else sb.toString()
    }

    // AlertDialog for adding a new item gear item
    private fun showAddGearDialog() {
        val dialogLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
        }

        val etName = EditText(this).apply { hint = "Item Name (e.g. Sleeping Bag)" }
        val etCategory = EditText(this).apply { hint = "Item Category (e.g. Shelter, Food, Safety)" }
        val etQuantity = EditText(this).apply {
            hint = "Quantity (e.g. 2)"
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        val etComments = EditText(this).apply { hint = "Comments/Notes" }

        dialogLayout.addView(etName)
        dialogLayout.addView(etCategory)
        dialogLayout.addView(etQuantity)
        dialogLayout.addView(etComments)

        AlertDialog.Builder(this)
            .setTitle("Add Gear")
            .setView(dialogLayout)
            .setPositiveButton("Add") { _, _ ->
                val name = etName.text.toString()
                val category = etCategory.text.toString()
                val qtyStr = etQuantity.text.toString()
                val quantity = qtyStr.toIntOrNull() ?: 0
                val comment = etComments.text.toString()

                // Input validation
                when {
                    name.isEmpty() -> Toast.makeText(this, "Item name cannot be empty!", Toast.LENGTH_SHORT).show()
                    category.isEmpty() -> Toast.makeText(this, "Item category cannot be empty!", Toast.LENGTH_SHORT).show()
                    qtyStr.isEmpty() -> Toast.makeText(this, "Quantity cannot be empty!", Toast.LENGTH_SHORT).show()
                    else -> {
                        // Add to parallel arrays
                        itemNames.add(name)
                        itemCategories.add(category)
                        itemQuantities.add(quantity)
                        itemComments.add(comment)
                        updateDisplay()
                        Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
