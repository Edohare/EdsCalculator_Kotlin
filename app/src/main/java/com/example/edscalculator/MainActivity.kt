package com.example.edscalculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.NumberFormatException
import kotlinx.android.synthetic.main.activity_main.*

private const val STATE_PENDING_OPERATION="PendingOperation"
private const val STATE_OPERAND1 ="Operand1"
private const val STATE_OPERAND1_STORED = "Operand1_Stored"
class MainActivity : AppCompatActivity() {
    private var operand1: Double? = null
    private var pendingOperation = "="
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = View.OnClickListener {
            val b = it as Button
            newNumber.append(b.text)
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener {
            val op = (it as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException){
                newNumber.setText("")
            }
            pendingOperation = op
            operation.text = pendingOperation
        }

        buttonEquals.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)

        buttonNeg.setOnClickListener {
            val value = newNumber.text.toString()
            if (value.isEmpty()){
                newNumber.setText("-")
            } else{
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())
                } catch (e: NumberFormatException){
                    newNumber.setText("")
                }
            }
        }

        buttonClear.setOnClickListener{
            operand1 = null
            pendingOperation = "="
            operation.text = pendingOperation
            newNumber.text.clear()
            result.text.clear()
            }

    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }
            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if(value == 0.0){
                    Double.NaN // handle attempt to divide by zero, displays NaN instead of crashing
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-"-> operand1 = operand1!! - value
                "+"-> operand1 = operand1!! + value
            }

        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1 !=null){
            // key value pair, taking the operand 1, and assigning it to state_operand1, etc
            // using the !! to override the nullable type exception
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if( savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)){
            savedInstanceState.getDouble(STATE_OPERAND1)
        } else{
            null
        }
        // using the key value pair created in onsave, to extract that info when it restores
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        operation.text = pendingOperation
    }

}
