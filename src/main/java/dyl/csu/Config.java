package dyl.csu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dyl.csu.CHUServerUtils.LOGGER;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("csu.json");

    /** 固定第一个位置 */
    public static final String FIRST_SERVER = "survival";
    /** 位置总数 */
    public static final int SLOT_COUNT = 4;

    /** 服务器列表，长度始终为 4，第一个为 "survival" */
    public List<String> servers = new ArrayList<>(Arrays.asList(
            FIRST_SERVER, "", "", ""
    ));

    /** 单例 */
    public static final Config INSTANCE = new Config();

    /** 规范化：确保列表长度为4，且第一个为 "survival" */
    public void normalize() {
        while (servers.size() < SLOT_COUNT) {
            servers.add("");
        }
        if (servers.size() > SLOT_COUNT) {
            servers = new ArrayList<>(servers.subList(0, SLOT_COUNT));
        }
        if (!FIRST_SERVER.equals(servers.get(0))) {
            servers.set(0, FIRST_SERVER);
        }
    }

    /** 获取实际要显示的服务器（过滤掉空项） */
    public List<String> getValidServers() {
        normalize();
        List<String> valid = new ArrayList<>();
        for (String s : servers) {
            if (s != null && !s.trim().isEmpty()) {
                valid.add(s.trim());
            }
        }
        return valid;
    }

    /** 从文件加载 */
    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                Config loaded = GSON.fromJson(reader, Config.class);
                if (loaded != null && loaded.servers != null) {
                    INSTANCE.servers = loaded.servers;
                }
            } catch (IOException e) {
                LOGGER.error("读取配置失败", e);
            }
        }
        INSTANCE.normalize();
        save(); // 首次生成或修正后写回
    }

    /** 写入文件 */
    public static void save() {
        INSTANCE.normalize();
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            LOGGER.error("保存配置失败", e);
        }
    }
}
