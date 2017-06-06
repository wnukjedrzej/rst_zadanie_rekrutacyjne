package wnukjedrzej.rst_zadanie_rekrutacyjne;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Jędrzej on 2017-06-06.
 */
public class Card implements Serializable {
    private int cardValue;
    private int colorValue;
    private String imageAddress;
    private transient Bitmap bmp;
    public Card(String suit, String value, String image, Bitmap bmp){
        colorValue = convertToColorValue(suit);
        cardValue = convertToCardValue(value);
        imageAddress = image;
        this.bmp = bmp;
    }
    private int convertToCardValue(String value){
        String[] array = {"ACE","2","3","4","5","6","7","8","9","10","JACK","QUEEN","KING"};
        for(int i = 0; i < array.length; i++){
            if(value.equals(array[i])) return i+1;
        }
        return 0;
    }
    private int convertToColorValue(String value){
        String[] array = {"DIAMONDS","HEARTS","CLUBS","SPADES"};
        for(int i = 0; i < array.length; i++){
            if(value.equals(array[i])) return i+1;
        }
        return 0;
    }
    public int getCardValue(){
        return cardValue;
    }
    public int getColorValue(){
        return colorValue;
    }
    public String getImageAddress(){
        return imageAddress;
    }
    public Bitmap getBmp(){
        return bmp;
    }
}
