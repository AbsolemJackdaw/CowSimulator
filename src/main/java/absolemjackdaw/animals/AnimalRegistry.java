package absolemjackdaw.animals;

import absolemjackdaw.capability.CowData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum AnimalRegistry {
    INSTANCE;

    private final Map<ResourceLocation, AnimalCurse> animals = new HashMap<>();

    public final void register(AnimalCurse animal, ResourceLocation identifier) {
        if (identifier == null || identifier.getPath().isBlank() || animal == null)
            throw new IllegalStateException("Cannot register animal without a valid animal or resource location !");
        if (identifier.getNamespace().equals("minecraft"))
            throw new ResourceLocationException("Cannot register animal under the minecraft namespace ! please use your mod's namespace");

        animals.put(identifier, animal);
    }

    public Optional<AnimalCurse> get(ResourceLocation identifier) {
        if (animals.containsKey(identifier) && animals.get(identifier) != null)
            return Optional.of(animals.get(identifier));
        return Optional.empty();
    }
}
