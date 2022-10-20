package absolemjackdaw.events.axolotl;

import absolemjackdaw.Constants;
import absolemjackdaw.CowSimulator;
import absolemjackdaw.capability.CowData;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AxolotlDoesInWater {
    public static final AttributeModifier slower = new AttributeModifier(UUID.fromString("5038cdc1-89be-4914-b880-332498d672af"), "axolotl_slow", -0.35D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final AttributeModifier faster = new AttributeModifier(UUID.fromString("6038cdc1-89be-4914-b880-332498d672af"), "axolotl_fast", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final Vec3 hovervec = new Vec3(0, 0, 0);

    @SubscribeEvent
    public static void pickupevent(EntityItemPickupEvent event) {
        CowData.get(event.getEntity()).ifPresent(cowData -> {
            if (event.getEntity().getFoodData().getFoodLevel() < 20) {
                if (event.getItem().getItem().is(ItemTags.FISHES)) {
                    for (int i = 0; i < 2; i++)
                        event.getEntity().getFoodData().eat(event.getItem().getItem().getItem(), event.getItem().getItem(), event.getEntity());
                    event.getItem().remove(Entity.RemovalReason.DISCARDED);
                    event.setResult(Event.Result.ALLOW);
                }
                if (event.getItem().getItem().getItem().equals(Items.INK_SAC)) {
                    event.getEntity().getFoodData().eat(Items.COOKED_BEEF, new ItemStack(Items.COOKED_BEEF, 1), event.getEntity());
                    event.getItem().remove(Entity.RemovalReason.DISCARDED);
                    event.setResult(Event.Result.ALLOW);
                }
            }

        });
    }

    @SubscribeEvent
    public static void handleWaterExisting(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        CowData.get(player).ifPresent(cowData -> {
            if (cowData.is(Constants.axolotlAnimal)) {
                boolean inWater = player.isInWaterRainOrBubble();
                breathInWater(player, inWater);
                //canSee(player, inWater);
                hurtOutside(cowData, player, inWater);
                if (inWater) {
                    goFast(player);
                    hover(player);
                    playDeadToHeal(player, cowData);
                } else {
                    goSlow(player);
                }
            } else {
                resetMovement(player);
            }
        });
    }

    private static void breathInWater(Player player, boolean inWater) {
        if (inWater) player.setAirSupply(0);
        else player.setAirSupply(player.getMaxAirSupply());
    }

    private static void hurtOutside(CowData data, Player player, boolean inWater) {
        if (inWater) {
            data.intFlag2 = 0;
        } else {
            data.intFlag2++;
            if (data.intFlag2 > 5 * 60 * 20) { //5 minutes in ticks
                data.intFlag2 -= 20;
                player.hurt(DamageSource.DRY_OUT, 4.0F);
            }
        }

    }

    private static void goFast(Player player) {
        AttributeInstance swim = player.getAttribute(ForgeMod.SWIM_SPEED.get());
        AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (!swim.hasModifier(faster))
            swim.addTransientModifier(faster);
        if (speed.hasModifier(slower))
            speed.removeModifier(slower);
    }

    private static void hover(Player player) {
        boolean moves = player.getDeltaMovement().horizontalDistanceSqr() > 1.0E-7D || player.getXRot() != player.xRotO || player.getYRot() != player.yRotO || player.xOld != player.getX() || player.zOld != player.getZ();
        if (!moves)
            player.setDeltaMovement(hovervec);
    }

    private static void playDeadToHeal(Player player, CowData data) {
        if (player.getHealth() <= 2) {
            data.flag = true;
        } else if (player.getHealth() >= player.getMaxHealth() - 6)
            data.flag = false;
        if (data.flag && player.getHealth() < player.getMaxHealth() - 6 && player.level.getGameTime() % 40 == 1) {
            player.heal(1.0F);
        }
    }

    private static void goSlow(Player player) {
        AttributeInstance swim = player.getAttribute(ForgeMod.SWIM_SPEED.get());
        AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (!speed.hasModifier(slower)) speed.addTransientModifier(slower);
        if (swim.hasModifier(faster)) swim.removeModifier(faster);
    }

    private static void resetMovement(Player player) {
        AttributeInstance swim = player.getAttribute(ForgeMod.SWIM_SPEED.get());
        AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed == null || swim == null) return;
        if (speed.hasModifier(slower)) speed.removeModifier(slower);
        if (swim.hasModifier(faster)) swim.removeModifier(faster);
    }

    private static void canSee(Player player, boolean inWater) {
        if (inWater) {
            if (!player.hasEffect(MobEffects.NIGHT_VISION) || player.getEffect(MobEffects.NIGHT_VISION).getDuration() < 80)
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 100, 1, false, false, false));
        } else {
            if (player.hasEffect(MobEffects.NIGHT_VISION))
                player.removeEffect(MobEffects.NIGHT_VISION);
        }
    }
}
