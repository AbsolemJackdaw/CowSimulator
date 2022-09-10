package absolemjackdaw;

import absolemjackdaw.item.AnimalBrew;
import absolemjackdaw.item.RegisterItem;
import absolemjackdaw.network.CowNetwork;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mod(CowSimulator.MODID)
@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CowSimulator {
    public static final String MODID = "cowsimulator";
    public static final Map<Item, Supplier<Potion>> brew = new HashMap<>();

    static {
        CowNetwork.init();
        RegisterItem.init();
        CowApi.classLoad();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(CowSimulator::modConfig);
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, ConfigData.SERVER_SPEC);
    }

    public static void modConfig(ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == ConfigData.SERVER_SPEC)
            ConfigData.refreshServer();
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            brew.forEach((item, potion) -> {
                BrewingRecipeRegistry.addRecipe(new AnimalBrew(item, potion.get()));

            });
        });
    }
}
