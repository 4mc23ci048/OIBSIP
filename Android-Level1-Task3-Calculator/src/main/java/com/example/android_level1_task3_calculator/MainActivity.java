package com.example.android_level1_task3_calculator;


import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvExpression;
    private TextView tvDisplay;

    private StringBuilder expression = new StringBuilder();

    private boolean justCalculated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tvExpression = findViewById(R.id.tvExpression);
        tvDisplay = findViewById(R.id.tvDisplay);

        setupNumberButtons();
        setupOperatorButtons();
        setupControlButtons();
    }

    // -----------------------------------------
    // NUMBER BUTTONS
    // -----------------------------------------

    private void setupNumberButtons() {

        int[] numberIds = {
                R.id.btn0,
                R.id.btn1,
                R.id.btn2,
                R.id.btn3,
                R.id.btn4,
                R.id.btn5,
                R.id.btn6,
                R.id.btn7,
                R.id.btn8,
                R.id.btn9
        };

        for (int id : numberIds) {

            Button button = findViewById(id);

            button.setOnClickListener(v -> {

                if (justCalculated) {
                    expression.setLength(0);
                    tvExpression.setText("");
                    justCalculated = false;
                }

                Button clickedButton = (Button) v;

                expression.append(
                        clickedButton.getText().toString()
                );

                updateDisplay();
            });
        }
    }

    // -----------------------------------------
    // OPERATOR BUTTONS
    // -----------------------------------------

    private void setupOperatorButtons() {

        int[] operatorIds = {
                R.id.btnPlus,
                R.id.btnMinus,
                R.id.btnMultiply,
                R.id.btnDivide
        };

        for (int id : operatorIds) {

            Button button = findViewById(id);

            button.setOnClickListener(v -> {

                if (expression.length() == 0) {
                    return;
                }

                justCalculated = false;

                char lastChar =
                        expression.charAt(expression.length() - 1);

                char newOperator =
                        ((Button) v).getText().charAt(0);

                // Prevent multiple operators together
                if (isOperator(lastChar)) {

                    expression.setCharAt(
                            expression.length() - 1,
                            newOperator
                    );

                } else {

                    expression.append(newOperator);
                }

                updateDisplay();
            });
        }
    }

    // -----------------------------------------
    // CONTROL BUTTONS
    // -----------------------------------------

    private void setupControlButtons() {

        // DECIMAL
        Button decimalButton =
                findViewById(R.id.btnDecimal);

        decimalButton.setOnClickListener(v -> {

            if (justCalculated) {
                expression.setLength(0);
                tvExpression.setText("");
                justCalculated = false;
            }

            String currentNumber = getCurrentNumber();

            // Prevent multiple decimal points
            if (!currentNumber.contains(".")) {

                if (expression.length() == 0 ||
                        isOperator(
                                expression.charAt(
                                        expression.length() - 1))) {

                    expression.append("0.");

                } else {

                    expression.append(".");
                }

                updateDisplay();
            }
        });

        // CLEAR
        Button clearButton =
                findViewById(R.id.btnClear);

        clearButton.setOnClickListener(v -> {

            expression.setLength(0);

            justCalculated = false;

            tvExpression.setText("");
            tvDisplay.setText("0");
        });

        // BACKSPACE
        Button backspaceButton =
                findViewById(R.id.btnBackspace);

        backspaceButton.setOnClickListener(v -> {

            if (expression.length() > 0) {

                expression.deleteCharAt(
                        expression.length() - 1
                );

                justCalculated = false;

                updateDisplay();
            }
        });

        // EQUALS
        Button equalsButton =
                findViewById(R.id.btnEquals);

        equalsButton.setOnClickListener(v -> calculate());
    }

    // -----------------------------------------
    // CALCULATE
    // -----------------------------------------

    private void calculate() {

        if (expression.length() == 0) {
            return;
        }

        String exp = expression.toString();

        // Cannot calculate if expression ends
        // with an operator
        if (isOperator(exp.charAt(exp.length() - 1))) {
            return;
        }

        try {

            double result = evaluateExpression(exp);

            // Division by zero
            if (Double.isInfinite(result) ||
                    Double.isNaN(result)) {

                tvExpression.setText(exp);
                tvDisplay.setText("Error");

                expression.setLength(0);

                justCalculated = true;

                return;
            }

            String resultText;

            // Remove .0 from whole numbers
            if (result == (long) result) {

                resultText =
                        String.valueOf((long) result);

            } else {

                resultText =
                        String.valueOf(result);
            }

            // Show expression on top
            tvExpression.setText(exp);

            // Show answer below
            tvDisplay.setText(resultText);

            // Store result
            expression.setLength(0);
            expression.append(resultText);

            justCalculated = true;

        } catch (Exception e) {

            tvExpression.setText(exp);
            tvDisplay.setText("Error");

            expression.setLength(0);

            justCalculated = true;
        }
    }

    // -----------------------------------------
    // EVALUATE EXPRESSION
    // -----------------------------------------

    private double evaluateExpression(String exp) {

        String[] numbers =
                exp.split("[+\\-*/]");

        String operators =
                exp.replaceAll("[0-9.]", "");

        if (numbers.length == 0) {
            throw new ArithmeticException();
        }

        double result =
                Double.parseDouble(numbers[0]);

        for (int i = 0;
             i < operators.length();
             i++) {

            double nextNumber =
                    Double.parseDouble(numbers[i + 1]);

            char operator =
                    operators.charAt(i);

            switch (operator) {

                case '+':

                    result += nextNumber;

                    break;

                case '-':

                    result -= nextNumber;

                    break;

                case '*':

                    result *= nextNumber;

                    break;

                case '/':

                    if (nextNumber == 0) {
                        throw new ArithmeticException();
                    }

                    result /= nextNumber;

                    break;

                default:

                    throw new ArithmeticException();
            }
        }

        return result;
    }

    // -----------------------------------------
    // CHECK OPERATOR
    // -----------------------------------------

    private boolean isOperator(char character) {

        return character == '+' ||
                character == '-' ||
                character == '*' ||
                character == '/';
    }

    // -----------------------------------------
    // GET CURRENT NUMBER
    // -----------------------------------------

    private String getCurrentNumber() {

        if (expression.length() == 0) {
            return "";
        }

        int index = expression.length() - 1;

        while (index >= 0 &&
                !isOperator(
                        expression.charAt(index))) {

            index--;
        }

        return expression.substring(index + 1);
    }

    // -----------------------------------------
    // UPDATE DISPLAY
    // -----------------------------------------

    private void updateDisplay() {

        if (expression.length() == 0) {

            tvDisplay.setText("0");

        } else {

            tvDisplay.setText(
                    expression.toString()
            );
        }
    }
}
