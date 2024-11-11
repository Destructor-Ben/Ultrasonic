package mod.destructor_ben.ultrasonic.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import mod.destructor_ben.ultrasonic.Album;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class AlbumWidget extends AbstractConfigListEntry<Boolean>
{
    // Icons are actually 128, but that was too big and I can't be bothered resizing them
    private static final int ICON_SIZE = 64;

    private final Album album;
    private final AtomicBoolean isEnabled;
    private final boolean originalValue;

    private final List<ClickableWidget> widgets;

    public AlbumWidget(Album album, boolean value, Consumer<Boolean> saveConsumer)
    {
        super(album.getName(), false);
        this.saveCallback = saveConsumer;

        this.album = album;
        this.originalValue = value;
        this.isEnabled = new AtomicBoolean(value);

        // TODO: finish the UI
        // TODO: make a toggle to enable or disable the album
        widgets = new ArrayList<>()
        {

        };
    }

    @Override
    public void render(DrawContext graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta)
    {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);

        graphics.drawTexture(album.getIconID(), x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }

    @Override
    public int getItemHeight()
    {
        return ICON_SIZE;
    }

    @Override
    public boolean isEdited()
    {
        return super.isEdited() || this.originalValue != this.isEnabled.get();
    }

    @Override
    public Boolean getValue()
    {
        return this.isEnabled.get();
    }

    @Override
    public Optional<Boolean> getDefaultValue()
    {
        return Optional.of(true);
    }

    @Override
    public List<? extends Selectable> narratables()
    {
        return widgets;
    }

    @Override
    public List<? extends Element> children()
    {
        return widgets;
    }
}
