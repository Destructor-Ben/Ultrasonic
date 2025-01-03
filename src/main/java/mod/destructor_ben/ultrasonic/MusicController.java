package mod.destructor_ben.ultrasonic;

import mod.destructor_ben.ultrasonic.config.UltrasonicConfig;
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
// TODO: make stopping music fade it out
// TODO: stop music when records are playing
// TODO: stop the same song playing in a row
// TODO: maybe allow playing a song whenever the user wants + queuing songs
// TODO: possibly add a small delay when always playing music/skipping to make it sound nicer
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
        if (track == null)
            return;

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
        id = MusicDatabase.soundIDtoID(id);
        var track = MusicDatabase.getInstance().getTrack(id);
        return !UltrasonicConfig.ignoredTracks.contains(track.id) && !UltrasonicConfig.ignoredAlbums.contains(track.album.id);
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

    // TODO: spamming stop and skip can leave multiple songs playing, also theoretically it will make the time until next song massively increase
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
