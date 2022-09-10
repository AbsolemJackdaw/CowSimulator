package absolemjackdaw.item;

import absolemjackdaw.CowSimulator;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

public class RegisterItem {
    public static final DeferredRegister<Potion> potionRegistry = DeferredRegister.create(Registry.POTION_REGISTRY, CowSimulator.MODID);
    public static final DeferredRegister<MobEffect> effectRegistry = DeferredRegister.create(Registry.MOB_EFFECT_REGISTRY, CowSimulator.MODID);

    public static void init() {
        potionRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        effectRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
