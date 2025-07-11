package networkjoy.util;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class ReturnedJoyData {
    private FloatBuffer axes;
    private ByteBuffer buttons;
    private ByteBuffer povs;

    public ReturnedJoyData(FloatBuffer axes, ByteBuffer buttons, ByteBuffer povs) {
        this.axes = axes;
        this.buttons = buttons;
        this.povs = povs;
    }

    public FloatBuffer getAxes() {
        return axes;
    }

    public ByteBuffer getButtons() {
        return buttons;
    }

    public ByteBuffer getPovs() {
        return povs;
    }

}