package mod.destructor_ben.ultrasonic;

import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

/*
Albums contain tracks.
Tracks have a reference to the music type they are assigned to.
Albums and tracks can be enabled or disabled.
Currently, tracks can't have their music type changed.
 */
// TODO: make this a registry and loaded from json in assets
// Possibly look at JukeboxSong
public class MusicDatabase
{
    private static MusicDatabase instance;
    private static final String FOLDER = "sounds/music";
    private static final String EXTENSION = ".ogg";

    public List<Album> albums = new ArrayList<>();

    public static void initialize(ResourceManager resourceManager)
    {
        Ultrasonic.LOGGER.info("Initializing Music Database...");

        instance = new MusicDatabase();
        instance.addInitialData();
        instance.findUnknownTracks(resourceManager);

        Ultrasonic.LOGGER.info("Initialized Music Database");
    }

    public static MusicDatabase getInstance()
    {
        return instance;
    }

    // Takes the resource path in ("sounds/music/" prefix)
    public static Identifier musicPathToID(Identifier path)
    {
        var id = path.getPath();
        // Chop off start and end
        // Add 1 for the leading slash that is illegal for some reason
        id = id.substring(FOLDER.length() + 1, id.length() - EXTENSION.length());
        return Identifier.of(path.getNamespace(), id);
    }

    // Takes the SoundEvent path in ("music/" prefix)
    public static Identifier soundIDtoID(Identifier id)
    {
        var path = id.getPath();

        // Slice the "music/" off the beginning
        var sliceString = "music/";
        if (path.startsWith(sliceString))
            path = path.substring(sliceString.length());

        return id.withPath(path);
    }

    public void addAlbum(Album album)
    {
        Ultrasonic.LOGGER.info("Adding album: {}", album.getName().getString());
        albums.add(album);
    }

    public Album getAlbum(Identifier id)
    {
        for (var album : albums)
        {
            if (album.id.equals(id))
                return album;
        }

        return null;
    }

    public Track getTrack(Identifier id)
    {
        for (var album : albums)
        {
            for (var track : album.tracks)
            {
                if (track.id.equals(id))
                    return track;
            }
        }

        return null;
    }

