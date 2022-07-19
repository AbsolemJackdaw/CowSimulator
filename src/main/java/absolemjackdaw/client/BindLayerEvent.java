package absolemjackdaw.client;

import absolemjackdaw.CowSimulator;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BindLayerEvent {

    @SubscribeEvent
    public static void attachLayer(EntityRenderersEvent.AddLayers event) {
        event.getSkins().forEach(skin -> {
            if (event.getSkin(skin) instanceof PlayerRenderer renderer)
                renderer.addLayer(new CowLayer(renderer));
        });
    }
}
