package dev.lonmo.tfc2mecompat.neoforge;

import dev.lonmo.tfc2mecompat.CompatInit;
import java.nio.file.Path;
import net.neoforged.fml.common.Mod;

/**
 * NeoForge entrypoint. C2ME's NeoForge port exposes the same dfc registries
 * (com.ishland.c2me.opts.dfc.*), so the bindings are identical to the Fabric
 * side; only the bootstrap differs.
 */
@Mod("tfc2mecompat")
public class TfC2meCompatNeoForge {

	public TfC2meCompatNeoForge() {
		CompatInit.init(Path.of("config"));
	}
}
