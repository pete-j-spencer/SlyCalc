package com.spencer.slycalc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.List;


public class CalcActivity extends Activity {

    private boolean didEquals = false;
    TextView valueDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        valueDisplay = (TextView)findViewById(R.id.valueDisplay);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickNumberButton(View view) {
        if(didEquals) {
            valueDisplay.setText("");
            didEquals = false;
        }
        Button b = (Button)view;
        String buttonText = b.getText().toString();

        String currentValue = valueDisplay.getText().toString();

        valueDisplay.setText(currentValue + buttonText);


    }

    public void clearValueDisplay(View view) {
        valueDisplay.setText("");
    }

    public void calculateIt(View view) {
        String screen = valueDisplay.getText().toString();

        List<String> errorMessages = SubParser.validate(screen);

        if(errorMessages.isEmpty()) {
            String answer = SubParser.eval(screen);
            valueDisplay.setText(screen + "\n=\n" + answer);
            didEquals = true;
        }
        else {
            Toast.makeText(getApplicationContext(), errorMessages.toString(),
            Toast.LENGTH_LONG).show();
        }


    }

    public void backspace(View view) {
        String screen = valueDisplay.getText().toString();
        if(screen.length() > 0) {
            valueDisplay.setText(screen.substring(0,screen.length() - 1));
        }
    }
}
