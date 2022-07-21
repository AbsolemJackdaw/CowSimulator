package absolemjackdaw.network;

import absolemjackdaw.CowSimulator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class CowNetwork {
    private static final String PROTOCOL = "1.0.0";
    public static final SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(CowSimulator.MODID, "network"))
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .networkProtocolVersion(() -> PROTOCOL)
            .simpleChannel();

    public static void init() {
        int id = 0;
        CowNetwork.NETWORK.registerMessage(id++, CSyncCowData.class, CSyncCowData::encode, CSyncCowData::new, CSyncCowData::handle);

    }
}
