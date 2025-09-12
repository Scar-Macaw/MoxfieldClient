package moe.scar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Deck {
    protected String decklistPath;

    private ArrayList<Card> deckList;

    public Deck(String decklistPath) {
        this.decklistPath = decklistPath;
    }

    public ArrayList<Card> getDeckList() {
        return deckList;
    }

    public String getName(){
        return decklistPath.substring(decklistPath.lastIndexOf(File.separator) + 1);
    }

    @Override
    public String toString() {
        return "Deck{" +
                "deckList=" + deckList +
                ", decklistPath='" + decklistPath + '\'' +
                '}';
    }

    public void loadDeck() throws FileNotFoundException {
        File listFile = new File(decklistPath);
        if(listFile.exists()){
            Scanner scanFile = new Scanner(listFile);
            deckList = new ArrayList();
            while(scanFile.hasNext()){
                String[] tokens = scanFile.nextLine().split(" ");
                String cardName = "";
                for (int i = 1; i < tokens.length; i++) {
                    if (tokens.length == 1){
                        break;
                    }
                    if (i == tokens.length-1)
                        cardName += tokens[i];
                    else
                        cardName += tokens[i]+" ";
                }
                if(tokens.length != 1)
                    deckList.add(new Card(cardName, Integer.parseInt(tokens[0])));
            }
        }
    }
    static class Card {
        private String name;
        private int quantity;
        public Card(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;

        }

        @Override
        public String toString() {
            return quantity + " " + name;
        }
    }
}

