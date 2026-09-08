package dyl.csu;

import dyl.csu.screen.MenuScreen;
import dyl.csu.screen.CSUInventoryScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import static dyl.csu.CHUServerUtils.MOD_ID;

public class CHUServerUtilsClient implements ClientModInitializer {
	private static KeyBinding openScreenKey;

	@Override
	public void onInitializeClient() {
		KeyBinding.Category csuCategory = new KeyBinding.Category(Identifier.of(MOD_ID, "general"));
		openScreenKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.csu.open_switcher",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_H,
				csuCategory
		));

		// 监听客户端 Tick 事件，检测按键是否按下
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openScreenKey.wasPressed()) {
				if (client.currentScreen == null) {
					client.setScreen(new MenuScreen());
				}
			}
		});

		// 玩家加入给出按键提示
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			if (client.player != null) {
				Text keyText = ((MutableText)openScreenKey.getBoundKeyLocalizedText()).formatted(Formatting.YELLOW);;
				Text key_tip = Text.translatable("message.csu.key_tip", keyText);
				client.player.sendMessage(key_tip,false);
			}
		});

		ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof InventoryScreen original) {
				if (!(screen instanceof CSUInventoryScreen)) {
					Screen newScreen = new CSUInventoryScreen(client.player);
					client.setScreen(newScreen);
				}
			}
		});
	}
}

