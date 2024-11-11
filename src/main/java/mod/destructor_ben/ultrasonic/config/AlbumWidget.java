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

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class AlbumWidget extends AbstractConfigListEntry<Boolean>
{
    // Icons are actually 128, but that was too big and I can't be bothered resizing them
    private static final int ICON_SIZE = 64;
    private static final int PADDING = 4; // Padding below elements that cloth creates

    private final Album album;
    private final AtomicBoolean isEnabled;
    private final AtomicBoolean isTracksVisible;
    private final boolean originalValue;

    private final List<ClickableWidget> widgets;
    private final ButtonWidget toggleEnabledButton;
    private final ButtonWidget toggleTracksVisibleButton;

    public AlbumWidget(Album album, boolean value, Consumer<Boolean> saveConsumer)
    {
        super(album.getName(), false);
        saveCallback = saveConsumer;

        this.album = album;
        originalValue = value;
        isEnabled = new AtomicBoolean(value);
        isTracksVisible = new AtomicBoolean(false);

        toggleEnabledButton = ButtonWidget.builder(Text.empty(), widget -> this.isEnabled.set(!this.isEnabled.get()))
                                          .dimensions(0, 0, 50, 20)
                                          .build();

        toggleTracksVisibleButton = ButtonWidget.builder(Text.empty(), widget -> this.isTracksVisible.set(!this.isTracksVisible.get()))
                                                .dimensions(0, 0, 50, 20)
                                                .build();

        widgets = List.of(toggleEnabledButton, toggleTracksVisibleButton);
    }

    @Override
    public void render(DrawContext graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta)
    {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);

        var textRenderer = MinecraftClient.getInstance().textRenderer;

        graphics.drawTexture(album.getIconID(), x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        graphics.drawText(textRenderer, album.getName(), x + ICON_SIZE + PADDING, y, 0xFFFFFFFF, true);
        graphics.drawText(textRenderer, album.getArtists(), x + ICON_SIZE + PADDING, y + 16, 0xFFAAAAAA, true);

        toggleEnabledButton.active = this.isEditable();
        toggleEnabledButton.setPosition(x + ICON_SIZE + PADDING, y + 16 + 16);
        toggleEnabledButton.setMessage(Text.translatable(isEnabled.get() ? "§aEnabled" : "§cDisabled"));
        toggleEnabledButton.render(graphics, mouseX, mouseY, delta);

        toggleTracksVisibleButton.active = this.isEditable();
        toggleTracksVisibleButton.setPosition(x + ICON_SIZE + PADDING + 50, y + 16 + 16);
        toggleEnabledButton.setMessage(Text.translatable(isTracksVisible.get() ? "Hide" : "Show"));
        toggleTracksVisibleButton.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public int getItemHeight()
    {
        return ICON_SIZE + PADDING;
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
