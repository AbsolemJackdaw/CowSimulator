package absolemjackdaw.events.axolotl;

import absolemjackdaw.CowApi;
import absolemjackdaw.CowSimulator;
import absolemjackdaw.capability.CowData;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForceSwimmingPose {

    @SubscribeEvent
    public static void update(TickEvent.PlayerTickEvent event) {
        CowData.get(event.player).ifPresent(cowData -> {
            if(cowData.is(CowApi.axolotlAnimal)){
                event.player.setForcedPose(Pose.SWIMMING);
            }
        });
    }
}
