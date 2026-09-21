package dyl.csu.screen;

import dyl.csu.Config;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.List;

public class SwitcherScreen extends Screen {
    private static final Text TITLE = Text.translatable("gui.csu.choose_server");
    private static final Text BACK = Text.translatable("gui.csu.back");
    private static final Text SURVIVAL = Text.translatable("gui.csu.survival");
    private static final Text MINIGAMES = Text.translatable("gui.csu.minigames");


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

        List<String> servers = Config.INSTANCE.getValidServers();

        int startY = this.height / 2 - 30;
        for (int i = 0; i < servers.size(); i++) {
            String server = servers.get(i);
            int row = i / 2;
            int col = i % 2;
            int x = (col == 0)
                    ? centerX - buttonWidth - spacing / 2
                    : centerX + spacing / 2;
            int y = startY + row * (buttonHeight + spacing);

            final String cmd = "server " + server;
            this.addDrawableChild(ButtonWidget.builder(
                    Text.literal(server),
                    btn -> {
                        if (client != null && client.player != null) {
                            client.player.networkHandler.sendChatCommand(cmd);
                        }
                        this.close();
                    }
            ).dimensions(x, y, buttonWidth, buttonHeight).build());
        }

        /*
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
        }).dimensions(centerX + spacing / 2, this.height / 2 - 30, buttonWidth, buttonHeight).build());*/

        this.addDrawableChild(ButtonWidget.builder(BACK, btn -> {
            client.setScreen(new MenuScreen());
        }).dimensions(centerX - buttonWidth/2, this.height / 2 + 50, buttonWidth, buttonHeight).build());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}