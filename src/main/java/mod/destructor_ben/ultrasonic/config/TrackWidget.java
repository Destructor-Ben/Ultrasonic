package mod.destructor_ben.ultrasonic.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import mod.destructor_ben.ultrasonic.Track;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class TrackWidget extends AbstractConfigListEntry<Boolean>
{
    private final Track track;
    private final AlbumWidget albumWidget;
    private final AtomicBoolean isEnabled;
    private final boolean originalValue;

    private final List<ClickableWidget> widgets;

    public TrackWidget(Track track, AlbumWidget albumWidget, boolean value, Consumer<Boolean> saveConsumer)
    {
        // TODO: make the name also include the album
        super(track.getName(), false);
        saveCallback = saveConsumer;

        this.track = track;
        this.albumWidget = albumWidget;
        originalValue = value;
        isEnabled = new AtomicBoolean(value);

        // TODO: add the toggle button - make it uneditable when the parent is disabled

        widgets = List.of();
    }

    @Override
    public void render(DrawContext graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta)
    {
        // Only draw if the toggle has been enabled
        if (!albumWidget.shouldTracksDraw())
            return;

        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);

        // TODO: rendering
        var textRenderer = MinecraftClient.getInstance().textRenderer;

        graphics.drawText(textRenderer, track.getName(), x, y, 0xFFFFFF, true);
    }

    @Override
    public int getItemHeight()
    {
        return albumWidget.shouldTracksDraw() ? super.getItemHeight() : 0;
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
