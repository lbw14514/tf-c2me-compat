package dev.lonmo.tfc2mecompat;

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
		CompatInit.init(FabricLoader.getInstance().getConfigDir());
	}
}
