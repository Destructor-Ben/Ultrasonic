package mod.destructor_ben.ultrasonic;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

// Have to use this because we can reload resources at any time
public class MusicResourceListener implements IdentifiableResourceReloadListener
{
    @Override
    public Identifier getFabricId()
    {
        return Identifier.of(Ultrasonic.MOD_ID, "music");
    }

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor)
    {
        return CompletableFuture.runAsync(() -> MusicDatabase.initialize(manager), prepareExecutor).thenCompose(synchronizer::whenPrepared);
    }
}
