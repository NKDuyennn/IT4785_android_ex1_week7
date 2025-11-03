package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var displayTextView: TextView
    private lateinit var historyTextView: TextView

    private var currentNumber = "0"
    private var firstOperand = 0.0
    private var operation: String? = null
    private var newNumber = true
    private var lastOperand = 0.0
    private var lastOperation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        displayTextView = findViewById(R.id.display)
        historyTextView = findViewById(R.id.history)

        // Các nút số
        findViewById<Button>(R.id.btn0).setOnClickListener { onNumberClick("0") }
        findViewById<Button>(R.id.btn1).setOnClickListener { onNumberClick("1") }
        findViewById<Button>(R.id.btn2).setOnClickListener { onNumberClick("2") }
        findViewById<Button>(R.id.btn3).setOnClickListener { onNumberClick("3") }
        findViewById<Button>(R.id.btn4).setOnClickListener { onNumberClick("4") }
        findViewById<Button>(R.id.btn5).setOnClickListener { onNumberClick("5") }
        findViewById<Button>(R.id.btn6).setOnClickListener { onNumberClick("6") }
        findViewById<Button>(R.id.btn7).setOnClickListener { onNumberClick("7") }
        findViewById<Button>(R.id.btn8).setOnClickListener { onNumberClick("8") }
        findViewById<Button>(R.id.btn9).setOnClickListener { onNumberClick("9") }

        // Các nút chức năng
        findViewById<Button>(R.id.btnBS).setOnClickListener { onBackspaceClick() }
        findViewById<Button>(R.id.btnCE).setOnClickListener { onCEClick() }
        findViewById<Button>(R.id.btnC).setOnClickListener { onCClick() }

        // Các nút toán tử
        findViewById<Button>(R.id.btnPlus).setOnClickListener { onOperatorClick("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { onOperatorClick("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { onOperatorClick("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { onOperatorClick("/") }
        findViewById<Button>(R.id.btnEquals).setOnClickListener { onEqualsClick() }

        // Nút dấu thập phân
        findViewById<Button>(R.id.btnDot).setOnClickListener { onDecimalClick() }

        // Nút đổi dấu
        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener { onPlusMinusClick() }

        updateDisplay()
    }

    // Xử lý khi nhấn nút số
    private fun onNumberClick(number: String) {
        if (newNumber) {
            currentNumber = number
            newNumber = false
        } else {
            currentNumber = if (currentNumber == "0") number else currentNumber + number
        }
        updateDisplay()
    }

    // Xử lý khi nhấn nút dấu thập phân
    private fun onDecimalClick() {
        if (newNumber) {
            currentNumber = "0."
            newNumber = false
        } else if (!currentNumber.contains(".")) {
            currentNumber += "."
        }
        updateDisplay()
    }

    // Xử lý khi nhấn nút đổi dấu
    private fun onPlusMinusClick() {
        if (currentNumber != "0") {
            currentNumber = if (currentNumber.startsWith("-")) {
                currentNumber.substring(1)
            } else {
                "-$currentNumber"
            }
        }
        updateDisplay()
    }

    // Xử lý khi nhấn nút Backspace - Xóa chữ số hàng đơn vị
    private fun onBackspaceClick() {
        if (currentNumber.length > 1) {
            // Xóa ký tự cuối cùng
            currentNumber = currentNumber.dropLast(1)
            // Nếu chỉ còn dấu "-" thì đổi về "0"
            if (currentNumber == "-") {
                currentNumber = "0"
            }
        } else {
            // Nếu chỉ còn 1 ký tự thì đổi về "0"
            currentNumber = "0"
        }
        updateDisplay()
    }

    // Xử lý khi nhấn nút CE - Xóa toán hạng hiện tại về 0
    private fun onCEClick() {
        currentNumber = "0"
        newNumber = true
        updateDisplay()
    }

    // Xử lý khi nhấn nút C - Xóa toàn bộ, nhập lại từ đầu
    private fun onCClick() {
        currentNumber = "0"
        firstOperand = 0.0
        operation = null
        newNumber = true
        lastOperand = 0.0
        lastOperation = null
        updateDisplay()
        updateHistory()
    }

    // Xử lý khi nhấn nút toán tử
    private fun onOperatorClick(op: String) {
        if (!newNumber) {
            calculateResult()
        }
        firstOperand = currentNumber.toDouble()
        operation = op
        newNumber = true
        updateHistory()
    }

    // Xử lý khi nhấn nút =
    private fun onEqualsClick() {
        if (operation != null) {
            val secondOperand = currentNumber.toDouble()
            updateHistory(showEquals = true)
            calculateResult()
            lastOperation = operation
            lastOperand = secondOperand
            operation = null
        } else if (lastOperation != null) {
            firstOperand = currentNumber.toDouble()
            operation = lastOperation
            currentNumber = lastOperand.toString()
            updateHistory(showEquals = true)
            calculateResult()
        }
    }

    // Tính toán kết quả
    private fun calculateResult() {
        val secondOperand = currentNumber.toDouble()
        val result = when (operation) {
            "+" -> firstOperand + secondOperand
            "-" -> firstOperand - secondOperand
            "×" -> firstOperand * secondOperand
            "/" -> if (secondOperand != 0.0) firstOperand / secondOperand else Double.NaN
            else -> secondOperand
        }
        currentNumber = formatResult(result)
        firstOperand = result
        newNumber = true
        updateDisplay()
    }

    // Định dạng kết quả hiển thị
    private fun formatResult(number: Double): String {
        return if (number.isNaN() || number.isInfinite()) {
            "Error"
        } else if (number % 1.0 == 0.0) {
            number.toLong().toString()
        } else {
            number.toString()
        }
    }

    // Cập nhật hiển thị
    private fun updateDisplay() {
        displayTextView.text = currentNumber
    }

    // Cập nhật lịch sử
    private fun updateHistory(showEquals: Boolean = false) {
        val history = when {
            operation != null && !showEquals -> {
                "${formatResult(firstOperand)} $operation"
            }
            operation != null && showEquals -> {
                "${formatResult(firstOperand)} $operation ${currentNumber} ="
            }
            else -> ""
        }
        historyTextView.text = history
    }
}