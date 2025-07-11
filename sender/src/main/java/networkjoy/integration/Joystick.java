package networkjoy.integration;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Joystick {

    private int selectedJoy;

    public static void joystickInit() {
        glfwInit();
    }

    public static final Set<Integer> JOYSTICKS = Set.of(GLFW_JOYSTICK_1, GLFW_JOYSTICK_2, GLFW_JOYSTICK_3,
            GLFW_JOYSTICK_4, GLFW_JOYSTICK_5, GLFW_JOYSTICK_6, GLFW_JOYSTICK_7, GLFW_JOYSTICK_8, GLFW_JOYSTICK_9,
            GLFW_JOYSTICK_10, GLFW_JOYSTICK_11, GLFW_JOYSTICK_12, GLFW_JOYSTICK_13, GLFW_JOYSTICK_14, GLFW_JOYSTICK_15,
            GLFW_JOYSTICK_16);

    public static Map<String, Integer> getAllJoy() {
        Map<String, Integer> availableJoysticks = new HashMap<String, Integer>();
        for (int joyId : JOYSTICKS) {
            boolean prenet = glfwJoystickPresent(joyId);
            if (prenet) {
                availableJoysticks.put(glfwGetJoystickName(joyId), joyId);
            }
        }
        return availableJoysticks;
    }

    public Joystick(int selectedJoy) {
        this.selectedJoy = selectedJoy;
    }

    public class ReturnedJoyData {
        public FloatBuffer axes;
        public ByteBuffer buttons;
        public ByteBuffer povs;

        public ReturnedJoyData(FloatBuffer axes, ByteBuffer buttons, ByteBuffer povs) {
            this.axes = axes;
            this.buttons = buttons;
            this.povs = povs;
        }

    }

    public ReturnedJoyData readAllParts() {
        return (new ReturnedJoyData(
                glfwGetJoystickAxes(this.selectedJoy),
                glfwGetJoystickButtons(this.selectedJoy),
                glfwGetJoystickHats(this.selectedJoy)));

    }

}
