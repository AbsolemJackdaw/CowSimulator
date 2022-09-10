package absolemjackdaw.events.cow;

import absolemjackdaw.CowApi;
import absolemjackdaw.CowSimulator;
import absolemjackdaw.capability.CowData;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CowIsSafeEvent {

    @SubscribeEvent
    public static void beSafe(LivingSetAttackTargetEvent event) {
        if (event.getTarget() instanceof Player player) {
            CowData.get(player).ifPresent(cowData -> {
                if (cowData.is(CowApi.cowAnimal)) {
                    if (event.getEntity() instanceof Mob mob) {
                        mob.setTarget(null);
                    }
                }
            });
        }

    }
}
