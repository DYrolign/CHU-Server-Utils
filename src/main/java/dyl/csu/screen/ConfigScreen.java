package dyl.csu.screen;

import dyl.csu.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {
    private static final Text TITLE = Text.translatable("gui.csu.config");
    private static final Text SAVE = Text.translatable("gui.csu.save");
    private static final Text CANCEL = Text.translatable("gui.csu.cancel");
    private static final Text RESET = Text.literal("↺");

    private final Screen parent;
    private final List<TextFieldWidget> fields = new ArrayList<>();

    public ConfigScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int startFieldY = this.height / 2 - 70;
        int startButtonY = this.height / 2 - 30;
        int fieldWidth = 200;
        int fieldHeight = 20;
        int buttonWidth = 100;
        int buttonHeight = 20;
        int spacing = 10;
        int gap = 8;

        // 标题
        this.addDrawableChild(new TextWidget(
                centerX - this.textRenderer.getWidth(TITLE) / 2,
                startFieldY - 30,
                this.textRenderer.getWidth(TITLE),
                this.textRenderer.fontHeight,
                TITLE,
                this.textRenderer
        ));

        // 4 个输入框
        fields.clear();
        Config.INSTANCE.normalize();
        for (int i = 0; i < Config.SLOT_COUNT; i++) {
            int y = startFieldY + i * (fieldHeight + gap);
            TextFieldWidget field = new TextFieldWidget(
                    this.textRenderer,
                    centerX - fieldWidth / 2,
                    y,
                    fieldWidth,
                    fieldHeight,
                    Text.literal("Server " + (i + 1))
            );
            field.setMaxLength(64);
            field.setText(Config.INSTANCE.servers.get(i));

            if (i == 0) {
                // 第一个位置固定，不可编辑
                field.setEditable(false);
                field.setFocused(false);
                field.setEditableColor(0x808080); // 灰色显示
            }

            this.addDrawableChild(field);
            fields.add(field);

            if (i != 0) {
                final int index = i;
                ButtonWidget resetBtn = ButtonWidget.builder(RESET, btn -> {
                    fields.get(index).setText("");
                }).dimensions(centerX - fieldWidth / 2 + fieldWidth + 4, y, fieldHeight, fieldHeight).build();
                this.addDrawableChild(resetBtn);
            }
        }

        // 保存按钮
        this.addDrawableChild(ButtonWidget.builder(SAVE, btn -> {
            for (int i = 0; i < Config.SLOT_COUNT; i++) {
                Config.INSTANCE.servers.set(i, fields.get(i).getText().trim());
            }
            Config.save();
            this.close();
        }).dimensions(centerX - buttonWidth - 5, startButtonY + 3 * (buttonHeight + spacing), buttonWidth, buttonHeight).build());

        // 取消按钮
        this.addDrawableChild(ButtonWidget.builder(CANCEL, btn -> {
            this.close();
        }).dimensions(centerX + 5, startButtonY + 3 * (buttonHeight + spacing), buttonWidth, buttonHeight).build());
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}