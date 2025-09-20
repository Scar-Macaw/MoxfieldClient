package moe.scar;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


import static moe.scar.Renderer.*;

public class Main {


    //todo check if all of these are necessary
    static ArrayList<String> decks = new ArrayList<>();
    static ArrayList<Deck> deckLists = new ArrayList<>();
    static boolean inDeck = false;
    static int magic = 0, pages;
    static int selectedIndex = 0, selectedDeck = 0, selectedIndexCard = 0;

    public static void main(String[] args) throws IOException, InterruptedException {

        var terminal = new DefaultTerminalFactory().createTerminal();
        var screen = new TerminalScreen(terminal);
        try {
            screen.startScreen();
            screen.setCursorPosition(null);
            TextGraphics textGraphics = screen.newTextGraphics();
            //todo add scanning of decks
            deckLists.add(new Deck("/home/scar/IdeaProjects/MoxfieldClient1/decklists/skull-ce & gab-briar.txt"));
            deckLists.add(new Deck("decklists/Magda $100 cad.txt"));
            deckLists.add(new Deck("decklists/hi.txt"));

            deckLists.add(new Deck("decklists/skull.txt"));
            decks.add("skull");
            decks.add("pigeon");
            decks.add("vito");
            decks.add("magda");
            addImportedLists(deckLists);
            while (true) {
                KeyStroke key = screen.pollInput();
                TerminalSize newSize = screen.doResizeIfNecessary();
                if (newSize != null) {
                    screen.clear();
                    borders(screen, newSize);
                    render(screen, newSize, textGraphics);
                }


                if (key != null) {

                    if(!inDeck){

                        switch (key.getKeyType()) {
                            case ArrowUp:
                                selectedIndex = (selectedIndex - 1 + deckLists.size()) % deckLists.size();
                                break;
                            case ArrowDown:
                                selectedIndex = (selectedIndex + 1) % deckLists.size();
                                break;
                            case ArrowRight:
                            case Enter:
                                selectedIndexCard = 0;
                                selectedDeck = selectedIndex;
                                inDeck = true;
                                break;
                            case Escape:
                                break;
                        }
                    } else{
                        switch (key.getKeyType()) {
                            //todo clean up code here
                            case ArrowUp:
                                selectedIndexCard = (selectedIndexCard - 1 + deckLists.get(selectedIndex).getDeckList().size()) % deckLists.get(selectedIndex).getDeckList().size();
                                break;
                            case ArrowDown:
                                selectedIndexCard = (selectedIndexCard + 1) % deckLists.get(selectedIndex).getDeckList().size();
                                //todo add wrapping for the decklist
                                break;
                            case ArrowLeft:
                                inDeck = false;
                                break;
                                case ArrowRight:
                                    if (magic < pages-1){
                                        magic++;
                                    } else { magic = 0;}
                                    break;

                        }
                    }

                }

                if (key != null) {
                    textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
                    textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
                    textGraphics.fill(' ');
                    if (key.getKeyType() == KeyType.Escape) {
                        break;
                    }
                }
                render(screen, screen.getTerminalSize(), textGraphics);
                Thread.sleep(100);
            }
        } finally {
            screen.stopScreen();
        }

    }


    public static void addImportedLists(ArrayList<Deck> decks){
        for (Deck deck : decks) {
            try {
                deck.loadDeck();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }


    private static int centerX(int width, String text){
        return (width/2-text.length()/2);
    }
    private static void setColors(TextGraphics tg, boolean selected) {
        if (selected) {
            tg.setBackgroundColor(TextColor.ANSI.BLUE);
            tg.setForegroundColor(TextColor.ANSI.WHITE);
        } else {
            tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
            tg.setForegroundColor(TextColor.ANSI.DEFAULT);
        }
    }

    public static void render(Screen screen, TerminalSize size, TextGraphics tg) throws IOException {

        //TODO Clean up magic numbers
        //TODO clean up code
        //TODO put render code into methods
        borders(screen, size);
        //decklist list to content separator
        drawVerticalLine(size, screen, 2, size.getColumns()/3, size.getRows()-3, Symbols.DOUBLE_LINE_VERTICAL);

        //top bar separator

        drawHorizontalLine(size, screen, 0, 2, size.getColumns(), Symbols.DOUBLE_LINE_HORIZONTAL);


        tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
        tg.setForegroundColor(TextColor.ANSI.DEFAULT);
        tg.putString(2, 1, "Moxfield Tui");
        tg.putString(size.getColumns() - 9, 1, "v1.0.0");
        int i;
            for (i = 0; i < deckLists.size(); i++){
                setColors(tg, i == selectedIndex);

                tg.putString(centerX(size.getColumns()/3-1, deckLists.get(i).getName()), 3+3*i, deckLists.get(i).getName());
                drawHorizontalLine(size, screen, 0, 5+3*i,size.getColumns()/3, Symbols.DOUBLE_LINE_HORIZONTAL);


            }

        //deck separator
        int m = 0;
            
            int itemsPerPage;
            
            pages = deckLists.get(selectedDeck).getDeckList().size() / size.getRows();
            itemsPerPage = (deckLists.get(selectedDeck).getDeckList().size() / pages)-8;


            String[][] visibleCards = new String[pages][itemsPerPage];
            for (int k = 0; k < visibleCards.length ; k++) {
                for (int l = 0; l < visibleCards[k].length; l++){
                    visibleCards[k][l] = deckLists.get(selectedDeck).getDeckList().get(m).toString();
                    m++;
                }
            }

            tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
            tg.setForegroundColor(TextColor.ANSI.DEFAULT);

            for (int j = 0; j < visibleCards[magic].length; j++) {
                setColors(tg, j == selectedIndex);
                tg.putString(size.getColumns()-maxCardName(deckLists.get(selectedDeck)),3+j, visibleCards[magic][j]);

            }




        //account info separator
        drawHorizontalLine(size, screen, 0,size.getRows()-5,size.getColumns()/3, Symbols.DOUBLE_LINE_HORIZONTAL);

        screen.refresh();
    }
    public static int maxCardName(Deck deck){
        int length = 0;
        for (int k = 0; k < deck.getDeckList().size(); k++){
            if (deck.getDeckList().get(k).toString().length() > length){
                length = deck.getDeckList().get(k).toString().length();
            }
        }
        return length;
    }

    }
