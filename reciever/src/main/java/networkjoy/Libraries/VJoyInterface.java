package networkjoy.Libraries;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface VJoyInterface extends StdCallLibrary{
 short GetvJoyVersion();
    boolean vJoyEnabled();
    Pointer GetvJoyProductString();
    Pointer GetvJoyManufacturerString();
    Pointer GetvJoySerialNumberString();

    int GetVJDButtonNumber(int rID);
    int GetVJDDiscPovNumber(int rID);
    int GetVJDContPovNumber(int rID);
    boolean GetVJDAxisExist(int rID, int Axis);
    boolean GetVJDAxisMax(int rID, int Axis, LongByReference Max);
    boolean GetVJDAxisMin(int rID, int Axis, LongByReference Min);

    boolean AcquireVJD(int rID);
    void RelinquishVJD(int rID);
    boolean UpdateVJD(int rID, Pointer pData); // You may want to create a structure instead of Pointer

    int GetVJDStatus(int rID); // If you define an enum, this can be mapped more cleanly

    boolean ResetVJD(int rID);
    void ResetAll();
    boolean ResetButtons(int rID);
    boolean ResetPovs(int rID);

    boolean SetAxis(int value, int rID, int Axis);
    boolean SetBtn(boolean value, int rID, byte nBtn);
    boolean SetDiscPov(int value, int rID, byte nPov);
    boolean SetContPov(int value, int rID, byte nPov);
}
