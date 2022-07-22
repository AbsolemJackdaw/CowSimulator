package absolemjackdaw;

import absolemjackdaw.item.RegisterItem;
import absolemjackdaw.network.CowNetwork;
import net.minecraftforge.fml.common.Mod;

@Mod(CowSimulator.MODID)
public class CowSimulator {
    public static final String MODID = "cowsimulator";

    static {
        CowNetwork.init();
        RegisterItem.init();
    }
}
