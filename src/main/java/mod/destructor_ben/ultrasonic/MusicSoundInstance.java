package mod.destructor_ben.ultrasonic;

import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public class MusicSoundInstance extends PositionedSoundInstance
{
    public MusicSoundInstance(Identifier id)
    {
        super(
            id,
            SoundCategory.MUSIC,
            1.0F,
            1.0F,
            SoundInstance.createRandom(),
            false,
            0,
            SoundInstance.AttenuationType.NONE,
            0.0,
            0.0,
            0.0,
            true
        );
    }

    // This is special because it excludes the sounds that are disabled by the user
    // This is unfortunately copy-pasted
    @Override
    public WeightedSoundSet getSoundSet(SoundManager soundManager)
    {
        if (this.id.equals(SoundManager.INTENTIONALLY_EMPTY_ID))
        {
            this.sound = SoundManager.INTENTIONALLY_EMPTY_SOUND;
            return SoundManager.INTENTIONALLY_EMPTY_SOUND_SET;
        }
        else
        {
            WeightedSoundSet weightedSoundSet = soundManager.get(this.id);

            if (weightedSoundSet == null)
            {
                this.sound = SoundManager.MISSING_SOUND;
            }
            else
            {
                // This is modified
                this.sound = MusicController.getSoundFromSoundSet(weightedSoundSet, this.random);
            }

            return weightedSoundSet;
        }
    }
}
