package absolemjackdaw.network;

import absolemjackdaw.capability.CowData;
import absolemjackdaw.client.ClientSidedCalls;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CSyncCowData {
    private Boolean isCow = null;
    private Integer eating = null;

    public CSyncCowData() {
    }

    public CSyncCowData(FriendlyByteBuf read) {
        decode(read);
    }

    public void decode(FriendlyByteBuf buf) {
        int ord = buf.readInt();
        STATUS status = STATUS.values()[ord];
        switch (status) {
            case INT -> eating = buf.readInt();
            case BOOL -> isCow = buf.readBoolean();
            case BOTH -> {
                isCow = buf.readBoolean();
                eating = buf.readInt();
            }
        }
    }

    public CSyncCowData with(boolean isCow) {
        this.isCow = isCow;
        return this;
    }

    public CSyncCowData with(int eating) {
        this.eating = eating;
        return this;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(STATUS.get(isCow, eating).ordinal());
        if (isCow != null)
            buffer.writeBoolean(isCow);
        if (eating != null)
            buffer.writeInt(eating);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = ClientSidedCalls.getClientPlayer();
            CowData.get(player).ifPresent(cowData -> {
                if (isCow != null)
                    cowData.setCow(isCow);
                if (eating != null)
                    cowData.setClientEating(eating);
            });
        });
        context.get().setPacketHandled(true);
    }

    private enum STATUS {
        BOTH,
        BOOL,
        INT,
        NONE;

        public static STATUS get(Boolean b, Integer i) {
            return b != null && i != null ? BOTH : b != null ? BOOL : i != null ? INT : NONE;
        }
    }
}