package absolemjackdaw.events.axolotl;

import absolemjackdaw.CowSimulator;
import absolemjackdaw.animals.model.PlayerAxolotlModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterModel {

    public static final ModelLayerLocation AXOLOTLLAYER = new ModelLayerLocation(new ResourceLocation(CowSimulator.MODID, "axolotl"), "axolotl");

    @SubscribeEvent
    public static void registerlayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AXOLOTLLAYER, PlayerAxolotlModel::createBodyLayer);
    }
}
