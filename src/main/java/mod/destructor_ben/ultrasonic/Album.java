package mod.destructor_ben.ultrasonic;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;

public class Album
{
    public Identifier id;
    // Do not add tracks directly, they won't have their album set
    public List<Track> tracks;

    public Album(Identifier id)
    {
        this.id = id;
        this.tracks = new ArrayList<>();
    }

    public Album addTrack(Track track)
    {
        track.album = this;
        tracks.add(track);
        return this;
    }

    public Album addTrack(Identifier id)
    {
        addTrack(new Track(id));
        return this;
    }

    public Text getName()
    {
        return Text.translatable(Util.createTranslationKey("album", id));
    }

    public Text getArtists()
    {
        return Text.translatable(Util.createTranslationKey("album", id.withSuffixedPath("/artists")));
    }

    public Identifier getIconID()
    {
        return id.withPrefixedPath("textures/album/").withSuffixedPath(".png");
    }
}
