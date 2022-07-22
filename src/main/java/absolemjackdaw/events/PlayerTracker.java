package absolemjackdaw.events;

import absolemjackdaw.CowSimulator;
import absolemjackdaw.capability.CowData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerTracker {

    @SubscribeEvent
    public static void track(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player)
            CowData.get(player).ifPresent(CowData::sync);
    }

    @SubscribeEvent
    public static void track(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player)
            CowData.get(player).ifPresent(CowData::sync);
    }

    @SubscribeEvent
    public static void track(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof ServerPlayer player)
            CowData.get(player).ifPresent(CowData::sync);
    }

    @SubscribeEvent
    public static void track(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player)
            CowData.get(player).ifPresent(cowData -> cowData.turnCow(false));
    }

    @SubscribeEvent
    public static void track(PlayerEvent.Clone event) {
        if (event.isWasDeath() && event.getEntity() instanceof ServerPlayer newPlayer && event.getOriginal() instanceof ServerPlayer originalPlayer) {
            originalPlayer.reviveCaps();
            CowData.get(originalPlayer).ifPresent(ogCow -> {
                CowData.get(newPlayer).ifPresent(nCow -> {
                    nCow.read(ogCow.write());
                });
            });
            originalPlayer.invalidateCaps();
        }

    }
}
