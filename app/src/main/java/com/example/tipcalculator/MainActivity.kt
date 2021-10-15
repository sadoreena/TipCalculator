package com.example.tipcalculator

import android.animation.ArgbEvaluator
import android.graphics.Color
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

private const val TAG = "MainActivity"
private const val initial_tip_percent = 15
class MainActivity : AppCompatActivity() {

    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var DefaultButton: Button
    private lateinit var ValentinesDayButton: Button
    private lateinit var HalloweenButton: Button
    private lateinit var ValentinesBackground: ImageView
    private lateinit var HalloweenBackground: ImageView
    private lateinit var tvTipDescription: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)

        DefaultButton = findViewById(R.id.DefaultButton)
        ValentinesDayButton = findViewById(R.id.ValentinesDayButton)
        HalloweenButton = findViewById(R.id.HalloweenButton)

        ValentinesBackground = findViewById(R.id.ValentinesBackground)
        HalloweenBackground = findViewById(R.id.HalloweenBackground)


        seekBarTip.progress = initial_tip_percent
        tvTipPercentLabel.text = "$initial_tip_percent%"
        updateTipDescription(initial_tip_percent)

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercentLabel.text = "$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }
        })

        // changing background after a button is pressed
        HalloweenButton.setOnClickListener(View.OnClickListener {
            HalloweenBackground.isVisible = true
            ValentinesBackground.isVisible = false
        })

        ValentinesDayButton.setOnClickListener(View.OnClickListener {
            HalloweenBackground.isVisible = false
            ValentinesBackground.isVisible = true
        })

        DefaultButton.setOnClickListener(View.OnClickListener {
            HalloweenBackground.isVisible = false
            ValentinesBackground.isVisible = false
        })
    }

    private fun updateTipDescription(tipPercent: Int) {

        val tipDescription = when(tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Accpetable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription

        // update color based on tip percent
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.worst_tip),
            ContextCompat.getColor(this, R.color.best_tip)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        // get value of base and tip percentage

        if (etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }

        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress

        // computer tip and total
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        // update the UI
        tvTipAmount.text =  "\$%.2f".format(tipAmount)
        tvTotalAmount.text = "\$%.2f".format(totalAmount)
   }
}

