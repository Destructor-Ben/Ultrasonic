package mod.destructor_ben.ultrasonic.mixin;

import mod.destructor_ben.ultrasonic.MusicController;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicTracker.class)
public class MusicTrackerMixin
{
    @Inject(at = @At("RETURN"), method = "play")
    private void onPlay(MusicSound music, CallbackInfo info)
    {
        MusicController.displayCurrentMusicMessage();
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void onTick(CallbackInfo info)
    {
        MusicController.tick();
    }
}
