package mod.destructor_ben.ultrasonic.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import mod.destructor_ben.ultrasonic.Album;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class AlbumWidget extends AbstractConfigListEntry<Boolean>
{
    @SuppressWarnings("DataFlowIssue")
    public static final int GRAY = Formatting.GRAY.getColorValue();

    public static final int PADDING = 4; // Padding below elements that cloth creates
    // Icons are actually 128, but that was too big and I can't be bothered resizing them
    private static final int ICON_SIZE = 64;
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;

    private static final Text ENABLED_TEXT = Text.translatable("option.ultrasonic.enabled");
    private static final Text DISABLED_TEXT = Text.translatable("option.ultrasonic.disabled");
    private static final Text HIDE_TEXT = Text.translatable("option.ultrasonic.hide_tracks");
    private static final Text SHOW_TEXT = Text.translatable("option.ultrasonic.show_tracks");
    private static final Text EMPTY_TEXT = Text.translatable("option.ultrasonic.empty");

    private final Album album;
    private final AtomicBoolean isEnabled;
    private final AtomicBoolean isTracksVisible;
    private final boolean originalValue;

    private final List<ClickableWidget> widgets;
    private final ButtonWidget toggleEnabledButton;
    private final ButtonWidget toggleTracksVisibleButton;

    public boolean shouldTracksDraw()
    {
        return isTracksVisible.get();
    }

    public AlbumWidget(Album album, boolean value, Consumer<Boolean> saveConsumer)
    {
        super(album.getName(), false);
        saveCallback = saveConsumer;

        this.album = album;
        originalValue = value;
        isEnabled = new AtomicBoolean(value);
        isTracksVisible = new AtomicBoolean(false);

        // Create buttons UI
        toggleEnabledButton = ButtonWidget.builder(Text.empty(), widget -> this.isEnabled.set(!this.isEnabled.get()))
                                          .width(BUTTON_WIDTH)
                                          .build();

        toggleTracksVisibleButton = ButtonWidget.builder(Text.empty(), widget -> this.isTracksVisible.set(!this.isTracksVisible.get()))
                                                .width(BUTTON_WIDTH)
                                                .build();

        // Create widgets list
        widgets = List.of(toggleEnabledButton, toggleTracksVisibleButton);
    }

    @Override
    public void render(DrawContext graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta)
    {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        var textRenderer = MinecraftClient.getInstance().textRenderer;

        // Draw album icon
        int iconY = y + PADDING;
        graphics.drawTexture(album.getIconID(), x, iconY, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);

        // Draw text info
        int textX = x + ICON_SIZE + PADDING;
        int textY = iconY;
        graphics.drawTextWithShadow(textRenderer, album.getName(), textX, textY, this.getPreferredTextColor());
        textY += textRenderer.fontHeight + PADDING;
        graphics.drawTextWithShadow(textRenderer, album.getArtists(), textX, textY, GRAY);

        // Draw buttons
        int buttonX = textX;
        int buttonY = y + PADDING + ICON_SIZE - BUTTON_HEIGHT;
        // TODO: make this button inactive too if the album is empty? reset the value too?
        toggleEnabledButton.active = this.isEditable();
        toggleEnabledButton.setPosition(buttonX, buttonY);
        toggleEnabledButton.setMessage(isEnabled.get() ? ENABLED_TEXT : DISABLED_TEXT);
        toggleEnabledButton.render(graphics, mouseX, mouseY, delta);

        buttonX += BUTTON_WIDTH + PADDING;
        toggleTracksVisibleButton.active = this.isEditable() && !album.tracks.isEmpty();
        toggleTracksVisibleButton.setPosition(buttonX, buttonY);
        // TODO: maybe make the text just say "Empty"?
        toggleTracksVisibleButton.setMessage(album.tracks.isEmpty() ? EMPTY_TEXT : isTracksVisible.get() ? HIDE_TEXT : SHOW_TEXT);
        toggleTracksVisibleButton.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public int getItemHeight()
    {
        return ICON_SIZE + PADDING * 3;
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
