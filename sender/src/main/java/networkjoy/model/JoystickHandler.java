package networkjoy.model;

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
import java.util.Map;

import networkjoy.integration.Joystick;
import networkjoy.util.AssignedBindings;
import networkjoy.util.ConvertedJoystickInputs;
import networkjoy.util.ReturnedJoyData;

public class JoystickHandler {
    private AssignedBindings assignedBindings;
    private Joystick joystick;
    private boolean axisCheck;
    private float[] axisValues;

    public JoystickHandler(int joyId, AssignedBindings assignedBindings) {
        this.assignedBindings = assignedBindings;
        this.joystick = new Joystick(joyId);

    }

    public static Map<String, Integer> getAllJoysticks() {
        return Joystick.getAllJoy();

    }

    public void setAssignedBindings(AssignedBindings assignedBindings) {
        this.assignedBindings = assignedBindings;
    }

    public void setAxisCheck(boolean axisCheck) {
        this.axisCheck = axisCheck;
    }

    public boolean bindButtons(int buttonId) {
        ByteBuffer buttons = joystick.readAllParts().getButtons();
        for (int i = 0; i < buttons.limit(); i++) {
            if (buttons.get(i) == 1) {
                this.assignedBindings.getButtons()[buttonId - 1] = i;
                return true;
            }
        }
        return false;
    }

    public boolean bindAxis(int axisId) {
        FloatBuffer axes = joystick.readAllParts().getAxes();
        if (axisCheck) {
            axisValues = new float[axes.limit()];
        }
        for (int i = 0; i < axes.limit(); i++) {
            float currentAxis = axes.get(i);

            if (axisCheck) {
                axisValues[i] = currentAxis;
            } else {
                if (Math.abs(currentAxis - axisValues[i]) >= 0.3) {
                    this.assignedBindings.getAxis()[axisId - 1] = i;
                    return true;
                }
            }
        }
        axisCheck = false;
        return false;
    }

    public boolean bindPovs(int povId) {
        ByteBuffer povs = joystick.readAllParts().getPovs();
        for (int i = 0; i < povs.limit(); i++) {
            if (povs.get(i) == 1) {
                this.assignedBindings.getPovs()[povId - 1] = i;
                return true;
            }
        }
        return false;
    }

    public ConvertedJoystickInputs convertJoysitckInputs() {
        ReturnedJoyData joyData = joystick.readAllParts();
        int[] axisData = new int[assignedBindings.getAxis().length];
        boolean[] buttonData = new boolean[assignedBindings.getButtons().length];
        byte[] povData = new byte[assignedBindings.getPovs().length];

        for (int i = 0; i < joyData.getButtons().limit(); i++) {
            for (int j = 0; j < assignedBindings.getButtons().length; j++) {
                if (assignedBindings.getButtons()[j] == i) {
                    if (joyData.getButtons().get(i) == 1) {
                        buttonData[j] = true;
                    } else {
                        buttonData[j] = false;
                    }
                }

            }
        }

        for (int i = 0; i < joyData.getAxes().limit(); i++) {
            for (int j = 0; j < assignedBindings.getAxis().length; j++) {
                if (assignedBindings.getAxis()[j] == i) {
                    axisData[j] = (int) (((joyData.getAxes().get(i) + 1) / 2) * (0x8000 - 1)) + 1;
                }

            }

        }
        for (int i = 0; i < joyData.getPovs().limit(); i++) {
            for (int j = 0; j < assignedBindings.getPovs().length; j++) {
                if (assignedBindings.getPovs()[j] == i) {
                    Byte povPos = joyData.getPovs().get(i);
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
