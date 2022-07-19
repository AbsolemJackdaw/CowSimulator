package absolemjackdaw.events;

import absolemjackdaw.CowSimulator;
import absolemjackdaw.capability.CowData;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SkipPlayerRendering {

    @SubscribeEvent
    public static void skip(RenderPlayerEvent.Pre event) {
        CowData.get(event.getEntity()).ifPresent(cowData -> {
            if (cowData.isCow) {
                event.getRenderer().getModel().setAllVisible(false);
//                event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void skip(RenderPlayerEvent.Post event) {
        CowData.get(event.getEntity()).ifPresent(cowData -> {
            if (cowData.isCow) {

            }
        });
    }
}
