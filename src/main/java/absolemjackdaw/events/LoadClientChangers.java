package absolemjackdaw.events;

import absolemjackdaw.Constants;
import absolemjackdaw.CowSimulator;
import absolemjackdaw.animals.AxolotlChanger;
import absolemjackdaw.animals.ClientAnimalChangerRegistry;
import absolemjackdaw.animals.CowChanger;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LoadClientChangers {

    @SubscribeEvent
    public static void load(FMLClientSetupEvent event) {
        ClientAnimalChangerRegistry.INSTANCE.register(new CowChanger(), Constants.cowAnimal);
        ClientAnimalChangerRegistry.INSTANCE.register(new AxolotlChanger(), Constants.axolotlAnimal);
    }
}
