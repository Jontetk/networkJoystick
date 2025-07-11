package networkjoy.Main;



import networkjoy.Integration.Network;
import networkjoy.Integration.Vjoy;
import networkjoy.Integration.Network.RecievedJoyData;
public class Main {
    public static void main(String[] args) throws Exception{
        Vjoy vjoy = new Vjoy(1);
        if(!vjoy.takeOwn()) {
            System.out.println("Couldn't take ownership, bye");
            System.exit(1);
        }
        vjoy.reset();

        
        Network network = new Network(false, "192.168.100.220", 44801);
                System.out.println("GO");
        while (true) {
            RecievedJoyData recieved = network.recieveData();
            for (byte i=1;i<=recieved.buttonDatas.length;i++) {
                vjoy.setButton(recieved.buttonDatas[i-1], i);
            }
            for (int i=1;i<=recieved.axisDatas.length;i++) {
                vjoy.setAxis(recieved.axisDatas[i-1], Vjoy.HID_USAGES.get(i-1));
            }
            for (byte i=1;i<=recieved.povDatas.length;i++) {
                vjoy.setPov(35999/8*(recieved.povDatas[i-1]-1), i, true);
            }
        }



        //vjoy.releaseOwn();

    }
}