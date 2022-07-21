package absolemjackdaw.capability;

import absolemjackdaw.CowSimulator;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CowCapability implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation KEY = new ResourceLocation(CowSimulator.MODID, "cow_simulator_cap");
    public static Capability<CowData> CAPABILITY = CapabilityManager.get(new CapabilityToken<CowData>() {
    });
    private final CowData data;

    public CowCapability(ServerPlayer player) {
        data = new CowData(player);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == CowCapability.CAPABILITY ?
                (LazyOptional<T>) LazyOptional.of(this::getImpl) :
                LazyOptional.empty();
    }

    private CowData getImpl() {

        return data;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return ICapabilitySerializable.super.getCapability(cap);
    }

    @Override
    public CompoundTag serializeNBT() {
        return data.write();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        data.read(nbt);
    }
}
