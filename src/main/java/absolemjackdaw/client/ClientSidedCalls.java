package absolemjackdaw.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientSidedCalls {
    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
