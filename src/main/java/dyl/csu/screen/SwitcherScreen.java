package dyl.csu.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class SwitcherScreen extends Screen {
    private static final Text TITLE = Text.translatable("gui.csu.choose_server");
    private static final Text SURVIVAL = Text.translatable("gui.csu.survival");
    private static final Text MINIGAMES = Text.translatable("gui.csu.minigames");
    private static final Text BACK = Text.translatable("gui.csu.back");

    public SwitcherScreen() {
        super(TITLE);
    }

    @Override
    protected void init() {
        super.init();
        int centerX = this.width / 2;
        int buttonWidth = 100;
        int buttonHeight = 20;
        int spacing = 10;
        int titleWidth = this.textRenderer.getWidth(TITLE);

        this.addDrawableChild(
                new TextWidget(
                        (this.width - titleWidth) / 2,
                        this.height / 2 - 60,
                        titleWidth,
                        this.textRenderer.fontHeight,
                        TITLE,
                        this.textRenderer
                )
        );

        this.addDrawableChild(ButtonWidget.builder(SURVIVAL, btn -> {
            String server = "survival";
            if (client.player != null) {
                client.player.networkHandler.sendChatCommand("server " + server);
            }
            this.close();
        }).dimensions(centerX - buttonWidth - spacing / 2, this.height / 2 - 30, buttonWidth, buttonHeight).build());

        this.addDrawableChild(ButtonWidget.builder(MINIGAMES, btn -> {
            String server = "minigames";
            if (client.player != null) {
                client.player.networkHandler.sendChatCommand("server " + server);
            }
            this.close();
        }).dimensions(centerX + spacing / 2, this.height / 2 - 30, buttonWidth, buttonHeight).build());

        this.addDrawableChild(ButtonWidget.builder(BACK, btn -> {
            client.setScreen(new MenuScreen());
        }).dimensions(centerX - buttonWidth/2, this.height / 2 + 50, buttonWidth, buttonHeight).build());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}