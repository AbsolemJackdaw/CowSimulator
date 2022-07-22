package absolemjackdaw.events;

import absolemjackdaw.CowSimulator;
import absolemjackdaw.client.ClientSidedCalls;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CowsAreDumbEventClient {

    @SubscribeEvent
    public static void dontDoInventory(ScreenEvent.Opening event) {
        if (ClientSidedCalls.getClientPlayer() != null
                && (event.getNewScreen() instanceof InventoryScreen)) {
            CowsAreDumbEvent.dontDoEvent(ClientSidedCalls.getClientPlayer(), event);
        }

    }
}
