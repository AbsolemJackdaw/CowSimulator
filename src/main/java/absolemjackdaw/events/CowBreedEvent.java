package absolemjackdaw.events;

import absolemjackdaw.CowSimulator;
import absolemjackdaw.capability.CowData;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CowBreedEvent {

    @SubscribeEvent
    public static void interact(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        if (event.getTarget() instanceof Cow cow && event.getHand().equals(InteractionHand.MAIN_HAND)) {
            CowData.get(player).ifPresent(cowData -> {
                if (cowData.isServerCow(player)) { //is not cow and is not client
                    if (!cow.isInLove())
                        cow.setInLove(player);
                }
            });
        }
    }

    @SubscribeEvent
    public static void breed(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        CowData.get(player).ifPresent(cowData -> {
            if (player instanceof ServerPlayer serverPlayer && cowData.isServerCow(player)) {
                List<Cow> mates = player.level.getEntitiesOfClass(Cow.class, player.getBoundingBox());
                if (!mates.isEmpty())
                    for (Cow mate : mates)
                        if (mate.isInLove()) {
                            spawnChildFromBreeding(serverPlayer.getLevel(), serverPlayer, mate);
                        }
            }
        });
    }

    private static void spawnChildFromBreeding(ServerLevel level, ServerPlayer player, Cow parent) {
        AgeableMob baby = parent.getBreedOffspring(level, parent);
        final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(parent, parent, baby);
        final boolean cancelled = net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        baby = event.getChild();
        if (cancelled) {
            parent.setAge(6000);
            parent.resetLove();
            return;
        }
        if (baby != null && player != null) {
            player.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(player, parent, parent, baby);

            parent.setAge(6000);
            parent.resetLove();
            baby.setBaby(true);
            baby.moveTo(player.getX(), player.getY(), player.getZ(), 0.0F, 0.0F);
            level.addFreshEntityWithPassengers(baby);
            level.broadcastEntityEvent(player, (byte) 18);
            if (level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                level.addFreshEntity(new ExperienceOrb(level, player.getX(), player.getY(), player.getZ(), player.getRandom().nextInt(7) + 1));
            }
        }
    }
}
