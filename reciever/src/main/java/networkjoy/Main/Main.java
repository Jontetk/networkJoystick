package networkjoy.Main;

import java.util.Scanner;


import networkjoy.Integration.Vjoy;
public class Main {
    public static void main(String[] args) {
        Vjoy vjoy = new Vjoy(1);
        vjoy.reset();
        System.out.println();
        System.out.println(vjoy.getStatus());
        System.out.println(vjoy.takeOwn());
        System.out.println(vjoy.getStatus());
        String inputVal = "";
        String axisVal = "";
        Scanner scanner = new Scanner(System.in);
        while (!inputVal.equals("STOP")) {
            System.out.print("AXIS");
            inputVal = scanner.nextLine();
            System.out.println("");
            System.out.print("VALUE");
            axisVal = scanner.nextLine();
            if (!inputVal.equals("STOP"))
                vjoy.setAxis(Integer.valueOf(axisVal), Integer.valueOf(inputVal));
        }
        scanner.close();
        vjoy.releaseOwn();

    }
}