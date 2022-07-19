package absolemjackdaw.capability;

import absolemjackdaw.CowSimulator;
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
            event.addCapability(CowCapability.KEY, new CowCapability(player));
    }
}
