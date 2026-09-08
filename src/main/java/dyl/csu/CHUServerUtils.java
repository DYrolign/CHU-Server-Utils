package dyl.csu;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CHUServerUtils implements ModInitializer{
	public static final String MOD_ID = "csu";
	public static final String MOD_NAME = "CHU Server Utils";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	@Override
	public void onInitialize() {
		LOGGER.info("Welcome to CHU Server!");
	}
}

