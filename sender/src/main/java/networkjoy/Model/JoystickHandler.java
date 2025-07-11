package networkjoy.Model;

import static org.lwjgl.glfw.GLFW.GLFW_HAT_CENTERED;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_LEFT_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_LEFT_UP;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_RIGHT_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_RIGHT_UP;
import static org.lwjgl.glfw.GLFW.GLFW_HAT_UP;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import networkjoy.Integration.Joystick;
import networkjoy.Integration.Joystick.ReturnedJoyData;

public class JoystickHandler {
    private int[] assignedButtons;
    private int[] assignedAxis;
    private int[] assignedPovs;
    private Joystick joystick;

    public JoystickHandler(int joyId, int[] assignedButtons, int[] assignedAxis, int[] assignedPovs) {
        this.assignedButtons = assignedButtons;
        this.assignedAxis = assignedAxis;
        this.assignedPovs = assignedPovs;
        this.joystick = new Joystick(joyId);

    }

    public void setAssignedButtons(int[] assignedButtons) {
        this.assignedButtons = assignedButtons;
    }

    public void setAssignedAxis(int[] assignedAxis) {
        this.assignedAxis = assignedAxis;
    }

    public void setAssignedPovs(int[] assignedPovs) {
        this.assignedPovs = assignedPovs;
    }

    public boolean bindButtons(int buttonId) {
        ByteBuffer buttons = joystick.readAllParts().buttons;
        for (int i = 0; i < buttons.limit(); i++) {
            if (buttons.get(i) == 1) {
                this.assignedButtons[buttonId - 1] = i;
                return true;
            }
        }
        return false;
    }

    public boolean bindAxis(int axisId) {
        FloatBuffer axes = joystick.readAllParts().axes;
        for (int i = 0; i < axes.limit(); i++) {
            Float currentAxis = axes.get(i);
            if (Math.abs(currentAxis) > 0.5) {
                this.assignedAxis[axisId - 1] = i;
                return true;
            }
        }
        return false;
    }

    public boolean bindPovs(int povId) {
        ByteBuffer povs = joystick.readAllParts().povs;
        for (int i = 0; i < povs.limit(); i++) {
            if (povs.get(i) == 1) {
                this.assignedPovs[povId - 1] = i;
                return true;
            }
        }
        return false;
    }

    public class ConvertedJoystickInputs {
        public int[] axisData;
        public boolean[] buttonData;
        public byte[] povData;

        public ConvertedJoystickInputs(int[] axisData, boolean[] buttonData, byte[] povData) {
            this.axisData = axisData;
            this.buttonData = buttonData;
            this.povData = povData;
        }

    }

    public ConvertedJoystickInputs convertJoysitckInputs() {
        ReturnedJoyData joyData = joystick.readAllParts();
        int[] axisData = new int[assignedAxis.length];
        boolean[] buttonData = new boolean[assignedButtons.length];
        byte[] povData = new byte[assignedPovs.length];

        for (int i = 0; i < joyData.buttons.limit(); i++) {
            for (int j = 0; j < assignedButtons.length; j++) {
                if (assignedButtons[j] == i) {
                    if (joyData.buttons.get(i) == 1) {
                        buttonData[j] = true;
                    } else {
                        buttonData[j] = false;
                    }
                }

            }
        }

        for (int i = 0; i < joyData.axes.limit(); i++) {
            for (int j = 0; j < assignedAxis.length; j++) {
                if (assignedAxis[j] == i) {
                    axisData[j] = (int) ((joyData.axes.get(i) + 1) * 0x8000) / 2 + 1;
                }

            }

        }
        for (int i = 0; i < joyData.povs.limit(); i++) {
            for (int j = 0; j < assignedPovs.length; j++) {
                if (assignedPovs[j] == i) {
                    Byte povPos = joyData.povs.get(i);
                    switch (povPos) {
                        case GLFW_HAT_CENTERED:
                            povData[j] = 0;
                            break;
                        case GLFW_HAT_UP:
                            povData[j] = 1;
                            break;
                        case GLFW_HAT_RIGHT_UP:
                            povData[j] = 2;
                            break;
                        case GLFW_HAT_RIGHT:
                            povData[j] = 3;
                            break;
                        case GLFW_HAT_RIGHT_DOWN:
                            povData[j] = 4;
                            break;
                        case GLFW_HAT_DOWN:
                            povData[j] = 5;
                            break;
                        case GLFW_HAT_LEFT_DOWN:
                            povData[j] = 6;
                            break;
                        case GLFW_HAT_LEFT:
                            povData[j] = 7;
                            break;
                        case GLFW_HAT_LEFT_UP:
                            povData[j] = 8;
                            break;

                        default:
                            break;
                    }

                }

            }
        }
        return new ConvertedJoystickInputs(axisData, buttonData, povData);

    }

}
