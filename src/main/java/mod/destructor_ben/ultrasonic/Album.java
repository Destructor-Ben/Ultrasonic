package mod.destructor_ben.ultrasonic;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;

public class Album
{
    public Identifier id;
    public List<Track> tracks;

    public Album(Identifier id)
    {
        this.id = id;
        this.tracks = new ArrayList<>();
    }

    public Album addTrack(Track track)
    {
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
}
