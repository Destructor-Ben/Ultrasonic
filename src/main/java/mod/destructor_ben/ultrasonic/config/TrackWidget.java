package mod.destructor_ben.ultrasonic.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import mod.destructor_ben.ultrasonic.Track;
import mod.destructor_ben.ultrasonic.Ultrasonic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class TrackWidget extends AbstractConfigListEntry<Boolean>
{
    private static final int BUTTON_SIZE = 20;
    private static final Identifier TICK_TEXTURE = Identifier.of(Ultrasonic.MOD_ID, "config/tick");
    private static final Identifier CROSS_TEXTURE = Identifier.of(Ultrasonic.MOD_ID, "config/cross");

    private final Track track;
    private final AlbumWidget albumWidget;
    private final AtomicBoolean isEnabled;
    private final boolean originalValue;

    private final List<ClickableWidget> widgets;
    private final ButtonWidget toggleEnabledButton;

    private boolean isVisible()
    {
        // TODO: if we are searching, also make visible
        return albumWidget.shouldTracksDraw();
    }

    public TrackWidget(Track track, AlbumWidget albumWidget, boolean value, Consumer<Boolean> saveConsumer)
    {
        super(Text.literal(track.getName().getString() + " - " + track.getArtistName().getString()), false);
        saveCallback = saveConsumer;

        this.track = track;
        this.albumWidget = albumWidget;
        originalValue = value;
        isEnabled = new AtomicBoolean(value);

        toggleEnabledButton = ButtonWidget.builder(Text.empty(), widget -> this.isEnabled.set(!this.isEnabled.get()))
                                          .width(BUTTON_SIZE)
                                          .build();

        widgets = List.of(toggleEnabledButton);
    }

    @Override
    public void render(DrawContext graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta)
    {
        // Only draw if the toggle has been enabled
        if (!isVisible())
            return;

        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        var textRenderer = MinecraftClient.getInstance().textRenderer;

        // Toggle
        // Disable this when the parent is disabled
        toggleEnabledButton.active = isEditable() && albumWidget.isEditable() && albumWidget.getValue();
        toggleEnabledButton.setPosition(x, y);
        toggleEnabledButton.render(graphics, mouseX, mouseY, delta);

        // Draw tick and cross
        var iconTexture = isEnabled.get() ? TICK_TEXTURE : CROSS_TEXTURE;
        // TODO: clean up
        graphics.drawGuiTexture(iconTexture, x, y, BUTTON_SIZE, BUTTON_SIZE);

        // Draw the track name and artist
        int nameX = x + BUTTON_SIZE + AlbumWidget.PADDING;
        int nameY = y + 6; // 6 comes from what the boolean toggle uses to draw text
        var name = track.getName();
        graphics.drawTextWithShadow(textRenderer, name, nameX, nameY, getPreferredTextColor());

        nameX += textRenderer.getWidth(name);
        graphics.drawTextWithShadow(textRenderer, " - " + track.getArtistName().getString(), nameX, nameY, AlbumWidget.GRAY);
    }

    @Override
    public int getItemHeight()
    {
        return isVisible() ? super.getItemHeight() : 0;
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
