package wnukjedrzej.rst_zadanie_rekrutacyjne;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GameActivity extends Activity {
    private TextView textViewRemainCards;
    private TextView textViewDeckID;
    private TextView textViewCardsSet;
    private Button buttonShuffleDeck;
    private Button buttonChangeCard;
    private ImageView imageViewCard1;
    private ImageView imageViewCard2;
    private ImageView imageViewCard3;
    private ImageView imageViewCard4;
    private ImageView imageViewCard5;
    private Deck deck;
    private Card[] cards;
    private int activeCard = -1; // zmienna określająca, która karta została wybrana do wymiany
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent myIntent = getIntent();
        Bundle myBundle = myIntent.getExtras();
        int decksNumber = myBundle.getInt("decksNumber");
        textViewCardsSet = (TextView) findViewById(R.id.textViewCardsSet);
        textViewDeckID = (TextView)findViewById(R.id.textViewDeckID);
        textViewRemainCards = (TextView)findViewById(R.id.textViewCardsRemained);
        buttonChangeCard = (Button)findViewById(R.id.buttonChangeCard);
        buttonShuffleDeck = (Button)findViewById(R.id.buttonShuffle);
        imageViewCard1 = (ImageView)findViewById(R.id.imageView1);
        imageViewCard2 = (ImageView)findViewById(R.id.imageView2);
        imageViewCard3 = (ImageView)findViewById(R.id.imageView3);
        imageViewCard4 = (ImageView)findViewById(R.id.imageView4);
        imageViewCard5 = (ImageView)findViewById(R.id.imageView5);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.loading_data_title_text));
        progressDialog.setMessage(getResources().getString(R.string.loading_data_msg_text));
        progressDialog.setCancelable(false);

        imageViewCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeCard = 0;
                clearColors();
                imageViewCard1.setColorFilter(Color.argb(255,150,150,200), android.graphics.PorterDuff.Mode.MULTIPLY );
            }
        });
        imageViewCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeCard = 1;
                clearColors();
                imageViewCard2.setColorFilter(Color.argb(255,150,150,200), android.graphics.PorterDuff.Mode.MULTIPLY );
            }
        });
        imageViewCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeCard = 2;
                clearColors();
                imageViewCard3.setColorFilter(Color.argb(255,150,150,200), android.graphics.PorterDuff.Mode.MULTIPLY );
            }
        });
        imageViewCard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeCard = 3;
                clearColors();
                imageViewCard4.setColorFilter(Color.argb(255,150,150,200), android.graphics.PorterDuff.Mode.MULTIPLY );
            }
        });
        imageViewCard5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeCard = 4;
                clearColors();
                imageViewCard5.setColorFilter(Color.argb(255,150,150,200), android.graphics.PorterDuff.Mode.MULTIPLY );
            }
        });
        deck = new Deck(decksNumber);
        cards = new Card[5];
        new getDeckUrlExecutor().execute(deck.getDeckApiUrl());
    }
    private void clearColors(){
        imageViewCard1.setColorFilter(Color.argb(255,255,255,255), android.graphics.PorterDuff.Mode.MULTIPLY );
        imageViewCard2.setColorFilter(Color.argb(255,255,255,255), android.graphics.PorterDuff.Mode.MULTIPLY );
        imageViewCard3.setColorFilter(Color.argb(255,255,255,255), android.graphics.PorterDuff.Mode.MULTIPLY );
        imageViewCard4.setColorFilter(Color.argb(255,255,255,255), android.graphics.PorterDuff.Mode.MULTIPLY );
        imageViewCard5.setColorFilter(Color.argb(255,255,255,255), android.graphics.PorterDuff.Mode.MULTIPLY );
    }
    public void changeOneCard(View v){
        if(deck.getCardsRemain() > 0) {
            if(activeCard == -1){
                Toast.makeText(v.getContext(), getResources().getString(R.string.choose_card_text), Toast.LENGTH_SHORT).show();
            } else {
                cards[activeCard] = cards[0]; // karta na pozycji 0 zostanie zastapiona przez nowa karte, dlatego karta z pozycji 0 wstawiana jest w miejsce wybranej karty
                //activeCard = -1;
                clearColors();
                new getCardsUrlExecutor().execute("1");
            }
        } else {
            Toast.makeText(v.getContext(), getResources().getString(R.string.zero_cards_remain), Toast.LENGTH_SHORT).show();
        }
    }
    public void shuffleDeck(View v){
        activeCard = -1;
        clearColors();
        new shuffleDeckUrlExecutor().execute();
    }
    private void drawCards(){
        if(cards[0] != null) {
            imageViewCard1.setImageBitmap(cards[0].getBmp());
        }
        if(cards[1] != null) {
            imageViewCard2.setImageBitmap(cards[1].getBmp());
        }
        if(cards[2] != null) {
            imageViewCard3.setImageBitmap(cards[2].getBmp());
        }
        if(cards[3] != null) {
            imageViewCard4.setImageBitmap(cards[3].getBmp());
        }
        if(cards[4] != null) {
            imageViewCard5.setImageBitmap(cards[4].getBmp());
        }
        progressDialog.dismiss();
    }
    private void checkCardSet(){

    }
    // przetasowanie bedzie pobieralo piec nowych kart
    private class getDeckUrlExecutor extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            buttonChangeCard.setEnabled(false);
            buttonShuffleDeck.setEnabled(false);
            progressDialog.show();
        }

        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = buffer.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    buffer.close();

                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                Log.e("myERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            buttonChangeCard.setEnabled(true);
            buttonShuffleDeck.setEnabled(true);
            if(response!= null) {
                try {
                    JSONObject obj = (JSONObject) new JSONTokener(response).nextValue();
                    deck.setID(obj.getString("deck_id"));
                    deck.setCardsRemain(obj.getInt("remaining"));
                    textViewDeckID.setText(getResources().getString(R.string.decks_id_text)+" "+deck.getDeckID());
                    textViewRemainCards.setText(getResources().getString(R.string.cards_remain_text)+" "+deck.getCardsRemain());
                    new getCardsUrlExecutor().execute("5"); // pobieramy pięć kart z tali
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(GameActivity.this,getResources().getString(R.string.data_download_error_text),Toast.LENGTH_SHORT);
                    Log.e("myERROR", e.getMessage(), e);
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(GameActivity.this, getResources().getString(R.string.data_download_error_text),Toast.LENGTH_SHORT);
            }
        }
    }

    private class getCardsUrlExecutor extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            buttonChangeCard.setEnabled(false);
            buttonShuffleDeck.setEnabled(false);
            progressDialog.show();
        }

        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(deck.getGetCardsApiUrl(urls[0]));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = buffer.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    buffer.close();
                    String response = stringBuilder.toString();
                    if(response!=null) {
                        try {
                            JSONObject obj = (JSONObject) new JSONTokener(response).nextValue();
                            deck.setCardsRemain(obj.getInt("remaining"));
                            JSONArray cardsData = obj.getJSONArray("cards");
                            for (int index = 0; index < cardsData.length(); index++) {
                                JSONObject currentCard = cardsData.getJSONObject(index);
                                try {
                                    InputStream imgStream = new java.net.URL(currentCard.getString("image")).openStream();
                                    if(cardsData.length() > 1) {
                                        cards[index] = new Card(currentCard.getString("suit"), currentCard.getString("value"), currentCard.getString("image"), BitmapFactory.decodeStream(imgStream));
                                    } else {
                                        cards[activeCard] = new Card(currentCard.getString("suit"), currentCard.getString("value"), currentCard.getString("image"), BitmapFactory.decodeStream(imgStream));
                                        activeCard = -1;
                                    }
                                    imgStream.close();
                                } catch (Exception e) {
                                    Toast.makeText(GameActivity.this, getResources().getString(R.string.data_download_error_text), Toast.LENGTH_SHORT);
                                    Log.e("myERROR", e.getMessage(), e);
                                    return null; // przerywamy petle i konczymy ta funkcje aby nie wykonala sie dalsza czesc kodu
                                }
                            }
                            //textViewDeckID.setText(getResources().getString(R.string.decks_id_text)+" "+deck.getDeckID());
                            //textViewRemainCards.setText(getResources().getString(R.string.cards_remain_text) + " " + deck.getCardsRemain());
                            //drawCards();
                        } catch (JSONException e) {
                            Toast.makeText(GameActivity.this, getResources().getString(R.string.data_download_error_text), Toast.LENGTH_SHORT);
                            Log.e("myERROR", e.getMessage(), e);
                        }
                        return String.valueOf(deck.getCardsRemain());
                    } else {
                        return null;
                    }

                } finally {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                Log.e("myERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            buttonChangeCard.setEnabled(true);
            buttonShuffleDeck.setEnabled(true);
            if(response!= null) {
                textViewRemainCards.setText(getResources().getString(R.string.cards_remain_text) + " " +response);
                drawCards();
                checkCardSet();
            } else {
                progressDialog.dismiss();
                Toast.makeText(GameActivity.this, getResources().getString(R.string.data_download_error_text),Toast.LENGTH_SHORT);
            }

        }
    }

    private class shuffleDeckUrlExecutor extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            buttonChangeCard.setEnabled(false);
            buttonShuffleDeck.setEnabled(false);
            progressDialog.show();
        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(deck.getShuffleCardsApiUrl());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = buffer.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    buffer.close();

                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                Log.e("myERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            buttonChangeCard.setEnabled(true);
            buttonShuffleDeck.setEnabled(true);

            if(response!= null) {
                try {
                    JSONObject obj = (JSONObject) new JSONTokener(response).nextValue();

                    deck.setCardsRemain(obj.getInt("remaining"));
                    boolean shuffled = obj.getBoolean("shuffled");
                    textViewRemainCards.setText(getResources().getString(R.string.cards_remain_text)+" "+deck.getCardsRemain());
                    if(shuffled) {
                        new getCardsUrlExecutor().execute("5"); // pobieramy pięć kart z tali
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(GameActivity.this,getResources().getString(R.string.wrong_shuffle_text),Toast.LENGTH_SHORT);
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(GameActivity.this,getResources().getString(R.string.data_download_error_text),Toast.LENGTH_SHORT);
                    Log.e("myERROR", e.getMessage(), e);
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(GameActivity.this, getResources().getString(R.string.data_download_error_text),Toast.LENGTH_SHORT);
            }

        }
    }
}
