package dyl.csu.screen;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class CSUInventoryScreen extends InventoryScreen {
    public CSUInventoryScreen(PlayerEntity player) {
        super(player);
    }

    @Override
    protected void init() {
        super.init();
        int panelLeft = this.x;
        int panelTop = this.y;
        int buttonSize = 10;
        int buttonX = panelLeft + 165 - buttonSize / 2;
        int buttonY = panelTop + buttonSize / 2;

        ButtonWidget button = ButtonWidget.builder(Text.literal("H"), btn -> {
                    if (this.client != null && this.client.player != null) {
                        client.setScreen(new MenuScreen());
                    }
                }
        ).dimensions(buttonX, buttonY, buttonSize, buttonSize).build();

        this.addDrawableChild(button);
    }
}
