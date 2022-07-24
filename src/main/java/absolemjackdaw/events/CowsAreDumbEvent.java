package absolemjackdaw.events;

import absolemjackdaw.CowSimulator;
import absolemjackdaw.capability.CowData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BedBlock;
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
            if (cowData.isServerCow(player) || cowData.isClientCow(player)) {
                event.setCanceled(true);
                if (cowData.isServerCow(player))
                    player.level.playSound(
                            player instanceof ServerPlayer ? null : player, //null to play on server, player for client
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
}
