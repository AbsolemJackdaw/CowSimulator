package absolemjackdaw;

import absolemjackdaw.item.AnimalPotion;
import absolemjackdaw.item.RegisterItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class CowApi {

    private static final List<TurnedEvent> turnedEvents = new ArrayList<>();

    public static void registerAnimalPotion(ResourceLocation animalIdentifier, Item potionIngredient, int color) {
        RegistryObject<MobEffect> animalEffect = RegisterItem.effectRegistry.register(String.format("%s_effect", animalIdentifier.getPath()), () -> new AnimalPotion.AnimalEffect(animalIdentifier, color));
        String potionName = String.format("%s_potion", animalIdentifier.getPath());
        RegistryObject<Potion> animalPotion = RegisterItem.potionRegistry.register(potionName, () -> new AnimalPotion(potionName, animalEffect));
        CowSimulator.brew.put(potionIngredient, animalPotion);
    }

    public static void fireEvent(Player player, boolean isAnimal) {
        turnedEvents.forEach(turnedEvent -> {
            if (isAnimal) {
                turnedEvent.toAnimal(player);
            } else {
                turnedEvent.toHuman(player);
            }
        });
    }

    public static void registerTurnedListener(TurnedEvent event) {
        turnedEvents.add(event);
    }

    public interface TurnedEvent {
        void toAnimal(Player player);

        void toHuman(Player player);
    }
}
