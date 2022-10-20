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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public class AxolotlChanger extends AnimalChanger {

    private static float oldAngle = 0.0f;
    private PlayerAxolotlModel<AbstractClientPlayer> model;

    public AxolotlChanger() {
        super(new ResourceLocation(String.format(Locale.ROOT, "textures/entity/axolotl/axolotl_%s.png", net.minecraft.world.entity.animal.axolotl.Axolotl.Variant.LUCY.toString().toLowerCase())));
    }

    @Override
    public void renderSpecific(CowData data, Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float lerpedX, float lerpedY, float lerpBodyY) {
        getModel().getHead().visible = true;
        renderAxolotl(data, poseStack, multiBufferSource, packedLight, lerpedX, lerpedY, partialTick);

    }

    public PlayerAxolotlModel<AbstractClientPlayer> getModel() {
        if (model == null)
            model = new PlayerAxolotlModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(RegisterModel.AXOLOTLLAYER));

        return model;
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
                buffer.getBuffer(RenderType.entityTranslucent(getTexture())),
                packedLight,
                LivingEntityRenderer.getOverlayCoords(ClientSidedCalls.getClientPlayer(), 0.0F),
                1f, 1f, 1f, 1f);
    }
}
