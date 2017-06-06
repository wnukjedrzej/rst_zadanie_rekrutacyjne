package wnukjedrzej.rst_zadanie_rekrutacyjne;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jędrzej on 2017-06-06.
 */
public class Deck {
    private String id;
    private int cardsRemain;
    private String deckApiUrl;
    public Deck(int decksNumber){
        deckApiUrl = "https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=" + decksNumber;
    }
    public void setID(String id){
        this.id = id;
    }
    public void setCardsRemain(int cardsRemain){
        this.cardsRemain = cardsRemain;
    }
    public String getDeckID(){
        return id;
    }
    public String getDeckApiUrl(){
        return deckApiUrl;
    }
    public int getCardsRemain(){
        return cardsRemain;
    }
}
