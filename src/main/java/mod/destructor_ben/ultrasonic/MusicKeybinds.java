package mod.destructor_ben.ultrasonic;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MusicKeybinds
{
    public static KeyBinding stopKeybind;
    public static KeyBinding skipKeybind;

    public static void initialize()
    {
        // Note that this is indistinguishable from skip when music always plays
        stopKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.ultrasonic.stop",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_BRACKET,
            "category.ultrasonic.music"
        ));

        skipKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.ultrasonic.skip",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_BRACKET,
            "category.ultrasonic.music"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            while (stopKeybind.wasPressed())
            {
                MusicController.stop();
            }

            while (skipKeybind.wasPressed())
            {
                MusicController.skip();
            }
        });
    }
}
