package networkjoy.Integration;

import com.sun.jna.Native;

import networkjoy.Libraries.VJoyInterface;

public class Vjoy {
    private VJoyInterface vJoyInterface = Native.load("vJoyInterface",VJoyInterface.class);
    int id;
    public Vjoy(int id) {
        this.id = id;
    }

    public int getStatus() {

        int devStat = vJoyInterface.GetVJDStatus(this.id);
        
        return devStat;
    }
    public boolean takeOwn(){
        return vJoyInterface.AcquireVJD(this.id);
    }
    public boolean setAxisValue(int val, int axId){
        return vJoyInterface.SetAxis(val, this.id, axId);
    }
    public void releaseOwn() {
        vJoyInterface.RelinquishVJD(this.id);
    }
    public void setButton(boolean val,byte bId){
        vJoyInterface.SetBtn(val, this.id, bId);
    }
    public void reset(){
        vJoyInterface.ResetVJD(this.id);
    }

}
