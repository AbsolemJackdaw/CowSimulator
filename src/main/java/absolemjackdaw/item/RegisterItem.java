package absolemjackdaw.item;

import absolemjackdaw.CowSimulator;
import absolemjackdaw.animals.AnimalCurse;
import absolemjackdaw.animals.AnimalRegistry;
import absolemjackdaw.animals.Axolotl;
import absolemjackdaw.animals.Cow;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RegisterItem {
    private static final DeferredRegister<Potion> potionRegistry = DeferredRegister.create(Registry.POTION_REGISTRY, CowSimulator.MODID);
    private static final DeferredRegister<MobEffect> effectRegistry = DeferredRegister.create(Registry.MOB_EFFECT_REGISTRY, CowSimulator.MODID);

    static {
        register(new Cow(), CowSimulator.MODID, "cow", Items.MILK_BUCKET);
        register(new Axolotl(), CowSimulator.MODID, "axolotl", Items.KELP);
    }

    public static void register(AnimalCurse curse, String modid, String animal, Item potionIngredient) {
        ResourceLocation animalIdentifier = new ResourceLocation(modid, animal);
        RegistryObject<MobEffect> animalEffect = effectRegistry.register(String.format("%s_effect", animal), () -> new AnimalPotion.AnimalEffect(animalIdentifier));

        String potionName = String.format("%s_potion", animal);
        RegistryObject<Potion> animalPotion = potionRegistry.register(potionName, () -> new AnimalPotion(potionName));
        CowSimulator.brew.put(potionIngredient, animalPotion);
        AnimalRegistry.INSTANCE.register(curse, animalIdentifier);
    }

    public static void init() {
        potionRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        effectRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
