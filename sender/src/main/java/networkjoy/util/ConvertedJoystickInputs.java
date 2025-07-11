package networkjoy.util;

public class ConvertedJoystickInputs {
    private int[] axisData;
    private boolean[] buttonData;
    private byte[] povData;

    public ConvertedJoystickInputs(int[] axisData, boolean[] buttonData, byte[] povData) {
        this.axisData = axisData;
        this.buttonData = buttonData;
        this.povData = povData;
    }

    public int[] getAxisData() {
        return axisData;
    }

    public boolean[] getButtonData() {
        return buttonData;
    }

    public byte[] getPovData() {
        return povData;
    }

}
