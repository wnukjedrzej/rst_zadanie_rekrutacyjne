package wnukjedrzej.rst_zadanie_rekrutacyjne;

/**
 * Created by Jędrzej on 2017-06-06.
 */
public class Card {
    private int cardValue;
    private int colorValue;
    private String imageAddress;

    public Card(String suit, String value, String _image){
        colorValue = convertToColorValue(suit);
        cardValue = convertToCardValue(value);
        imageAddress = _image;
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
}
