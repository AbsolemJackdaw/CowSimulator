package absolemjackdaw.events.cow;

import absolemjackdaw.ConfigData;
import absolemjackdaw.Constants;
import absolemjackdaw.CowSimulator;
import absolemjackdaw.capability.CowData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EatGrassEvent {

    @SubscribeEvent
    public static void eat(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        CowData.get(player).ifPresent(cowData -> {
            if (cowData.isServerAnimal(player) && cowData.is(Constants.cowAnimal)) {
                if (player instanceof ServerPlayer serverPlayer) {
                    if (serverPlayer.gameMode.isDestroyingBlock) {
                        cowData.eat();
                    } else if (cowData.isEating())
                        cowData.resetEating();
                    //exhaust cows faster so they eat more
                    if (ConfigData.exhaustion >= 0 && serverPlayer.level.random.nextInt(ConfigData.exhaustion) == 0) {
                        serverPlayer.getFoodData().setFoodLevel(Math.max(0, serverPlayer.getFoodData().getFoodLevel() - 1));
                        serverPlayer.getFoodData().setSaturation(Math.max(0, serverPlayer.getFoodData().getSaturationLevel() - 1));
                    }
                }
            }
            //update eating counter instead of syncing it with a packet every tick
            //only needs to count when the client is notified/synced that we're effectively eating on the server
            else if (cowData.isClientAnimal(player) && cowData.isEating()) {
                cowData.eat(); //eat syncs to client if we're on the server, which isn't the case here, so it will fail to pass the server check and not send a packet
            }
        });
    }

    @SubscribeEvent
    public static void eat(BlockEvent.BreakEvent event) { //fired before block is broken.
        Player player = event.getPlayer();
        CowData.get(player).ifPresent(cowData -> {
            if (cowData.isServerAnimal(player) && !player.isCreative() && cowData.is(Constants.cowAnimal)) {
                event.setCanceled(true);
                if (canEat(event.getState().getBlock())) {
                    ItemStack food = cowData.getFoodFor(event.getState().getBlock());
                    player.getFoodData().eat(food.getItem(), food, player);
                    player.level.setBlockAndUpdate(event.getPos(), cowData.replaceBlock(event.getState().getBlock()).defaultBlockState());
                    if (player.getFoodData().getFoodLevel() >= 20)
                        cowData.intFlag++;
                    if (cowData.intFlag > 10) {
                        cowData.intFlag = 0;
                        player.level.addFreshEntity(new ItemEntity(player.level,
                                player.blockPosition().getX(),
                                player.blockPosition().getY(),
                                player.blockPosition().getZ(),
                                new ItemStack(Items.BONE_MEAL, 1 + player.level.random.nextInt(0, 2))));
                    }
                }
            }
        });
    }

    private static boolean canEat(Block block) {
        return block instanceof GrassBlock
                || block instanceof DoublePlantBlock
                || block instanceof TallGrassBlock
                || block instanceof FlowerBlock
                || block instanceof SeagrassBlock
                || block instanceof MossBlock
                || block instanceof CropBlock;
    }

    @SubscribeEvent
    public static void eat(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        CowData.get(player).ifPresent(cowData -> {
            if (cowData.is(Constants.cowAnimal)) {
                event.setNewSpeed(canEat(event.getState().getBlock()) ? 0.5f : 0.0f);
            }
        });
    }
}
