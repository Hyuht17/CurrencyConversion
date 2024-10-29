package com.example.currencyconversionapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconversionapplication.R

class MainActivity : AppCompatActivity() {

    private lateinit var editTextSourceAmount: EditText
    private lateinit var editTextTargetAmount: EditText
    private lateinit var spinnerSourceCurrency: Spinner
    private lateinit var spinnerTargetCurrency: Spinner

    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.85,
        "JPY" to 110.0,
        "VND" to 23000.0,
        "GBP" to 0.73
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextSourceAmount = findViewById(R.id.editTextSourceAmount)
        editTextTargetAmount = findViewById(R.id.editTextTargetAmount)
        spinnerSourceCurrency = findViewById(R.id.spinnerSourceCurrency)
        spinnerTargetCurrency = findViewById(R.id.spinnerTargetCurrency)

        setupSpinners()
        setupListeners()
    }

    private fun setupSpinners() {
        val currencies = exchangeRates.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerSourceCurrency.adapter = adapter
        spinnerTargetCurrency.adapter = adapter
    }

    private fun setupListeners() {
        editTextSourceAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateConversion()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val currencySelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                updateConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerSourceCurrency.onItemSelectedListener = currencySelectedListener
        spinnerTargetCurrency.onItemSelectedListener = currencySelectedListener
    }

    private fun updateConversion() {
        val sourceAmountText = editTextSourceAmount.text.toString()
        if (sourceAmountText.isNotEmpty()) {
            val sourceAmount = sourceAmountText.toDoubleOrNull() ?: return
            val sourceCurrency = spinnerSourceCurrency.selectedItem.toString()
            val targetCurrency = spinnerTargetCurrency.selectedItem.toString()

            val sourceRate = exchangeRates[sourceCurrency] ?: 1.0
            val targetRate = exchangeRates[targetCurrency] ?: 1.0

            val convertedAmount = sourceAmount * (targetRate / sourceRate)
            editTextTargetAmount.setText(String.format("%.2f", convertedAmount))
        } else {
            editTextTargetAmount.setText("")
        }
    }
}
