package absolemjackdaw.capability;

import absolemjackdaw.CowSimulator;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AttachCapability {
    @SubscribeEvent
    public static void onAttach(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player)
            if (player instanceof ServerPlayer serverPlayer)
                event.addCapability(CowCapability.KEY, new CowCapability(serverPlayer));
            else
                event.addCapability(CowCapability.KEY, new CowCapability(null));
    }
}
