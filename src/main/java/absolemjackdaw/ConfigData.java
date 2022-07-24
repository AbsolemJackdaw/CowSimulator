package absolemjackdaw;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigData {

    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;
    public static int exhaustion = -1;

    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static void refreshServer() {

        exhaustion = SERVER.exhaustion.get();

    }

    public static class ServerConfig {

        public final ForgeConfigSpec.IntValue exhaustion;

        ServerConfig(ForgeConfigSpec.Builder builder) {

            builder.push("general");
            exhaustion = builder.
                    comment("Additional Cow Exhaustion. Cows exhaust faster, so they need to eat more regularly. Value is chance per tick. 0 is every tick. -1 disables extra exhaust.").
                    defineInRange("exhaustion", 600, -1, 24000);
            builder.pop();
        }
    }
}
