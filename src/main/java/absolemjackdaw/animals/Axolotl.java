package absolemjackdaw.animals;

import absolemjackdaw.capability.CowData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public class Axolotl extends AnimalCurse {
    public Axolotl() {
        super(new ResourceLocation(String.format(Locale.ROOT, "textures/entity/axolotl/axolotl_%s.png", net.minecraft.world.entity.animal.axolotl.Axolotl.Variant.LUCY)));
    }

    @Override
    public void render(CowData data, Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {

    }

    @Override
    public void renderBody(CowData data, InteractionHand hand, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float partialTick, float interpolatedPitch, float swingProgress, float equipProgress, ItemStack stack) {

    }
}
