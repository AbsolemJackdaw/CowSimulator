package absolemjackdaw.events.cow;

import absolemjackdaw.CowApi;
import absolemjackdaw.CowSimulator;
import absolemjackdaw.capability.CowData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BedBlock;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CowsAreDumbEvent {

    @SubscribeEvent
    public static void dontUseItems(LivingEntityUseItemEvent.Start event) {
        if (event.getEntity() instanceof Player player) {
            dontDoEvent(player, event);
        }
    }

    public static void dontDoEvent(Player player, Event event) {
        CowData.get(player).ifPresent(cowData -> {
            if (cowData.is(CowApi.cowAnimal)) {
                event.setCanceled(true);
                if (cowData.isServerAnimal(player))
                    player.level.playSound(
                            null, //null to play on server, player for client
                            player.blockPosition(),
                            SoundEvents.COW_AMBIENT,
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
            CowData.get(player).ifPresent(cowData -> event.setCanceled(cowData.is(CowApi.cowAnimal)));
        }
    }
}
