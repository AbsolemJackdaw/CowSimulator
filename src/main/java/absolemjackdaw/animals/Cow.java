package absolemjackdaw.animals;

import absolemjackdaw.capability.CowData;
import absolemjackdaw.client.ClientSidedCalls;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Cow extends AnimalChanger {
    private CowModel<AbstractClientPlayer> model;
    private static float oldAngle = 0.0f;

    public Cow() {
        super(new ResourceLocation("textures/entity/cow/cow.png"));
    }

    public CowModel<AbstractClientPlayer> getModel() {
        if (model == null)
            model = new CowModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.COW)) {

                @Override
                public void setupAnim(AbstractClientPlayer player, float p_103510_, float p_103511_, float p_103512_, float p_103513_, float p_103514_) {
                    this.rightHindLeg.loadPose(rightHindLeg.getInitialPose());
                    this.leftHindLeg.loadPose(leftHindLeg.getInitialPose());
                    this.rightFrontLeg.loadPose(rightFrontLeg.getInitialPose());
                    this.leftFrontLeg.loadPose(leftFrontLeg.getInitialPose());
                    this.head.loadPose(head.getInitialPose());

                    super.setupAnim(player, p_103510_, p_103511_, p_103512_, p_103513_, p_103514_);

                    if (player.isSleeping()) {
                        this.head.xRot = (float) Math.toRadians(90);
                        this.head.zRot = (float) Math.toRadians(-20);
                        this.head.yRot = (float) Math.toRadians(-15);
                        this.head.setPos(0, 5, -8);

                        this.leftFrontLeg.xRot = (float) Math.toRadians(90.0);
                        this.leftFrontLeg.yRot = (float) Math.toRadians(-100.0);
                        this.leftFrontLeg.setPos(6.0F, 11.9F, -8.0F);

                        this.rightFrontLeg.xRot = (float) Math.toRadians(-90.0);
                        this.rightFrontLeg.yRot = (float) Math.toRadians(-80.0);
                        this.rightFrontLeg.setPos(-6.0F, 12.0F, -8.0F);

                        this.leftHindLeg.setPos(6.0F, 3F, 7.0F);
                        this.leftHindLeg.xRot = (float) Math.toRadians(-25.0);

                        this.rightHindLeg.setPos(-6.0F, 3F, 7.0F);
                        this.rightHindLeg.xRot = (float) Math.toRadians(-25.0);
                    }
                }
            };
        return model;
    }

    @Override
    public void render(CowData data, Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        super.render(data, player, renderer, partialTick, poseStack, multiBufferSource, packedLight);
    }

    @Override
    public void renderSpecific(CowData cowData, Player player, PlayerRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float headX, float headY, float bodyLerpY) {
        if (player.isSleeping()) {
            if (player.hasPose(Pose.SLEEPING)) {
                Direction direction = player.getBedOrientation();
                float sleepDir = direction != null ? sleepDirectionToRotation(direction) : bodyLerpY;
                poseStack.mulPose(Vector3f.XP.rotationDegrees(00F));
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(00F));
                poseStack.mulPose(Vector3f.YP.rotationDegrees(sleepDir));
                poseStack.translate(0f, 0.75f, 0.5f);
            }
        }
        getModel().getHead().visible = true;
        getModel().getHead().y = 2.0f;
        renderCow(cowData, poseStack, multiBufferSource, packedLight, headX, headY, partialTick);
    }

    private static float sleepDirectionToRotation(Direction p_115329_) {
        return switch (p_115329_) {
            case SOUTH -> 180.0F;
            case NORTH -> 0.0F;
            case EAST -> 90.0f;
            default -> 270.0F;
        };
    }

    @Override
    public void renderBody(CowData cowData, InteractionHand hand, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float partialTick, float interpolatedPitch, float swingProgress, float equipProgress, ItemStack stack) {
        poseStack.translate(0, -0.7, -0.2);
        poseStack.mulPose(new Quaternion(0, 0, 180, true));
        poseStack.mulPose(new Quaternion(ClientSidedCalls.getClientPlayer().getXRot() * -1, 0, 0, true));
        if (getModel() != null)
            getModel().getHead().visible = false;
        renderCow(cowData, poseStack, multiBufferSource, packedLight, 0, 0, partialTick);

    }

    private void renderCow(CowData cowData, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float headX, float headY, float partialTick) {
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
