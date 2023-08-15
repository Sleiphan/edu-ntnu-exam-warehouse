package registry;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ItemRegistry reg = new ItemRegistry();
        reg.fillWithTestData();

        Scanner in = new Scanner(System.in);
        String ans;
        while (true) {
            System.out.println("Choose which interface you want to use:");
            System.out.println("1: Console");
            System.out.println("2: Graphical");
            ans = in.nextLine();
            if (ans.equals("1") || ans.equals("2"))
                break;
            System.out.println("Invalid input: " + ans);
        }


        if (ans.equals("1")) {
            ConsoleInterface i = new ConsoleInterface(reg);
            i.run();
        } else {
            AWTInterface i = new AWTInterface(reg);
            i.run();
        }
    }
}
