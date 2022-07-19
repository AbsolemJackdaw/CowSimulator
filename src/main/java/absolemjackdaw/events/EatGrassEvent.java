package absolemjackdaw.events;

import absolemjackdaw.CowSimulator;
import absolemjackdaw.capability.CowData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EatGrassEvent {
    private static final ItemStack food = new ItemStack(Items.COOKIE);

    @SubscribeEvent
    public static void eat(PlayerEvent.BreakSpeed event) {
        CowData.get(event.getEntity()).ifPresent(cowData -> {
            if (cowData.isCow) {
                event.setNewSpeed(0.0f);
                if (event.getEntity().getLevel().getBlockState(event.getPos()).getBlock() instanceof GrassBlock) {
                    cowData.eatingAt = event.getPos();
                    event.setNewSpeed(0.05f);
                } else {
                    cowData.eating = 0;
                }
            }
        });
    }

    @SubscribeEvent
    public static void eat(PlayerEvent.HarvestCheck event) {
        CowData.get(event.getEntity()).ifPresent(cowData -> {
            if (cowData.isCow) {
                cowData.eating++;
                if (cowData.eating > 100) {
                    if (event.getTargetBlock().getBlock() instanceof GrassBlock && cowData.eatingAt != null) {
                        event.getEntity().getFoodData().eat(food.getItem(), food, event.getEntity());
                        event.getEntity().level.setBlockAndUpdate(cowData.eatingAt, Blocks.DIRT.defaultBlockState());
                        cowData.eatingAt = null;
                        event.setCanHarvest(false);
                        cowData.eating = 0;
                    }
                }
            }
        });
    }
}
