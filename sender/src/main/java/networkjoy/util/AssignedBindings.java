package networkjoy.util;

import java.io.Serializable;

public class AssignedBindings implements Serializable{
    private int[] buttons;
    private int[] axis;
    private int[] povs;

    public AssignedBindings() {

    }

    public void setButtons(int[] buttons) {
        this.buttons = buttons;
    }

    public void setAxis(int[] axis) {
        this.axis = axis;
    }

    public void setPovs(int[] povs) {
        this.povs = povs;
    }

    public int[] getButtons() {
        return buttons;
    }

    public int[] getAxis() {
        return axis;
    }

    public int[] getPovs() {
        return povs;
    }

}
