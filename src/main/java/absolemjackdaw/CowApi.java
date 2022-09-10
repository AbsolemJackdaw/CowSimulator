package absolemjackdaw;

import absolemjackdaw.animals.AnimalCurse;
import absolemjackdaw.animals.AnimalRegistry;
import absolemjackdaw.animals.Axolotl;
import absolemjackdaw.animals.Cow;
import absolemjackdaw.item.AnimalPotion;
import absolemjackdaw.item.RegisterItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.RegistryObject;

public class CowApi {

    public static void classLoad() {
    }

    public static final ResourceLocation cowAnimal = new ResourceLocation(CowSimulator.MODID, "cow");
    public static final ResourceLocation axolotlAnimal = new ResourceLocation(CowSimulator.MODID, "axolotl");

    static {
        register(new Cow(), cowAnimal, Items.MILK_BUCKET, 0x654321);
        register(new Axolotl(), axolotlAnimal, Items.KELP, 0xf983fb);
    }

    public static void register(AnimalCurse curse, ResourceLocation animalIdentifier, Item potionIngredient, int color) {
        RegistryObject<MobEffect> animalEffect = RegisterItem.effectRegistry.register(String.format("%s_effect", animalIdentifier.getPath()), () -> new AnimalPotion.AnimalEffect(animalIdentifier, color));

        String potionName = String.format("%s_potion", animalIdentifier.getPath());
        RegistryObject<Potion> animalPotion = RegisterItem.potionRegistry.register(potionName, () -> new AnimalPotion(potionName, animalEffect));
        CowSimulator.brew.put(potionIngredient, animalPotion);
        AnimalRegistry.INSTANCE.register(curse, animalIdentifier);
    }
}
