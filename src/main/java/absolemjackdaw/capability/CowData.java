package absolemjackdaw.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;

public class CowData {

    public boolean isCow = false;
    public BlockPos eatingAt = null;

    public int eating = 0;

    public static LazyOptional<CowData> get(Player player) {

        return player.getCapability(CowCapability.CAPABILITY, null);
    }

    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("isCow", isCow);
        return nbt;
    }

    public void read(CompoundTag nbt) {
        this.isCow = nbt.getBoolean("isCow");
    }
}
