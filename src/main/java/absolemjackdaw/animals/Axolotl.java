package absolemjackdaw.animals;

import absolemjackdaw.animals.model.PlayerAxolotlModel;
import absolemjackdaw.capability.CowData;
import absolemjackdaw.client.ClientSidedCalls;
import absolemjackdaw.events.axolotl.RegisterModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public class Axolotl extends AnimalCurse {

    private PlayerAxolotlModel<AbstractClientPlayer> model;

    public PlayerAxolotlModel<AbstractClientPlayer> getModel() {
        if (model == null)
            model = new PlayerAxolotlModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(RegisterModel.AXOLOTLLAYER));

        return model;
    }

    public Axolotl() {
        super(new ResourceLocation(String.format(Locale.ROOT, "textures/entity/axolotl/axolotl_%s.png", net.minecraft.world.entity.animal.axolotl.Axolotl.Variant.LUCY.toString().toLowerCase())));
    }

    @Override
    public void render(CowData data, Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
//        poseStack.mulPose(new Quaternion(0f, 0f, 180f, true));
//        poseStack.translate(0, -17.5F, 0);

        boolean shouldSit = player.isPassenger() && (player.getVehicle() != null && player.getVehicle().shouldRiderSit());
        if (player.isSleeping())
            player.yBodyRot = player.yBodyRotO = 180;

        float f = Mth.rotLerp(partialTick, player.yBodyRotO, player.yBodyRot);
        float f1 = Mth.rotLerp(partialTick, player.yHeadRotO, player.yHeadRot);
        float headY = f1 - f;

        if (shouldSit && player.getVehicle() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity) player.getVehicle();
            f = Mth.rotLerp(partialTick, livingentity.yBodyRotO, livingentity.yBodyRot);
            headY = f1 - f;
            float f3 = Mth.wrapDegrees(headY);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            headY = f1 - f;
        }

        float headX = Mth.lerp(partialTick, player.xRotO, player.getXRot());
        getModel().getHead().visible = true;
        poseStack.translate(0, 1.5f, 0);
        poseStack.mulPose(new Quaternion(180, 0, 0, true));
        poseStack.mulPose(new Quaternion(0, f, 0, true));
        renderAxolotl(data, poseStack, multiBufferSource, packedLight, headX, headY, partialTick);

    }

    @Override
    public void renderBody(CowData data, InteractionHand hand, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float partialTick, float interpolatedPitch, float swingProgress, float equipProgress, ItemStack stack) {
        poseStack.translate(0, -0.4, 1.0);
        poseStack.mulPose(new Quaternion(0, 0, 180, true));
        poseStack.mulPose(new Quaternion(ClientSidedCalls.getClientPlayer().getXRot() * -1, 0, 0, true));
        if (getModel() != null)
            getModel().getHead().visible = false;
        renderAxolotl(data, poseStack, multiBufferSource, packedLight, 0, 0, partialTick);

    }

    private static float oldAngle = 0.0f;

    private void renderAxolotl(CowData cowData, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float headX, float headY, float partialTick) {
        getModel().young = false;
        float limbSwingPower = Mth.lerp(partialTick, ClientSidedCalls.getClientPlayer().animationSpeedOld, ClientSidedCalls.getClientPlayer().animationSpeed);
        float limbSwing = ClientSidedCalls.getClientPlayer().animationPosition;
        if (limbSwingPower > 1.0F) {
            limbSwingPower = 1.0F;
        }
        getModel().setupAnim((AbstractClientPlayer) ClientSidedCalls.getClientPlayer(), limbSwing, limbSwingPower, 0, headY, headX);
        if (cowData.isEating()) {
            getModel().getHead().y = cowData.getHeadEatPositionScale(partialTick);
            if (oldAngle == 0)
                oldAngle = getModel().getHead().xRot;
            getModel().getHead().xRot = Mth.lerp(partialTick, oldAngle, cowData.getHeadEatAngleScale(ClientSidedCalls.getClientPlayer(), partialTick));
            oldAngle = getModel().getHead().xRot;

        }
        getModel().renderToBuffer(
                poseStack,
                buffer.getBuffer(RenderType.entityCutout(getTexture())),
                packedLight,
                LivingEntityRenderer.getOverlayCoords(ClientSidedCalls.getClientPlayer(), 0.0F),
                1f, 1f, 1f, 1f);
    }
}
