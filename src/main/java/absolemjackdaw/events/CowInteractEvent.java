package absolemjackdaw.events;

import absolemjackdaw.CowSimulator;
import absolemjackdaw.capability.CowData;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CowInteractEvent {
    @SubscribeEvent
    public static void interact(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (event.getEntity() instanceof Cow) {
                CowData.get(player).ifPresent(cowData -> {
                    cowData.isCow = true;
                });
            }
        }
    }
}
