package absolemjackdaw.events;

import absolemjackdaw.CowSimulator;
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
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CowSimulator.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SkipPlayerRendering {

    private static final ResourceLocation COW_LOCATION = new ResourceLocation("textures/entity/cow/cow.png");
    protected static CowModel<AbstractClientPlayer> cowModel;

    @SubscribeEvent
    public static void skip(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        CowData.get(player).ifPresent(cowData -> {
            if (cowData.isClientCow(event.getEntity())) {
                event.setCanceled(true); //cancel complete rendering and render only a cow instead
                event.getPoseStack().pushPose();
                boolean shouldSit = player.isPassenger() && (player.getVehicle() != null && player.getVehicle().shouldRiderSit());
                if (player.isSleeping())
                    player.yBodyRot = player.yBodyRotO = 180;

                float f = Mth.rotLerp(event.getPartialTick(), player.yBodyRotO, player.yBodyRot);
                float f1 = Mth.rotLerp(event.getPartialTick(), player.yHeadRotO, player.yHeadRot);
                float headY = f1 - f;

                if (shouldSit && player.getVehicle() instanceof LivingEntity) {
                    LivingEntity livingentity = (LivingEntity) player.getVehicle();
                    f = Mth.rotLerp(event.getPartialTick(), livingentity.yBodyRotO, livingentity.yBodyRot);
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

                if (player.isSleeping()) {
                    if (player.hasPose(Pose.SLEEPING)) {
                        Direction direction = player.getBedOrientation();
                        float sleepDir = direction != null ? sleepDirectionToRotation(direction) : f;
                        event.getPoseStack().mulPose(Vector3f.XP.rotationDegrees(sleepDir));
                        event.getPoseStack().mulPose(Vector3f.ZP.rotationDegrees(90F));
                        event.getPoseStack().mulPose(Vector3f.YP.rotationDegrees(270.0F));
                        event.getPoseStack().translate(-0.3f, -1f, 0.5f);
                    }
                }

                float headX = Mth.lerp(event.getPartialTick(), player.xRotO, player.getXRot());
                cowModel.getHead().visible = true;
                event.getPoseStack().translate(0, 1.5f, 0);
                event.getPoseStack().mulPose(new Quaternion(180, 0, 0, true));
                event.getPoseStack().mulPose(new Quaternion(0, f, 0, true));
                cowModel.getHead().y = 2.0f;
                renderCow(cowData, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), headX, headY, event.getPartialTick());
                event.getPoseStack().popPose();
            }
        });
    }

    private static float sleepDirectionToRotation(Direction p_115329_) {
        switch (p_115329_) {
            case SOUTH:
                return 90.0F;
            case WEST:
                return 0.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            default:
                return 0.0F;
        }
    }

    @SubscribeEvent
    public static void skip(RenderHandEvent event) {
        CowData.get(ClientSidedCalls.getClientPlayer()).ifPresent(cowData -> {
            if (cowData.isClientCow(ClientSidedCalls.getClientPlayer())) {
                event.setCanceled(true);
                event.getPoseStack().pushPose();
                event.getPoseStack().translate(0, -0.7, -0.2);
                event.getPoseStack().mulPose(new Quaternion(0, 0, 180, true));
                event.getPoseStack().mulPose(new Quaternion(ClientSidedCalls.getClientPlayer().getXRot() * -1, 0, 0, true));
                if (cowModel != null)
                    cowModel.getHead().visible = false;
                renderCow(cowData, event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), 0, 0, event.getPartialTick());
                event.getPoseStack().popPose();

            }
        });
    }

    private static float oldAngle = 0.0f;

    private static void renderCow(CowData cowData, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float headX, float headY, float partialTick) {
        if (cowModel == null)
            cowModel = new CowModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.COW)) {
                @Override
                public void setupAnim(AbstractClientPlayer player, float p_103510_, float p_103511_, float p_103512_, float p_103513_, float p_103514_) {
                    super.setupAnim(player, p_103510_, p_103511_, p_103512_, p_103513_, p_103514_);
                    if (player.isSleeping())
                        cowModel.getHead().xRot = cowModel.getHead().yRot = 0;
                }
            };
        cowModel.young = false;
        float limbSwingPower = Mth.lerp(partialTick, ClientSidedCalls.getClientPlayer().animationSpeedOld, ClientSidedCalls.getClientPlayer().animationSpeed);
        float limbSwing = ClientSidedCalls.getClientPlayer().animationPosition;
        if (limbSwingPower > 1.0F) {
            limbSwingPower = 1.0F;
        }
        cowModel.setupAnim((AbstractClientPlayer) ClientSidedCalls.getClientPlayer(), limbSwing, limbSwingPower, 0, headY, headX);
        if (cowData.isEating()) {
            cowModel.getHead().y = cowData.getHeadEatPositionScale(partialTick);
            if (oldAngle == 0)
                oldAngle = cowModel.getHead().xRot;
            cowModel.getHead().xRot = Mth.lerp(partialTick, oldAngle, cowData.getHeadEatAngleScale(ClientSidedCalls.getClientPlayer(), partialTick));
            oldAngle = cowModel.getHead().xRot;

        }
        cowModel.renderToBuffer(
                poseStack,
                buffer.getBuffer(RenderType.entityCutout(COW_LOCATION)),
                packedLight,
                LivingEntityRenderer.getOverlayCoords(ClientSidedCalls.getClientPlayer(), 0.0F),
                1f, 1f, 1f, 1f);
    }
}
