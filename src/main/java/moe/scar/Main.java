package moe.scar;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalFactory;
import com.googlecode.lanterna.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


import static moe.scar.Renderer.*;

public class Main {

    private static boolean testMode = true;
    static ArrayList<String> decks = new ArrayList<>();
    static ArrayList<Deck> deckLists = new ArrayList<>();
    static boolean inDeck = false;
    static int maxVisible = 0, scrollOffset= 0, selectedIndex = 0, selectedDeck = 0, selectedIndexCard = 0;

    public static void main(String[] args) throws IOException, InterruptedException {

        var terminal = new DefaultTerminalFactory().createTerminal();
        var screen = new TerminalScreen(terminal);
        deckData hi = new deckData();
        try {
            screen.startScreen();
            screen.setCursorPosition(null);
            TerminalSize size = screen.getTerminalSize();
            TextGraphics textGraphics = screen.newTextGraphics();
            deckLists.add(new Deck("/home/scar/IdeaProjects/MoxfieldClient1/decklists/skull-ce & gab-briar.txt"));
            deckLists.add(new Deck("decklists/Magda $100 cad.txt"));
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
//                            System.out.println("Selected: " + deckLists.get(selectedIndex));
                                break;
                            case Escape:
                                break;
                        }
                    } else{
                        switch (key.getKeyType()) {
                            case ArrowUp:
                                selectedIndexCard = (selectedIndexCard - 1 + deckLists.get(selectedIndex).getDeckList().size()) % deckLists.get(selectedIndex).getDeckList().size();
                                break;
                            case ArrowDown:
                                selectedIndexCard = (selectedIndexCard + 1) % deckLists.get(selectedIndex).getDeckList().size();
                                break;
                            case ArrowLeft:
                                inDeck = false;
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
        for (int i = 0; i < decks.size(); i++) {
            try {
                decks.get(i).loadDeck();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }
    private static int centerX(int width, String text){
        return (width/2-text.length()/2);
    }

    public static void render(Screen screen, TerminalSize size, TextGraphics tg) throws IOException {
        maxVisible = size.getRows()-6;
        borders(screen, size);

        //deck  list list to content seperator
        drawVerticalLine(size, screen, 2, size.getColumns()/3, size.getRows()-3, Symbols.DOUBLE_LINE_VERTICAL);

        //top bar seperator

        drawHorizontalLine(size, screen, 0, 2, size.getColumns(), Symbols.DOUBLE_LINE_HORIZONTAL);


        tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
        tg.setForegroundColor(TextColor.ANSI.DEFAULT);
        tg.putString(2, 1, "Moxfield Tui");
        tg.putString(size.getColumns() - 9, 1, "v1.0.0");
        int i;
            for (i = 0; i < deckLists.size(); i++){
                if (i == selectedIndex) {
                    tg.setBackgroundColor(TextColor.ANSI.BLUE);
                    tg.setForegroundColor(TextColor.ANSI.WHITE);
                } else {
                    tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
                    tg.setForegroundColor(TextColor.ANSI.DEFAULT);
                }
                //(size.getColumns()/3-1)/2-(decks.get(i).length()/2)
                tg.putString(centerX(size.getColumns()/3-1, deckLists.get(i).getName()), 3+3*i, deckLists.get(i).getName());
                drawHorizontalLine(size, screen, 0, 5+3*i,size.getColumns()/3, Symbols.DOUBLE_LINE_HORIZONTAL);


            }
//        tg.putString(centerX(size.getColumns()/3-1, "Add +"), 3+3*i, "Add +");
//        drawHorizontalLine(size, screen, 0, 5+3*i,size.getColumns()/3, Symbols.DOUBLE_LINE_HORIZONTAL);
        //deck seperator

//        for (i = 0; i < deckLists.size(); i++) {
        tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
        tg.setForegroundColor(TextColor.ANSI.DEFAULT);
        int j;
            for (j = 0; j < deckLists.get(selectedDeck).getDeckList().size(); j++) {
                if (j == selectedIndexCard) {
                    tg.setBackgroundColor(TextColor.ANSI.BLUE);
                    tg.setForegroundColor(TextColor.ANSI.WHITE);
                } else {
                    tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
                    tg.setForegroundColor(TextColor.ANSI.DEFAULT);
                }
                tg.putString(size.getColumns()/3+2,3+j,deckLists.get(selectedDeck).getDeckList().get(j).toString());

            }
//        }


        //account info seperator
        drawHorizontalLine(size, screen, 0,size.getRows()-5,size.getColumns()/3, Symbols.DOUBLE_LINE_HORIZONTAL);

        screen.refresh();
    }

    }
