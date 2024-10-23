package mod.destructor_ben.ultrasonic.mixin;

import mod.destructor_ben.ultrasonic.MusicSoundInstance;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PositionedSoundInstance.class)
public class PositionedSoundInstanceMixin {
    @Inject(at = @At("RETURN"), method = "music", cancellable = true)
    private static void onMusic(SoundEvent sound, CallbackInfoReturnable<PositionedSoundInstance> info) {
        // Use our new sound instance for music
        var originalValue = info.getReturnValue();
        var instance = new MusicSoundInstance(originalValue.getId());
        info.setReturnValue(instance);
    }
}
