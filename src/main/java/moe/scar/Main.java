package moe.scar;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalFactory;
import com.googlecode.lanterna.*;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/>
public class Main {
    static Terminal terminal;

    static {
        try {
            terminal = new DefaultTerminalFactory().createTerminal();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Main() throws IOException {
    }

    public static void main(String[] args) throws IOException {
            TerminalSize size = terminal.getTerminalSize();
            borders(size);
            while (true) {
                KeyStroke key;
                key = terminal.readInput();
                if (key.getKeyType() == KeyType.ArrowDown) {
                    terminal.clearScreen();
                    render();
                }
            }
    }

    public static void render() throws IOException {
        TerminalSize size = terminal.getTerminalSize();
        borders(size);
//        terminal.setCursorPosition(1, ((size.getColumns() / 3)));
        for (int i = 0; i < size.getColumns(); i++){
            terminal.putCharacter('|');
            terminal.setCursorPosition(((size.getColumns() / 3)), i);
            terminal.flush();
        }
    }
    public static void borders(TerminalSize size) throws IOException {
        terminal.setCursorPosition(0,0);
        for (int i = 1; i < size.getColumns()-1; i++){
            terminal.setCursorPosition(i,0);
            terminal.putCharacter('-');
            terminal.setCursorPosition(i, size.getColumns()-1);
            terminal.putCharacter('-');
            terminal.flush();
        }
        for (int i = 0; i < size.getRows(); i++){
            terminal.setCursorPosition(0,i);
            terminal.putCharacter('|');
            terminal.setCursorPosition(size.getColumns()-1, i);
            terminal.putCharacter('|');
            terminal.flush();
        }
    }
}