    // Note that not all songs in the actual albums are here, since not all are used
    private void addInitialData()
    {
        // Minecraft - Volume Alpha
        var volumeAlpha = new Album(Identifier.ofVanilla("volume_alpha"))
            .addTrack(Identifier.ofVanilla("game/key"))
            .addTrack(Identifier.ofVanilla("game/subwoofer_lullaby"))
            .addTrack(Identifier.ofVanilla("game/living_mice"))
            .addTrack(Identifier.ofVanilla("game/haggstrom"))
            .addTrack(Identifier.ofVanilla("game/minecraft"))
            .addTrack(Identifier.ofVanilla("game/oxygene"))
            .addTrack(Identifier.ofVanilla("game/mice_on_venus"))
            .addTrack(Identifier.ofVanilla("game/dry_hands"))
            .addTrack(Identifier.ofVanilla("game/wet_hands"))
            .addTrack(Identifier.ofVanilla("game/clark"))
            .addTrack(Identifier.ofVanilla("game/sweden"))
            .addTrack(Identifier.ofVanilla("game/danny"));

        addAlbum(volumeAlpha);

        // Minecraft - Volume Beta
        var volumeBeta = new Album(Identifier.ofVanilla("volume_beta"))
            .addTrack(Identifier.ofVanilla("game/end/alpha"))
            .addTrack(Identifier.ofVanilla("game/nether/dead_voxel"))
            .addTrack(Identifier.ofVanilla("game/creative/blind_spots"))
            .addTrack(Identifier.ofVanilla("menu/moog_city_2"))
            .addTrack(Identifier.ofVanilla("game/nether/concrete_halls"))
            .addTrack(Identifier.ofVanilla("game/creative/biome_fest"))
            .addTrack(Identifier.ofVanilla("menu/mutation"))
            .addTrack(Identifier.ofVanilla("game/creative/haunt_muskie"))
            .addTrack(Identifier.ofVanilla("game/nether/warmth"))
            .addTrack(Identifier.ofVanilla("menu/floating_trees"))
            .addTrack(Identifier.ofVanilla("game/creative/aria_math"))
            .addTrack(Identifier.ofVanilla("game/nether/ballad_of_the_cats"))
            .addTrack(Identifier.ofVanilla("game/creative/taswell"))
            .addTrack(Identifier.ofVanilla("menu/beginning_2"))
            .addTrack(Identifier.ofVanilla("game/creative/dreiton"))
            .addTrack(Identifier.ofVanilla("game/end/the_end"))
            // Technically, the boss music isn't meant to be here, but I'm not making an album dedicated to it
            .addTrack(Identifier.ofVanilla("game/end/boss"));

        addAlbum(volumeBeta);

        // Underwater singles (Technically 3rd volume)
        var underwaterSingles = new Album(Identifier.ofVanilla("underwater_singles"))
            .addTrack(Identifier.ofVanilla("game/water/axolotl"))
            .addTrack(Identifier.ofVanilla("game/water/dragon_fish"))
            .addTrack(Identifier.ofVanilla("game/water/shuniji"));

        addAlbum(underwaterSingles);

        // Minecraft: Nether Update
        var netherUpdate = new Album(Identifier.ofVanilla("nether_update"))
            .addTrack(Identifier.ofVanilla("game/nether/crimson_forest/chrysopoeia"))
            .addTrack(Identifier.ofVanilla("game/nether/nether_wastes/rubedo"))
            .addTrack(Identifier.ofVanilla("game/nether/soulsand_valley/so_below"));

        addAlbum(netherUpdate);

        // Minecraft: Caves & Cliffs
        var cavesAndCliffs = new Album(Identifier.ofVanilla("caves_and_cliffs"))
            .addTrack(Identifier.ofVanilla("game/stand_tall"))
            .addTrack(Identifier.ofVanilla("game/left_to_bloom"))
            .addTrack(Identifier.ofVanilla("game/ancestry"))
            .addTrack(Identifier.ofVanilla("game/wending"))
            .addTrack(Identifier.ofVanilla("game/infinite_amethyst"))
            .addTrack(Identifier.ofVanilla("game/one_more_day"))
            .addTrack(Identifier.ofVanilla("game/floating_dream"))
            .addTrack(Identifier.ofVanilla("game/comforting_memories"))
            .addTrack(Identifier.ofVanilla("game/an_ordinary_day"));

        addAlbum(cavesAndCliffs);

        // Minecraft: The Wild Update
        var theWildUpdate = new Album(Identifier.ofVanilla("the_wild_update"))
            .addTrack(Identifier.ofVanilla("game/swamp/firebugs"))
            .addTrack(Identifier.ofVanilla("game/swamp/aerie"))
            .addTrack(Identifier.ofVanilla("game/swamp/labyrinthine"));

        addAlbum(theWildUpdate);

        // Minecraft: Trails & Tales
        var trailsAndTales = new Album(Identifier.ofVanilla("trails_and_tales"))
            .addTrack(Identifier.ofVanilla("game/echo_in_the_wind"))
            .addTrack(Identifier.ofVanilla("game/a_familiar_room"))
            .addTrack(Identifier.ofVanilla("game/bromeliad"))
            .addTrack(Identifier.ofVanilla("game/crescent_dunes"));

        addAlbum(trailsAndTales);

        // Minecraft: Tricky Trials
        var trickyTrials = new Album(Identifier.ofVanilla("tricky_trials"))
            .addTrack(Identifier.ofVanilla("game/featherfall"))
            .addTrack(Identifier.ofVanilla("game/watcher"))
            .addTrack(Identifier.ofVanilla("game/puzzlebox"))
            .addTrack(Identifier.ofVanilla("game/komorebi"))
            .addTrack(Identifier.ofVanilla("game/pokopoko"))
            .addTrack(Identifier.ofVanilla("game/yakusoku"))
            .addTrack(Identifier.ofVanilla("game/deeper"))
            .addTrack(Identifier.ofVanilla("game/eld_unknown"))
            .addTrack(Identifier.ofVanilla("game/endless"));

        addAlbum(trickyTrials);
    }

    // Since not all song files will have track data, especially from other mods, we need to add them to their own album
    private void findUnknownTracks(ResourceManager resourceManager)
    {
        var finder = new ResourceFinder(FOLDER, EXTENSION);
        var music = finder.findAllResources(resourceManager);
        var unknownTracks = new ArrayList<Track>();

        // Find the tracks not assigned to anything
        for (var path : music.keySet())
        {
            var id = musicPathToID(path);

            if (getTrack(id) != null)
                continue;

            Ultrasonic.LOGGER.warn("Unknown track: {}", id);
            var track = new Track(id);
            track.isUnknown = true;
            unknownTracks.add(track);
        }

        // Add them to an album
        var album = new Album(Identifier.ofVanilla("unknown_tracks"));
        album.tracks.addAll(unknownTracks);
        addAlbum(album);
    }
}
