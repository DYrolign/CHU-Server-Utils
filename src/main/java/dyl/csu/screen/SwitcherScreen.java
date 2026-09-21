package dyl.csu.screen;

import dyl.csu.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class SwitcherScreen extends Screen {
    private static final Text TITLE = Text.translatable("gui.csu.choose_server");
    private static final Text BACK = Text.translatable("gui.csu.back");

    private final Screen parent;
    public SwitcherScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int startY = this.height / 2 - 30;
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
                        client.setScreen(null);
                    }
            ).dimensions(x, y, buttonWidth, buttonHeight).build());
        }

        this.addDrawableChild(ButtonWidget.builder(BACK, btn -> {
            client.setScreen(new MenuScreen());
        }).dimensions(centerX - buttonWidth/2, startY + 3 * (buttonHeight + spacing), buttonWidth, buttonHeight).build());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}