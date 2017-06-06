package wnukjedrzej.rst_zadanie_rekrutacyjne;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText editTextDecksNumbers;
    private Button buttonNewGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextDecksNumbers = (EditText) findViewById(R.id.editTextDecksNumbers);
        buttonNewGame = (Button) findViewById(R.id.buttonStartGame);
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInternetConnection()){
                    //jezeli mamy dostep do internetu
                    
                } else {
                    //brak dostepu do internetu
                    Toast.makeText(v.getContext(),R.string.no_internet_connection_text,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean checkInternetConnection(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
