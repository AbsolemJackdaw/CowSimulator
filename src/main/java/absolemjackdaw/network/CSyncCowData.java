package absolemjackdaw.network;

import absolemjackdaw.capability.CowData;
import absolemjackdaw.client.ClientSidedCalls;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CSyncCowData {
    private ResourceLocation animal = null;
    private Integer eating = null;

    public CSyncCowData() {
    }

    public CSyncCowData(FriendlyByteBuf read) {
        decode(read);
    }

    public void decode(FriendlyByteBuf buf) {
        int ord = buf.readInt();
        STATUS status = STATUS.values()[ord];
        if (status == STATUS.INT || status == STATUS.BOTH) {
            eating = buf.readInt();
        }
        if (status == STATUS.TYPE || status == STATUS.BOTH) {
            animal = buf.readResourceLocation();
        }
    }

    public CSyncCowData with(ResourceLocation animal) {
        this.animal = animal;
        return this;
    }

    public CSyncCowData with(int eating) {
        this.eating = eating;
        return this;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(STATUS.get(animal, eating).ordinal());
        if (animal != null)
            buffer.writeResourceLocation(animal);
        if (eating != null)
            buffer.writeInt(eating);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = ClientSidedCalls.getClientPlayer();
            CowData.get(player).ifPresent(cowData -> {
                if (animal != null)
                    cowData.becomeAnimal(animal);
                if (eating != null)
                    cowData.setClientEating(eating);
            });
        });
        context.get().setPacketHandled(true);
    }

    private enum STATUS {
        BOTH,
        TYPE,
        INT,
        NONE;

        public static STATUS get(ResourceLocation b, Integer i) {
            return b != null && i != null ? BOTH : b != null ? TYPE : i != null ? INT : NONE;
        }
    }
}