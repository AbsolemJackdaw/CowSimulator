package absolemjackdaw.item;

import absolemjackdaw.CowSimulator;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RegisterItem {
    private static final DeferredRegister<Potion> potionRegistry = DeferredRegister.create(Registry.POTION_REGISTRY, CowSimulator.MODID);
    private static final DeferredRegister<MobEffect> effectRegistry = DeferredRegister.create(Registry.MOB_EFFECT_REGISTRY, CowSimulator.MODID);
    public static final RegistryObject<MobEffect> effect = effectRegistry.register("cow_effect", CowPotion.CowEffect::new);
    public static final RegistryObject<Potion> potion = potionRegistry.register("cow_potion", CowPotion::new);

    public static void init() {
        potionRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        effectRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
