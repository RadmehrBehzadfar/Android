package com.example.payslip_radmehr

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1) to find the EditText fields
        val editTextRate = findViewById<EditText>(R.id.editTextText)    // Rate
        val editTextHours = findViewById<EditText>(R.id.editTextText2) // Hours

        // 2) to find the Button
        val calculateButton = findViewById<Button>(R.id.button) // Calculate

        // 3) to find the TextViews
        val textViewWeeklyEarnings = findViewById<TextView>(R.id.textView4) // Weekly Earnings
        val textViewTax = findViewById<TextView>(R.id.textView5) // Tax
        val textViewEI = findViewById<TextView>(R.id.textView6) // EI
        val textViewCPP = findViewById<TextView>(R.id.textView7) // CPP
        val textViewVacationPay = findViewById<TextView>(R.id.textView8) // Vacation Pay
        val textViewTotalPay = findViewById<TextView>(R.id.textView9) // Total Pay


        calculateButton.setOnClickListener {
            // here we get the string itself from the EditText fields
            val rateStr = editTextRate.text.toString()
            val hoursStr = editTextHours.text.toString()

            // here we validate the inputs
            if (rateStr.isEmpty() || hoursStr.isEmpty()) {
                Toast.makeText(this, "Please enter values for rate and hours.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // here we convert the strings to doubles
            val rate = rateStr.toDoubleOrNull()
            val hours = hoursStr.toDoubleOrNull()

            if (rate == null || hours == null) {
                Toast.makeText(this, "Please enter valid numeric values.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // here we validate the inputs again by their ranges (rate and hours)
            if (rate < 55 || rate > 105) {
                Toast.makeText(this, "Rate must be between $55 and $105.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (hours < 0 || hours > 60) {
                Toast.makeText(this, "Hours must be between 0 and 60.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // here we calculate the weekly earnings
            val weeklyEarnings = if (hours <= 40) {
                hours * rate
            } else {
                val regularPay = 40 * rate  // First 40 hours at normal rate
                val overtimeHours = hours - 40  // Extra hours
                val overtimePay = overtimeHours * (rate * 2)  // Overtime pay (double rate)
                regularPay + overtimePay
            }

            // here we calculate the tax, ei, cpp, vacation pay, and total pay
            val tax = when {
                weeklyEarnings <= 2400 -> weeklyEarnings * 0.10
                weeklyEarnings <= 4800 -> weeklyEarnings * 0.18
                weeklyEarnings <= 6000 -> weeklyEarnings * 0.22
                else -> weeklyEarnings * 0.30
            }

            val ei = weeklyEarnings * 0.06  // 6% EI deduction
            val cpp = weeklyEarnings * 0.075  // 7.5% CPP deduction
            val vacationPay = weeklyEarnings * 0.05  // 5% vacation pay

            // here we calculate the total pay
            val totalPay = weeklyEarnings + vacationPay - tax - ei - cpp

            // here we update the TextViews with the calculated values
            textViewWeeklyEarnings.text = "Weekly Earnings: \$%.2f".format(weeklyEarnings)
            textViewTax.text = "Tax: \$%.2f".format(tax)
            textViewEI.text = "EI: \$%.2f".format(ei)
            textViewCPP.text = "CPP: \$%.2f".format(cpp)
            textViewVacationPay.text = "Vacation Pay: \$%.2f".format(vacationPay)
            textViewTotalPay.text = "Total Pay: \$%.2f".format(totalPay)
        }
    }
}
