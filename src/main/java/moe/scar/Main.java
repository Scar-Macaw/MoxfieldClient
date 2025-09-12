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
import java.io.IOException;
import java.util.ArrayList;

import static moe.scar.Renderer.*;

public class Main {

    static ArrayList<String> decks = new ArrayList<>();
    static int selectedIndex = 0;

    public static void main(String[] args) throws IOException, InterruptedException {

        var terminal = new DefaultTerminalFactory().createTerminal();
        var screen = new TerminalScreen(terminal);

        try {
            screen.startScreen();
            screen.setCursorPosition(null);
            TerminalSize size = screen.getTerminalSize();
            TextGraphics textGraphics = screen.newTextGraphics();
            decks.add("skull");
            decks.add("pigeon");
            decks.add("vito");
            decks.add("magda");
            while (true) {
                KeyStroke key = screen.pollInput();
                TerminalSize newSize = screen.doResizeIfNecessary();
                if (newSize != null) {
                    screen.clear();
                    borders(screen, newSize);
                    render(screen, newSize, textGraphics);
                }


                if (key != null) {
                    switch (key.getKeyType()) {
                        case ArrowUp:
                            selectedIndex = (selectedIndex - 1 + decks.size()) % decks.size();
                            break;
                        case ArrowDown:
                            selectedIndex = (selectedIndex + 1) % decks.size();
                            break;
                        case Enter:
                            System.out.println("Selected: " + decks.get(selectedIndex));
                            break;
                        case Escape:
                            break;
                    }
                }

                if (key != null) {
                    if (key.getKeyType() == KeyType.Escape) {
                        break;
                    }
                    if (key.getKeyType() == KeyType.ArrowDown) {

                    } else if (key.getKeyType() == KeyType.ArrowUp) {

                    }
                }
                render(screen, screen.getTerminalSize(), textGraphics);
                Thread.sleep(100);
            }
        } finally {
            screen.stopScreen();
        }

    }
    private static int centerX(int width, String text){
        return (width/2-text.length()/2);
    }
    public static void render(Screen screen, TerminalSize size, TextGraphics tg) throws IOException {

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
        for (i = 0; i < decks.size(); i++){
            if (i == selectedIndex) {
                tg.setBackgroundColor(TextColor.ANSI.BLUE);
                tg.setForegroundColor(TextColor.ANSI.WHITE);
            } else {
                tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
                tg.setForegroundColor(TextColor.ANSI.DEFAULT);
            }
            //(size.getColumns()/3-1)/2-(decks.get(i).length()/2)
            tg.putString(centerX(size.getColumns()/3-1, decks.get(i)), 3+3*i, decks.get(i));
            drawHorizontalLine(size, screen, 0, 5+3*i,size.getColumns()/3, Symbols.DOUBLE_LINE_HORIZONTAL);


        }
//        tg.putString(centerX(size.getColumns()/3-1, "Add +"), 3+3*i, "Add +");
//        drawHorizontalLine(size, screen, 0, 5+3*i,size.getColumns()/3, Symbols.DOUBLE_LINE_HORIZONTAL);
        //deck seperator


        //account info seperator
        drawHorizontalLine(size, screen, 0,size.getRows()-10,size.getColumns()/3, Symbols.DOUBLE_LINE_HORIZONTAL);

        screen.refresh();
    }

    }
