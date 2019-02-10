package com.abelhzo.calculator.activities;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abelhzo.calculator.services.CalculationService;
import com.abelhzo.calculator.services.CalculationServiceImpl;
import com.abelhzo.calculator.services.MemoryCalculator;
import com.abelhzo.calculator.services.MemoryCalculatorImpl;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8,
            button9, buttonClear, buttonDivision, buttonMultiply, buttonBackspace, buttonSubstract,
            buttonAdd, buttonResult, buttonPoint, buttonMC, buttonMAdd, buttonMSub, buttonMRead;
    private boolean flagPoint, flagNewOperation;
    private CalculationService calculationService;
    private MemoryCalculator memoryCalculator;
    private Typeface typeface;
    private String font = "fonts/digital-7.ttf";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.info:
                showAlertDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAlertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Calculadora hecha por Abel HZO");
        dialog.setMessage("Esta calculadora fue hecha por Abel HZO @ 2018");
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CalculatorActivity.this, "Culaculadora Android 2018 @ AbelHZO", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        calculationService = new CalculationServiceImpl();
        memoryCalculator = new MemoryCalculatorImpl();

        editText = findViewById(R.id.display);
        this.typeface = Typeface.createFromAsset(getAssets(), font);
        editText.setTypeface(this.typeface);
        editText.setInputType(InputType.TYPE_NULL);

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        buttonClear = findViewById(R.id.buttonClear);
        buttonDivision = findViewById(R.id.buttonDivision);
        buttonMultiply = findViewById(R.id.buttonMultiply);
        buttonBackspace = findViewById(R.id.buttonBackspace);
        buttonSubstract = findViewById(R.id.buttonSubstract);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonResult = findViewById(R.id.buttonResult);
        buttonPoint = findViewById(R.id.buttonPoint);
        buttonMC = findViewById(R.id.buttonMC);
        buttonMAdd  = findViewById(R.id.buttonMAdd);
        buttonMSub = findViewById(R.id.buttonMSub);
        buttonMRead = findViewById(R.id.buttonMRead);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonClear.setOnClickListener(this);
        buttonDivision.setOnClickListener(this);
        buttonMultiply.setOnClickListener(this);
        buttonBackspace.setOnClickListener(this);
        buttonSubstract.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        buttonResult.setOnClickListener(this);
        buttonPoint.setOnClickListener(this);
        buttonMC.setOnClickListener(this);
        buttonMAdd.setOnClickListener(this);
        buttonMSub.setOnClickListener(this);
        buttonMRead.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Button button = findViewById(v.getId());
        String textButton = button.getText().toString();
        String operation = editText.getText().toString().trim();

        if(textButton.matches("([0-9]+)")) {
            if(flagNewOperation) {
                operation = "";
                flagNewOperation = false;
                flagPoint = false;
            }
            operation += textButton;
        } else if(textButton.matches("(\\+|-|x|/)")) {
            operation = validateSigno(operation, textButton);
            flagNewOperation = false;
        } else if(textButton.equals("◄") && operation.length() != 0) {
            validatePoint(operation, "");
            operation = operation.substring(0, operation.length() - 1);
        } else if(textButton.equals("C")) {
            operation = "";
            flagPoint = false;
            flagNewOperation = true;
        }  else if(textButton.equals(".") && !flagPoint) {
            if(flagNewOperation) {
                operation = "";
                flagNewOperation = false;
                flagPoint = false;
            }
            operation += textButton;
            flagPoint = true;
        } else if(textButton.equals("=") && operation.length() != 0) {
            operation = resultWithFormat(operation);
            validatePoint(operation, "=");
            flagNewOperation = true;
        } else if(textButton.equals("M+")) {
            operation = resultWithFormat(operation);
            if(operation.equals("0")) {
                operation = "";
                memoryCalculator.memoryClear();
                buttonMRead.setBackgroundResource(R.drawable.style_buttons);
            } else {
                memoryCalculator.memoryAdd(Double.parseDouble(operation));
                buttonMRead.setBackgroundResource(R.drawable.btn_mread);
            }
        } else if(textButton.equals("M-")) {
            operation = resultWithFormat(operation);
            if(operation.equals("0")) {
                operation = "";
                memoryCalculator.memoryClear();
                buttonMRead.setBackgroundResource(R.drawable.style_buttons);
            } else {
                memoryCalculator.memorySubs(Double.parseDouble(operation));
                buttonMRead.setBackgroundResource(R.drawable.btn_mread);
            }
        } else if(textButton.equals("MR")) {
            if(flagNewOperation) {
                operation = "";
                flagNewOperation = false;
            }
            operation += memoryCalculator.memoryRead();
            if(operation.length() != 0)
                validatePoint(operation, "MR");
        } else if(textButton.equals("MC")) {
            memoryCalculator.memoryClear();
            buttonMRead.setBackgroundResource(R.drawable.style_buttons);
        }

        editText.setText(operation);
    }

    private String resultWithFormat(String operation) {
        Double result = calculationService.calculation(operation);
        DecimalFormat df = new DecimalFormat("#.########");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(result);
    }

    private String validateSigno(String operation, String textButton) {
        if(textButton.equals("-")) {

            if(operation.length() == 0)
                operation += textButton;
            else {
                String lastChar = operation.substring(operation.length() - 1, operation.length());
                if(lastChar.matches("(x|/|[0-9])")) {
                    operation += textButton;
                    flagPoint = false;
                }
            }

        } else if(textButton.matches("(\\+|x|/)") && operation.length() != 0) {
            String lastChar = operation.substring(operation.length() - 1, operation.length());
            if(lastChar.matches("([0-9])")) {
                operation += textButton;
                flagPoint = false;
            }
        }

        return operation;
    }

    private void validatePoint(String operation, String result) {
        String lastChar = operation.substring(operation.length() - 1, operation.length());

        if(lastChar.equals("."))
            flagPoint = false;

        if(lastChar.matches("(\\+|x|/)") || result.equals("MR")) {

            for(int i = operation.length(); i > 0; i--) {
                String lastCharDigit = operation.substring(i - 1, i);
                if(lastCharDigit.equals(".")) {
                    flagPoint = true;
                    break;
                } else if(lastCharDigit.matches("(\\+|-|x|/)") && i == operation.length()) {
                    continue;
                } else if(lastCharDigit.matches("(\\+|-|x|/)")) {
                    break;
                }
            }

        } else if(lastChar.equals("-"))	{

            if(operation.length() < 2) return;

            String anteLastChar = operation.substring(operation.length() - 2, operation.length() - 1);
            if(anteLastChar.matches("(x|/)")) {
                flagPoint = false;
            }

        } else if(result.equals("=")) {

            for(int i = operation.length(); i > 0; i--) {
                String lastCharDigit = operation.substring(i - 1, i);
                if(lastCharDigit.equals(".")) {
                    flagPoint = true;
                    break;
                }
            }

        }
    }
}
