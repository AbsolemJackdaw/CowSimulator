package absolemjackdaw.animals;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum AnimalRegistry {
    INSTANCE;

    private final Map<ResourceLocation, AnimalChanger> animals = new HashMap<>();

    public final void register(AnimalChanger animal, ResourceLocation identifier) {
        if (identifier == null || identifier.getPath().isBlank() || animal == null)
            throw new IllegalStateException("Cannot register animal without a valid animal or resource location !");
        if (identifier.getNamespace().equals("minecraft"))
            throw new ResourceLocationException("Cannot register animal under the minecraft namespace ! please use your mod's namespace");

        animals.put(identifier, animal);
    }

    public Optional<AnimalChanger> get(ResourceLocation identifier) {
        if (animals.containsKey(identifier) && animals.get(identifier) != null)
            return Optional.of(animals.get(identifier));
        return Optional.empty();
    }
}
