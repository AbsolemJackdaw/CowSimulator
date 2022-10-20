package absolemjackdaw.events.axolotl;

import absolemjackdaw.Constants;
import absolemjackdaw.CowSimulator;
import absolemjackdaw.capability.CowData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BedBlock;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AxolotlCantDoAlotl {

    @SubscribeEvent
    public static void doNotMove(TickEvent.PlayerTickEvent event) {
        CowData.get(event.player).ifPresent(cowData -> {

            if (cowData.flag) { //is healing
                event.player.setDeltaMovement(0, 0, 0);
            }
        });
    }

    @SubscribeEvent
    public static void dontUseItems(LivingEntityUseItemEvent.Start event) {
        if (event.getEntity() instanceof Player player) {
            dontDoEvent(player, event);
        }
    }

    public static void dontDoEvent(Player player, Event event) {
        CowData.get(player).ifPresent(cowData -> {
            if (cowData.is(Constants.axolotlAnimal)) {
                event.setCanceled(true);
                if (cowData.isServerAnimal(player))
                    player.level.playSound(
                            null, //null to play on server, player for client
                            player.blockPosition(),
                            SoundEvents.AXOLOTL_IDLE_WATER,
                            SoundSource.NEUTRAL,
                            0.5f + player.level.random.nextFloat() / 2f, 1.0f);
            }
        });
    }

    @SubscribeEvent
    public static void dontUseItems(PlayerInteractEvent.RightClickItem event) {
        dontDoEvent(event.getEntity(), event);
    }

    @SubscribeEvent
    public static void dontUseItems(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity().level.getBlockState(event.getPos()).getBlock() instanceof BedBlock))
            dontDoEvent(event.getEntity(), event);
    }

    @SubscribeEvent
    public static void dontUseItems(PlayerInteractEvent.EntityInteractSpecific event) {
        dontDoEvent(event.getEntity(), event);
    }

    @SubscribeEvent
    public static void dontPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            dontDoEvent(player, event);
        }
    }

    @SubscribeEvent
    public static void dontspar(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            CowData.get(player).ifPresent(cowData -> {
                boolean flag = event.getEntity() instanceof Squid ||
                        event.getEntity() instanceof AbstractFish;

                event.setCanceled(cowData.is(Constants.axolotlAnimal) && !flag);
            });
        }
    }
}
