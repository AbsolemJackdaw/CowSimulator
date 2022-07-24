package absolemjackdaw.item;

import absolemjackdaw.capability.CowData;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CowPotion extends Potion {

    public CowPotion() {
        super("cow_potion", new MobEffectInstance(
                RegisterItem.effect.get(),
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

    public static class CowEffect extends MobEffect {
        public CowEffect() {
            super(MobEffectCategory.NEUTRAL, 0x654321);
        }

        @Override
        public void applyInstantenousEffect(@Nullable Entity entity, @Nullable Entity otherEntity, LivingEntity entityApliedTo, int i, double d) {
            if (entityApliedTo instanceof Player player) {
                CowData.get(player).ifPresent(cowData -> {
                    if (!cowData.isClientCow(player)) { //is not cow and is not on client
                        cowData.turnCow(true);
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
