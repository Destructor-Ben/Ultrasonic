package mod.destructor_ben.ultrasonic.mixin;

import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MusicTracker.class)
public interface MusicTrackerAccessor
{
    @Accessor("current")
    SoundInstance getCurrent();

    @Accessor("timeUntilNextSong")
    void setTimeUntilNextSong(int timeUntilNextSong);
}
