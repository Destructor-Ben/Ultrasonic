package mod.destructor_ben.ultrasonic;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class Track
{
    public Identifier id;
    public boolean isUnknown = false;

    public Track(Identifier id)
    {
        this.id = id;
    }

    public Text getName()
    {
        var name = Text.translatable(Util.createTranslationKey("track", id));
        return isUnknown ? Text.translatable("track.unknown", name) : name;
    }

    public Text getArtistName()
    {
        var artist = Text.translatable(Util.createTranslationKey("track", id.withSuffixedPath("/artist")));
        return isUnknown ? Text.translatable("artist.unknown", artist) : artist;
    }
}
