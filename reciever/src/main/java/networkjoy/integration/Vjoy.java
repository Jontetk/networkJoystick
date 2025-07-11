package networkjoy.integration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sun.jna.Native;

import networkjoy.libraries.VJoyInterface;

public class Vjoy {
    private VJoyInterface vJoyInterface = Native.load("vJoyInterface", VJoyInterface.class);
    int id;
    public static final int HID_USAGE_X = 0x30;
    public static final int HID_USAGE_Y = 0x31;
    public static final int HID_USAGE_Z = 0x32;
    public static final int HID_USAGE_RX = 0x33;
    public static final int HID_USAGE_RY = 0x34;
    public static final int HID_USAGE_RZ = 0x35;
    public static final int HID_USAGE_SL0 = 0x36;
    public static final int HID_USAGE_SL1 = 0x37;
    public static final int HID_USAGE_WHL = 0x38;
    public static final int HID_USAGE_POV = 0x39;
    public static final List<Integer> HID_USAGES = Collections.unmodifiableList(Arrays.asList(HID_USAGE_X,
            HID_USAGE_Y,
            HID_USAGE_Z,
            HID_USAGE_RX,
            HID_USAGE_RY,
            HID_USAGE_RZ,
            HID_USAGE_SL0,
            HID_USAGE_SL1,
            HID_USAGE_WHL,
            HID_USAGE_POV));

    public Vjoy(int id) {
        this.id = id;
    }

    public int getStatus() {

        int devStat = vJoyInterface.GetVJDStatus(this.id);

        return devStat;
    }

    public boolean takeOwn() {
        return vJoyInterface.AcquireVJD(this.id);
    }

    /**
     * Sets an axis
     * 
     * @param val  The value of the axis between 0x1 and 0x8000
     * @param axId One of the HID usage variables
     * @return
     */
    public boolean setAxis(int val, int axId) {
        return vJoyInterface.SetAxis(val, this.id, axId);
    }

    public void releaseOwn() {
        vJoyInterface.RelinquishVJD(this.id);
    }

    public void setButton(boolean val, byte bId) {
        vJoyInterface.SetBtn(val, this.id, bId);
    }

    public void reset() {
        vJoyInterface.ResetVJD(this.id);
    }

    /**
     * Sets a pov
     * 
     * @param val   The value of the pov
     *              if not cont:
     *              0: North (or Forwards)
     *              1: East (or Right)
     *              2: South (or backwards)
     *              3: West (or left)
     *              -1: Neutral (Nothing pressed)
     *              if cont:
     *              range: -1 to 35999.
     *              It is measured in units of one-hundredth a degree. -1 means
     *              Neutral
     * @param povId the pov switch
     * @param cont  If the pov is continous or not
     */
    public void setPov(int val, byte povId, boolean cont) {
        if (cont) {
            vJoyInterface.SetContPov(val, id, povId);
        } else {
            vJoyInterface.SetDiscPov(val, id, povId);
        }
    }

}
