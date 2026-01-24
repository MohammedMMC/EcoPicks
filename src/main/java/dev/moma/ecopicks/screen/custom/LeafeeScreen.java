package dev.moma.ecopicks.screen.custom;

import dev.moma.ecopicks.EcoPicks;
import dev.moma.ecopicks.entity.custom.LeafeeEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LeafeeScreen extends HandledScreen<LeafeeScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(EcoPicks.MOD_ID, "textures/gui/leafee_gui.png");
    private static final Identifier ARROW_TEXTURE = Identifier.of(EcoPicks.MOD_ID, "textures/gui/arrow_progress.png");

    public LeafeeScreen(LeafeeScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 166;
        this.backgroundWidth = 176;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        this.drawProgressArrow(context, x, y);
        this.drawEntity(context, x, y, mouseX, mouseY);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private void drawProgressArrow(DrawContext context, int x, int y) {
        if (this.handler == null)
            return;

        int progress = this.handler.getPropertyDelegate().get(0);
        int arrowWidth = (progress * 24) / LeafeeEntity.MAX_PROGRESS;

        if (progress > 0) {
            arrowWidth = Math.max(2, arrowWidth);
        }

        context.drawTexture(ARROW_TEXTURE,
                x + 99, y + 20, 0, 0,
                arrowWidth, 16, 24, 16);
    }

    private void drawEntity(DrawContext context, int x, int y, int mouseX, int mouseY) {
        int left = (width - backgroundWidth) / 2;
        int top = (height - backgroundHeight) / 2;
        LeafeeEntity leafee = handler.entity;
        if (leafee != null) {
            int boxLeft = left + 10;
            int boxTop = top + 16;
            int boxRight = left + 63;
            int boxBottom = top + 69;
            int scale = 32;

            InventoryScreen.drawEntity(context, boxLeft, boxTop, boxRight, boxBottom,
                    scale, 0, mouseX, mouseY, leafee);
        }
    }
}
