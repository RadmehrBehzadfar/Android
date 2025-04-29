package com.example.pizza_radmehr

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerPizzaType: Spinner
    private lateinit var spinnerPizzaSize: Spinner
    private lateinit var spinnerQuantity: Spinner
    private lateinit var editTextPhone: EditText
    private lateinit var spinnerDeliveryMode: Spinner
    private lateinit var checkBoxNotify: CheckBox
    private lateinit var buttonPlaceOrder: Button
    private lateinit var textViewOrderSummary: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bind views
        spinnerPizzaType = findViewById(R.id.spinnerPizzaType)
        spinnerPizzaSize = findViewById(R.id.spinnerPizzaSize)
        spinnerQuantity = findViewById(R.id.spinnerQuantity)
        editTextPhone = findViewById(R.id.editTextPhone)
        spinnerDeliveryMode = findViewById(R.id.spinnerDeliveryMode)
        checkBoxNotify = findViewById(R.id.checkBoxNotify)
        buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder)
        textViewOrderSummary = findViewById(R.id.textViewOrderSummary)

        // Set up adapters for spinners
        val pizzaTypeAdapter = ArrayAdapter.createFromResource(
            this, R.array.pizza_types, android.R.layout.simple_spinner_item
        )
        pizzaTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPizzaType.adapter = pizzaTypeAdapter

        val pizzaSizeAdapter = ArrayAdapter.createFromResource(
            this, R.array.pizza_sizes, android.R.layout.simple_spinner_item
        )
        pizzaSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPizzaSize.adapter = pizzaSizeAdapter

        val quantityAdapter = ArrayAdapter.createFromResource(
            this, R.array.quantities, android.R.layout.simple_spinner_item
        )
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerQuantity.adapter = quantityAdapter

        val deliveryModeAdapter = ArrayAdapter.createFromResource(
            this, R.array.delivery_modes, android.R.layout.simple_spinner_item
        )
        deliveryModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDeliveryMode.adapter = deliveryModeAdapter

        // OnClick: Calculate and display order
        buttonPlaceOrder.setOnClickListener {
            placeOrder()
        }
    }

    private fun placeOrder() {
        // 1) Validates that the phone number is not empty
        val phoneNumber = editTextPhone.text.toString().trim()
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_invalid_phone), Toast.LENGTH_SHORT).show()
            return
        }

        // 2) Get selected pizza type
        val pizzaType = spinnerPizzaType.selectedItem.toString()

        // 3) Get selected pizza size and parse out the cost
        val pizzaSizeString = spinnerPizzaSize.selectedItem.toString()
        // For example: "Medium - $16.99" -> parse the cost
        val cost = when {
            pizzaSizeString.contains("Small")       -> 12.99
            pizzaSizeString.contains("Medium")      -> 16.99
            pizzaSizeString.contains("Large")       -> 20.99
            pizzaSizeString.contains("Party Size")  -> 26.99
            else -> 0.0
        }

        // 4) Get quantity
        val quantityString = spinnerQuantity.selectedItem.toString()
        val quantity = quantityString.toIntOrNull() ?: 1

        // 5) Check delivery mode
        val deliveryMode = spinnerDeliveryMode.selectedItem.toString()
        val deliveryFee = if (deliveryMode.contains("Delivery")) 3.99 else 0.0

        // 6) Check if user wants notification
        val wantsNotification = checkBoxNotify.isChecked

        // -----------------------
        // Perform the calculations
        // -----------------------
        // Subtotal
        val subTotal = cost * quantity

        // Discount: 10% if subtotal > 80
        val discount = if (subTotal > 80) subTotal * 0.10 else 0.0

        // Tax: 13% on (Subtotal - Discount)
        val taxableAmount = subTotal - discount
        val tax = taxableAmount * 0.13

        // Total Cost
        val totalCost = taxableAmount + tax + deliveryFee

        // -----------------------
        // Build the Order Summary
        // -----------------------
        val sb = StringBuilder()
        sb.append("Order Details:\n")
        sb.append("• Pizza Type: $pizzaType\n")
        sb.append("• Pizza Size: $pizzaSizeString\n")
        sb.append("• Quantity: $quantity\n")
        sb.append("• Phone: $phoneNumber\n")
        sb.append("• Delivery Mode: $deliveryMode\n")
        if (wantsNotification) {
            sb.append("• You will be notified when your order is ready.\n")
        } else {
            sb.append("• No notification requested.\n")
        }
        sb.append("--------------------------------\n")
        sb.append(String.format("Subtotal: $%.2f\n", subTotal))
        if (discount > 0) {
            sb.append(String.format("Discount (10%%): - $%.2f\n", discount))
        }
        sb.append(String.format("Tax (13%%): $%.2f\n", tax))
        if (deliveryFee > 0) {
            sb.append(String.format("Delivery Fee: $%.2f\n", deliveryFee))
        }
        sb.append("--------------------------------\n")
        sb.append(String.format("TOTAL: $%.2f", totalCost))

        // Display the summary
        textViewOrderSummary.text = sb.toString()
    }
}