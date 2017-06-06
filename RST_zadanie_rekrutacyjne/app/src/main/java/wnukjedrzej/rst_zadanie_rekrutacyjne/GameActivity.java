package wnukjedrzej.rst_zadanie_rekrutacyjne;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent myIntent = getIntent();
        Bundle myBundle = myIntent.getExtras();
        int decksNumber = myBundle.getInt("decksNumber");
        Toast.makeText(this, String.valueOf(decksNumber), Toast.LENGTH_SHORT);
    }
}
