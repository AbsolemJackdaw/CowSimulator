package absolemjackdaw.animals;

import absolemjackdaw.capability.CowData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class AnimalChanger {

    private final ResourceLocation texture;

    public AnimalChanger(ResourceLocation texture) {
        this.texture = texture;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public void render(CowData data, Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        boolean shouldSit = player.isPassenger() && (player.getVehicle() != null && player.getVehicle().shouldRiderSit());
        if (player.isSleeping())
            player.yBodyRot = player.yBodyRotO = 180;

        float bodyLerpY = Mth.rotLerp(partialTick, player.yBodyRotO, player.yBodyRot);
        float headLerpY = Mth.rotLerp(partialTick, player.yHeadRotO, player.yHeadRot);
        float lerpY = headLerpY - bodyLerpY;

        if (shouldSit && player.getVehicle() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) player.getVehicle();
            bodyLerpY = Mth.rotLerp(partialTick, livingentity.yBodyRotO, livingentity.yBodyRot);
            lerpY = headLerpY - bodyLerpY;
            float angle = Mth.wrapDegrees(lerpY);
            if (angle < -85.0F) {
                angle = -85.0F;
            }

            if (angle >= 85.0F) {
                angle = 85.0F;
            }

            bodyLerpY = headLerpY - angle;
            if (angle * angle > 2500.0F) {
                bodyLerpY += angle * 0.2F;
            }

            lerpY = headLerpY - bodyLerpY;
        }

        float lerpX = Mth.lerp(partialTick, player.xRotO, player.getXRot());
        poseStack.translate(0, 1.5f, 0);
        poseStack.mulPose(new Quaternion(180, 0, 0, true));
        poseStack.mulPose(new Quaternion(0, bodyLerpY, 0, true));
        renderSpecific(data, player, renderer, partialTick, poseStack, multiBufferSource, packedLight, lerpX, lerpY, bodyLerpY);
    }

    public abstract AgeableListModel<AbstractClientPlayer> getModel();

    public abstract void renderSpecific(CowData data, Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float lerpedX, float lerpedY, float lerpedBodyY);

    public abstract void renderBody(CowData data, InteractionHand hand, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float partialTick, float interpolatedPitch, float swingProgress, float equipProgress, ItemStack stack);
}
