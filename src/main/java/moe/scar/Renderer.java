package moe.scar;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public class Renderer {



//todo flesh out renderer code to be usable for other applications
    public static void drawVerticalDivider(TerminalSize size, Screen screen, int x, int rows) {
        drawVerticalLine(size, screen, 0, x, rows, Symbols.DOUBLE_LINE_VERTICAL);

//        screen.setCharacter(x, 0, new TextCharacter(Symbols.DOUBLE_LINE_T_DOWN));
//        screen.setCharacter(x, rows - 1, new TextCharacter(Symbols.DOUBLE_LINE_T_UP));
    }

    public static void drawVerticalLine(TerminalSize size, Screen screen, int yStart, int x, int length, char symbol) {
        for (int y = yStart; y < yStart + length; y++) {
            screen.setCharacter(x, y, new TextCharacter(symbol));
            if (y == 0){
                screen.setCharacter(x, y, new TextCharacter(Symbols.DOUBLE_LINE_T_DOWN));
            }
            if (y == size.getRows()- 1){
                screen.setCharacter(x, y, new TextCharacter(Symbols.DOUBLE_LINE_T_UP));
            }
        }
    }

    public static void drawHorizontalLine(TerminalSize size, Screen screen, int xStart, int y, int length, char symbol) {
        for (int x = xStart; x < xStart + length; x++) {
            screen.setCharacter(x, y, new TextCharacter(symbol));

            if (x == 0){
                screen.setCharacter(x, y, new TextCharacter(Symbols.DOUBLE_LINE_T_RIGHT));
            }
            if (x == size.getColumns()- 1){
                screen.setCharacter(x, y, new TextCharacter(Symbols.DOUBLE_LINE_T_LEFT));
            }
        }

    }

    public static void borders(Screen screen, TerminalSize size) throws IOException {

        for (int i = 0; i <= size.getColumns(); i++) {
            screen.setCharacter(i, 0, new TextCharacter(Symbols.DOUBLE_LINE_HORIZONTAL));
            screen.setCharacter(i, size.getRows()-1, new TextCharacter(Symbols.DOUBLE_LINE_HORIZONTAL));

        }

        for (int i = 0; i < size.getRows(); i++) {
            screen.setCharacter(0, i, new TextCharacter(Symbols.DOUBLE_LINE_VERTICAL));
            screen.setCharacter(size.getColumns() - 1, i, new TextCharacter(Symbols.DOUBLE_LINE_VERTICAL));
        }

        screen.setCharacter(0,0, new TextCharacter(Symbols.DOUBLE_LINE_TOP_LEFT_CORNER));
        screen.setCharacter(0,size.getRows() - 1, new TextCharacter(Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER));
        screen.setCharacter(size.getColumns()-1,0, new TextCharacter(Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER));
        screen.setCharacter(size.getColumns()-1,size.getRows() - 1, new TextCharacter(Symbols.DOUBLE_LINE_BOTTOM_RIGHT_CORNER));

        screen.refresh();
    }


}
