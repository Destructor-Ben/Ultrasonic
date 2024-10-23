package mod.destructor_ben.ultrasonic.mixin;

import mod.destructor_ben.ultrasonic.UltrasonicConfig;
import net.minecraft.client.gui.screen.option.SoundOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundOptionsScreen.class)
public class SoundOptionsScreenMixin
{
    @Inject(at = @At("RETURN"), method = "addOptions")
    private void onAddOptions(CallbackInfo info)
    {
        UltrasonicConfig.modifyMusicScreen();
    }
}
