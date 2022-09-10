package absolemjackdaw.item;

import absolemjackdaw.capability.CowData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class AnimalPotion extends Potion {
    public AnimalPotion(String name, Supplier<MobEffect> effect) {
        super(name, new MobEffectInstance(
                effect.get(),
                0,
                0,
                false,
                false,
                false,
                null,
                Optional.empty()
        ));
    }

    @Override
    public boolean hasInstantEffects() {
        return true;
    }

    public static class AnimalEffect extends MobEffect {
        private final ResourceLocation animal;

        public AnimalEffect(ResourceLocation animal) {
            super(MobEffectCategory.NEUTRAL, 0x654321);
            this.animal = animal;
        }

        @Override
        public void applyInstantenousEffect(@Nullable Entity entity, @Nullable Entity otherEntity, LivingEntity entityApliedTo, int i, double d) {
            if (entityApliedTo instanceof Player player) {
                CowData.get(player).ifPresent(cowData -> {
                    if (!cowData.isClientAnimal(player)) { //is not cow and is not on client
                        cowData.becomeAnimal(this.animal);
                    }
                });
            }
        }

        @Override
        public boolean isInstantenous() {
            return true;
        }
    }
}
