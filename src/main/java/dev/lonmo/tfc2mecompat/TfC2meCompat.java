package dev.lonmo.tfc2mecompat;

import com.ishland.c2me.opts.dfc.common.ast.McToAst;
import dev.lonmo.tfc2mecompat.bind.TfDfBindings;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TfC2meCompat implements ModInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TfC2meCompat.class);

	@Override
	public void onInitialize() {
		if (!FabricLoader.getInstance().isModLoaded("twilightforest")) {
			return; // declared as a dependency, but stay defensive
		}
		if (!FabricLoader.getInstance().isModLoaded("c2me")) {
			return;
		}
		try {
			TfDfBindings.register();
			LOGGER.info("[TF-C2ME-Compat] Twilight Forest <-> C2ME dfc bindings active (JVM + OpenCL)");
		} catch (Throwable t) {
			// Never hard-fail the game over an optimization mod; C2ME will
			// simply fall back to DelegateNode evaluation without us.
			LOGGER.error("[TF-C2ME-Compat] Failed to register dfc bindings; TF will fall back to C2ME's default handling", t);
		}
		// Only relevant when the separate OpenCL acceleration module is installed.
		OpenclFallbackEnabler.enable();
	}
}
