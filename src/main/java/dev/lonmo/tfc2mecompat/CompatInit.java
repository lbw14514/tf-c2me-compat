package dev.lonmo.tfc2mecompat;

import dev.lonmo.tfc2mecompat.bind.TfDfBindings;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loader-agnostic initialization shared by the Fabric and NeoForge entrypoints.
 */
public final class CompatInit {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompatInit.class);

	private CompatInit() {
	}

	public static void init(Path configDir) {
		try {
			TfDfBindings.register();
			LOGGER.info("[TF-C2ME-Compat] Twilight Forest <-> C2ME dfc bindings active (JVM + OpenCL)");
		} catch (Throwable t) {
			// Never hard-fail the game over an optimization mod; C2ME will
			// simply fall back to DelegateNode evaluation without us.
			LOGGER.error("[TF-C2ME-Compat] Failed to register dfc bindings; TF will fall back to C2ME's default handling", t);
		}
		// Only relevant when the separate OpenCL acceleration module is installed.
		OpenclFallbackEnabler.enable(configDir);
	}
}
