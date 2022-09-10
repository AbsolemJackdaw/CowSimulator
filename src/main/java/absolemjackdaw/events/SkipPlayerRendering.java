package absolemjackdaw.events;

import absolemjackdaw.CowSimulator;
import absolemjackdaw.animals.AnimalRegistry;
import absolemjackdaw.capability.CowData;
import absolemjackdaw.client.ClientSidedCalls;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SkipPlayerRendering {

    @SubscribeEvent
    public static void skip(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        CowData.get(player).ifPresent(cowData -> {

            if (cowData.isClientAnimal(event.getEntity())) {
                event.setCanceled(true); //cancel complete rendering and render only a cow instead
                event.getPoseStack().pushPose();
                AnimalRegistry.INSTANCE.get(cowData.getAnimalIdentifier()).ifPresent(animalCurse ->
                        animalCurse.render(cowData, player, event.getRenderer(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight()));
                event.getPoseStack().popPose();
            }
        });
    }

    @SubscribeEvent
    public static void skip(RenderHandEvent event) {
        CowData.get(ClientSidedCalls.getClientPlayer()).ifPresent(cowData -> {
            if (cowData.isClientAnimal(ClientSidedCalls.getClientPlayer())) {
                event.setCanceled(true);

                event.getPoseStack().pushPose();
                AnimalRegistry.INSTANCE.get(cowData.getAnimalIdentifier()).ifPresent(animalCurse ->
                        animalCurse.renderBody(cowData, event.getHand(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), event.getPartialTick(), event.getInterpolatedPitch(), event.getSwingProgress(), event.getEquipProgress(), event.getItemStack()));
                event.getPoseStack().popPose();
            }
        });
    }
}
