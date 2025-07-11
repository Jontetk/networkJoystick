package networkjoy.util;

public class RecievedJoyData {

    private boolean[] buttonDatas;
    private int[] axisDatas;
    private byte[] povDatas;

    public RecievedJoyData(int buttonInputs, int axisInputs, int povInputs) {
        this.buttonDatas = new boolean[buttonInputs];
        this.axisDatas = new int[axisInputs];
        this.povDatas = new byte[povInputs];
    }

    public boolean[] getButtonDatas() {
        return buttonDatas;
    }

    public int[] getAxisDatas() {
        return axisDatas;
    }

    public byte[] getPovDatas() {
        return povDatas;
    }


}
