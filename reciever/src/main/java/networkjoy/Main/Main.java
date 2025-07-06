package networkjoy.Main;

import java.util.Scanner;


import networkjoy.Integration.Vjoy;
public class Main {
    public static void main(String[] args) throws Exception{
        Vjoy vjoy = new Vjoy(1);
        if(!vjoy.takeOwn()) {
            System.out.println("Couldn't take ownership, bye");
            System.exit(1);
        }
        vjoy.reset();
        System.out.println("GO");
        Thread.sleep(2500);

        System.out.println("X 0");
        vjoy.setAxis(1, Vjoy.HID_USAGE_X);
        Thread.sleep(2500);
        System.out.println("X half");
        vjoy.setAxis(0x8000/2, Vjoy.HID_USAGE_X);
        Thread.sleep(2500);
        System.out.println("X full");
        vjoy.setAxis(0x8000, Vjoy.HID_USAGE_X);
        Thread.sleep(2500);
        System.out.println("RESET!");
        vjoy.reset();
        Thread.sleep(2500);
        System.out.println("1 press");
        vjoy.setButton(true, (byte) 1);
        Thread.sleep(2500);
        System.out.println("1 release");
        vjoy.setButton(false, (byte) 1);
        Thread.sleep(2500);
        System.out.println("RESET!");
        vjoy.reset();
        Thread.sleep(2500);
        System.out.println("POV 0");
        vjoy.setPov(0, (byte)1, true);
        Thread.sleep(2500);
        System.out.println("POV Change");
        vjoy.setPov(35999/8*1, (byte)1, true);
        Thread.sleep(2500);
        System.out.println("POV Change");
        vjoy.setPov(35999/8*2, (byte)1, true);
        Thread.sleep(2500);
        System.out.println("POV Change");
        vjoy.setPov(35999/8*3, (byte)1, true);
        Thread.sleep(2500);
        System.out.println("POV Change");
        vjoy.setPov(35999/8*4, (byte)1, true);
        Thread.sleep(2500);
        System.out.println("POV Change");
        vjoy.setPov(35999/8*5, (byte)1, true);
        Thread.sleep(2500);
        System.out.println("POV Change");
        vjoy.setPov(35999/8*6, (byte)1, true);
        Thread.sleep(2500);
        System.out.println("POV Change");
        vjoy.setPov(35999/8*7, (byte)1, true);
        Thread.sleep(2500);

        System.out.println("POV release");
        vjoy.setPov(-1, (byte)1, true);
        Thread.sleep(2500);
        System.out.println("RESET BYE");
        vjoy.reset();



        vjoy.releaseOwn();

    }
}