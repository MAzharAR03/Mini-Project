package myparser;

import java.util.ArrayList;
import java.util.List;

public class TACGenerator {
    private static List<String> code = new ArrayList<>();
    private static int tempCount = 0;
    private static int labelCount = 0;

    public static String newTemp() {
        return "t" + (++tempCount);
    }

    public static String newLabel() {
        return "L" + (++labelCount);
    }

    public static void emit(String instruction) {
        code.add(instruction);
    }

    public static void printCode() {
        System.out.println("\nParsing Input\n");
        for (String line : code) {
            System.out.println(line);
        }
    }
}
