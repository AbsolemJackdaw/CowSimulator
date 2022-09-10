package absolemjackdaw.capability;

import absolemjackdaw.network.CSyncCowData;
import absolemjackdaw.network.CowNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;

public class CowData {

    private static final ItemStack low = new ItemStack(Items.TROPICAL_FISH, 1);
    private static final ItemStack high = new ItemStack(Items.MELON_SLICE, 1);
    private static final ItemStack highest = new ItemStack(Items.CARROT, 1);
    @Nullable
    private final ServerPlayer serverPlayer;
    private boolean isAnimal = false;
    private int eating = 0;

    private int overAte;
    private final int overAteMax = 10;

    private ResourceLocation animal = new ResourceLocation("");

    public CowData(@Nullable ServerPlayer serverPlayer) {
        this.serverPlayer = serverPlayer;
    }

    public static LazyOptional<CowData> get(Player player) {

        return player.getCapability(CowCapability.CAPABILITY, null);
    }

    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("isCow", isAnimal);
        nbt.putString("animal", animal.toString());
        return nbt;
    }

    public void read(CompoundTag nbt) {
        this.isAnimal = nbt.getBoolean("isCow");
        this.animal = new ResourceLocation(nbt.getString("animal"));
    }

    /**
     * server logic
     */
    public boolean isServerAnimal(Player player) {
        return isAnimal && !player.level.isClientSide();
    }

    public boolean isClientAnimal(Player player) {
        return isAnimal && player.level.isClientSide();
    }

    public void resetEating() {
        eating = 0;
        syncEating();
    }

    private void syncEating() {
        if (serverPlayer != null)
            CowNetwork.NETWORK.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new CSyncCowData().with(eating));
    }

    public void eat() {
        eating++;
        if (eating - 1 == 0) //only sync when eating is updated to 1. client side only has to know whether the cow is eating or not.
            syncEating(); //sync after counting up, the eating variable is passed on to the packet
    }

    public void updateEatSaturation(ServerPlayer player) {
        if (player.getFoodData().getFoodLevel() >= 20)
            overAte++;
    }

    public boolean ateTooMuch() {
        boolean flag = overAte >= overAteMax;
        if (flag)
            overAte = 0;
        return flag;
    }

    public void setClientEating(int eating) {
        this.eating = eating;
    }

    public boolean isEating() {
        return eating > 0;
    }

    public void becomeAnimal(ResourceLocation animal) {
        this.animal = animal;
        syncFlag();
    }

    public ResourceLocation getAnimalIdentifier() {
        return animal;
    }

    private void syncFlag() {
        if (serverPlayer != null)
            CowNetwork.NETWORK.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new CSyncCowData().with(animal));
    }

    public void sync() {
        if (serverPlayer != null)
            CowNetwork.NETWORK.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new CSyncCowData().with(eating).with(animal));
    }

    public float getHeadEatAngleScale(LivingEntity clientPlayer, float partialTicks) { //use livingentity to prevent mishaps on server side with clientplayer
        if (this.eating > 4) {
            float f = ((float) (this.eating - 4) - partialTicks) / 32.0F;
            return ((float) Math.PI / 5F) + 0.22F * Mth.sin(f * 5.7F);
        } else {
            return this.eating > 0 ? ((float) Math.PI / 5F) : clientPlayer.getXRot() * ((float) Math.PI / 180F);
        }
    }

    public float getHeadEatPositionScale(float partialTicks) {
        return Math.min(10F, 2.0F + (((float) eating - partialTicks) * 1.5f));
    }

    public boolean canEat(Block block) {
        return block instanceof GrassBlock
                || block instanceof DoublePlantBlock
                || block instanceof TallGrassBlock
                || block instanceof FlowerBlock
                || block instanceof SeagrassBlock
                || block instanceof MossBlock
                || block instanceof CropBlock;
    }

    public Block replaceBlock(Block block) {
        return block instanceof GrassBlock ? Blocks.DIRT : Blocks.AIR;
    }

    public ItemStack getFoodFor(Block block) {
        return block instanceof GrassBlock ? highest :
                block instanceof DoublePlantBlock || block instanceof MossBlock || block instanceof CropBlock ? high :
                        low;

    }
}
