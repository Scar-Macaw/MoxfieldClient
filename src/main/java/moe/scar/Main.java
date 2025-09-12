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

import static moe.scar.Renderer.*;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        var terminal = new DefaultTerminalFactory().createTerminal();
        var screen = new TerminalScreen(terminal);

        try {
            screen.startScreen();
            screen.setCursorPosition(null);
            TerminalSize size = screen.getTerminalSize();
            TextGraphics textGraphics = screen.newTextGraphics();
            while (true) {
                KeyStroke key = screen.pollInput();
                TerminalSize newSize = screen.doResizeIfNecessary();
                if (newSize != null) {
                    screen.clear();
                    borders(screen, newSize);
                    render(screen, newSize, textGraphics);
                }
                if (key != null) {
                    if (key.getKeyType() == KeyType.ArrowDown) {
                        screen.clear();
                        render(screen, screen.getTerminalSize(), textGraphics);
                    } else if (key.getKeyType() == KeyType.ArrowUp) {
                        break; // exit
                    }
                }
                render(screen, screen.getTerminalSize(), textGraphics);
                Thread.sleep(100);
            }
        } finally {
            screen.stopScreen();
        }

    }
    public static void render(Screen screen, TerminalSize size, TextGraphics tg) throws IOException {

        borders(screen, size);

        //deck  list list to content seperator
        drawVerticalLine(size, screen, 2, size.getColumns()/3, size.getRows()-3, Symbols.DOUBLE_LINE_VERTICAL);

        //top bar seperator

        drawHorizontalLine(size, screen, 0, 2, size.getColumns(), Symbols.DOUBLE_LINE_HORIZONTAL);

        tg.putString(2, 1, "Moxfield Tui");

        tg.putString(size.getColumns()-9, 1, "v1.0.0");

        //account info seperator
        drawHorizontalLine(size, screen, 0,size.getRows()-10,size.getColumns()/3, Symbols.DOUBLE_LINE_HORIZONTAL);

        screen.refresh();
    }

    }
