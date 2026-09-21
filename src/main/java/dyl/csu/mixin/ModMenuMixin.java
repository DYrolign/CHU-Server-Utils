package dyl.csu.mixin;

import com.terraformersmc.modmenu.gui.ModsScreen;
import com.terraformersmc.modmenu.util.mod.Mod;
import com.terraformersmc.modmenu.util.mod.ModSearch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ModSearch.class)
public class ModMenuMixin {

    /** 你的模组ID */
    private static final String MODID = "csu";

    @Inject(method = "search", at = @At("RETURN"), cancellable = true, require = 0)
    private static void csu$forceFirst(
            ModsScreen screen, String query, List<Mod> candidates,
            CallbackInfoReturnable<List<Mod>> cir) {
        try {
            List<Mod> result = cir.getReturnValue();
            if (result == null || result.isEmpty()) return;

            // 搜索时不要强制置顶（避免干扰用户搜索）
            if (query != null && !query.isEmpty()) return;

            // 找到自己的模组
            int myIndex = -1;
            for (int i = 0; i < result.size(); i++) {
                if (MODID.equals(result.get(i).getId())) {
                    myIndex = i;
                    break;
                }
            }

            // 没找到，或已经在第一位，就不动
            if (myIndex <= 0) return;

            // 移到列表首位
            Mod myMod = result.remove(myIndex);
            result.add(0, myMod);
            cir.setReturnValue(result);
        } catch (Exception e) {
            // 万一 Mod Menu 内部结构变了，静默失败，不影响正常使用
        }
    }
}