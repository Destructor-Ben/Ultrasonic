package mod.destructor_ben.ultrasonic;

import mod.destructor_ben.ultrasonic.mixin.MusicTrackerAccessor;
import mod.destructor_ben.ultrasonic.mixin.WeightedSoundSetAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

// Manages in game music
// TODO: make this make stopping music fade it out
// TODO: allow certain albums and tracks to be disabled - probably do this by making a MusicSoundInstance (based on positioned sound instance) that is fancy, or modify WeightedSoundSet
// TODO: add keybinds to control the music
// TODO: stop music when records are playing
public class MusicController
{
    public static void displayCurrentMusicMessage()
    {
        // Config option
        if (!UltrasonicConfig.displayMusicMessage)
            return;

        // Only do it if we are in game
        if (MinecraftClient.getInstance().world == null)
            return;

        // Get the song being played
        var accessor = (MusicTrackerAccessor)(MinecraftClient.getInstance().getMusicTracker());
        var currentMusic = accessor.getCurrent();
        if (currentMusic == null)
            return;

        // Yay, get the id of the INSTANCE of the sound, not the sound group
        var id = MusicDatabase.soundIDtoID(currentMusic.getSound().getIdentifier());

        // Get the track
        var track = MusicDatabase.getInstance().getTrack(id);

        // Display the message with the same notification system as the Jukebox
        var message = Text.literal(track.getArtistName().getString() + " - " + track.getName().getString());
        MinecraftClient.getInstance().inGameHud.setRecordPlayingOverlay(message);
    }

    public static void tick()
    {
        var client = MinecraftClient.getInstance();
        var accessor = (MusicTrackerAccessor)(client.getMusicTracker());

        // Set the wait time to 0 if we always want music
        if (UltrasonicConfig.alwaysPlayMusic && accessor.getCurrent() == null)
            accessor.setTimeUntilNextSong(0);
    }

    public static boolean shouldPlaySound(Identifier id)
    {
        // TODO: make this check if a sound should be disabled
        var path = id.getPath();
        return !path.equals("music/menu/beginning_2") && !path.equals("music/game/yakusoku");
    }

    // TODO: test
    public static Sound getSoundFromSoundSet(WeightedSoundSet set, Random random)
    {
        // Vanilla behaviour
        if (!UltrasonicConfig.modifyAvailableMusic)
            return set.getSound(random);

        // Custom behaviour (copy-pasted)
        var accessor = (WeightedSoundSetAccessor)set;
        var sounds = accessor.getSounds();

        int totalWeight = set.getWeight();
        if (sounds.isEmpty() || totalWeight == 0)
            return SoundManager.MISSING_SOUND;

        int weight = random.nextInt(totalWeight);

        for (var soundContainer : sounds)
        {
            weight -= soundContainer.getWeight();

            if (weight >= 0)
                continue;

            // Added to make disabled music not play
            // TODO: what if this is the last sound in a list and stops a sound being played altogether? because its disabled and last
            // Ideally we would remove the weight from it so we wouldn't reach this spot in the first place
            // But we can't do that because the sound is random.
            var sound = soundContainer.getSound(random);
            if (!shouldPlaySound(sound.getIdentifier()))
                continue;

            return sound;
        }

        // This was modified in case we had an empty sound
        return SoundManager.INTENTIONALLY_EMPTY_SOUND;
    }

    // Stop current song and leave the delay the same
    public static void stop()
    {
        MinecraftClient.getInstance().getMusicTracker().stop();
    }

    // Stop the current song and set the delay to 0
    public static void skip()
    {
        stop();

        var client = MinecraftClient.getInstance();
        var tracker = client.getMusicTracker();
        var accessor = (MusicTrackerAccessor)tracker;
        accessor.setTimeUntilNextSong(0);
    }
}
