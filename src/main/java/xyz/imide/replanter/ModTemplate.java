package xyz.imide.replanter;

import xyz.imide.replanter.platform.Platform;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? fabric {
import xyz.imide.replanter.platform.fabric.FabricPlatform;
//?} neoforge {
/*import xyz.imide.replanter.platform.neoforge.NeoforgePlatform;
 *///?} forge {
/*import xyz.imide.replanter.platform.forge.ForgePlatform;
 *///?}

@SuppressWarnings("LoggingSimilarMessage")
public class ModTemplate {

	public static final String MOD_ID = /*$ mod_id*/ "replanter";
	public static final String MOD_VERSION = /*$ mod_version*/ "2.0.0";
	public static final String MOD_FRIENDLY_NAME = /*$ mod_name*/ "Replanter";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Platform PLATFORM = createPlatformInstance();

	public static void onInitialize() {
		LOGGER.info("Initializing {} on {}", MOD_ID, ModTemplate.xplat().loader());
		LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
	}

	public static void onInitializeClient() {
		LOGGER.info("Initializing {} Client on {}", MOD_ID, ModTemplate.xplat().loader());
		LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
	}

	static Platform xplat() {
		return PLATFORM;
	}

	private static Platform createPlatformInstance() {
		//? fabric {
		return new FabricPlatform();
		//?} neoforge {
		/*return new NeoforgePlatform();
		 *///?} forge {
		/*return new ForgePlatform();
		 *///?}
	}

	private static Identifier id(String path) {
		//? > 1.19.2 {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
		 //?} <= 1.19.2 {
		/*return new Identifier(MOD_ID, path);
		*///?}
	}

	private static Identifier id(String namespace, String path) {
		//? > 1.19.2 {
		return Identifier.fromNamespaceAndPath(namespace, path);
		 //?} <= 1.19.2 {
		/*return new Identifier(namespace, path);
		*///?}
	}
}
