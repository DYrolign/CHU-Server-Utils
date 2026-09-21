package dyl.csu.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;

public class MenuScreen extends Screen {
    private static final Text MENU_TITLE = Text.translatable("gui.csu.menu");
    private static final Text SERVER_SWITCHER = Text.translatable("gui.csu.server_switcher");
    private static final Text CONFIG = Text.translatable("gui.csu.config");
    private static final Text CANCEL = Text.translatable("gui.csu.cancel");

    public MenuScreen() {
        super(MENU_TITLE);
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int startY = this.height / 2 - 30;
        int buttonWidth = 100;
        int buttonHeight = 20;
        int spacing = 10;
        int titleWidth = this.textRenderer.getWidth(MENU_TITLE);

        TextWidget title = new TextWidget((this.width - titleWidth) / 2, this.height / 2 - 60, titleWidth, this.textRenderer.fontHeight, MENU_TITLE, this.textRenderer);

        this.addDrawableChild(title);

        if (client == null) return;

        this.addDrawableChild(ButtonWidget.builder(SERVER_SWITCHER, btn -> {
            client.setScreen(new SwitcherScreen(this));
        }).dimensions(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight).build());

        this.addDrawableChild(ButtonWidget.builder(CONFIG, btn -> {
            client.setScreen(new ConfigScreen(this));
        }).dimensions(centerX - buttonWidth / 2, startY + 2 * (buttonHeight + spacing), buttonWidth, buttonHeight).build());

        this.addDrawableChild(ButtonWidget.builder(CANCEL, btn -> {
            this.close();
        }).dimensions(centerX - buttonWidth / 2, startY + 3 * (buttonHeight + spacing), buttonWidth, buttonHeight).build());
    }

    private static void moveCursorY(double guiY) {
        MinecraftClient client = MinecraftClient.getInstance();
        Window window = client.getWindow();
        long handle = window.getHandle();
        double scale = window.getScaleFactor();

        // 读取当前物理像素坐标
        double currentPhysicalX;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            DoubleBuffer xBuf = stack.mallocDouble(1);
            DoubleBuffer yBuf = stack.mallocDouble(1);
            GLFW.glfwGetCursorPos(handle, xBuf, yBuf);
            currentPhysicalX = xBuf.get(0);
        }

        GLFW.glfwSetCursorPos(handle, currentPhysicalX, guiY * scale);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}