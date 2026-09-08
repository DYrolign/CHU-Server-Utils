package dyl.csu.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class MenuScreen extends Screen {
    private static final Text MENU_TITLE = Text.translatable("gui.csu.menu");
    private static final Text SERVER_SWITCHER = Text.translatable("gui.csu.server_switcher");
    private static final Text CANCEL = Text.translatable("gui.csu.cancel");

    public MenuScreen() {
        super(MENU_TITLE);
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int buttonWidth = 100;
        int buttonHeight = 20;
        int titleWidth = this.textRenderer.getWidth(MENU_TITLE);

        TextWidget title = new TextWidget((this.width - titleWidth) / 2, this.height / 2 - 60, titleWidth, this.textRenderer.fontHeight, MENU_TITLE, this.textRenderer);

        this.addDrawableChild(title);

        this.addDrawableChild(ButtonWidget.builder(SERVER_SWITCHER, btn -> {
            client.setScreen(new SwitcherScreen());
        }).dimensions(centerX - buttonWidth / 2, this.height / 2 - 30, buttonWidth, buttonHeight).build());

        this.addDrawableChild(ButtonWidget.builder(CANCEL, btn -> {
            this.close();
        }).dimensions(centerX - buttonWidth / 2, this.height / 2 + 50, buttonWidth, buttonHeight).build());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}