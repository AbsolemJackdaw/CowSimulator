//package absolemjackdaw.client;
//
//import absolemjackdaw.capability.CowData;
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.model.CowModel;
//import net.minecraft.client.model.PlayerModel;
//import net.minecraft.client.model.geom.ModelLayers;
//import net.minecraft.client.player.AbstractClientPlayer;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.entity.RenderLayerParent;
//import net.minecraft.client.renderer.entity.layers.RenderLayer;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.resources.ResourceLocation;
//import org.jetbrains.annotations.NotNull;
//
//import javax.annotation.Nonnull;
//
//public class CowLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
//    private static final ResourceLocation COW_LOCATION = new ResourceLocation("textures/entity/cow/cow.png");
//    protected CowModel<AbstractClientPlayer> cowModel;
//
//    public CowLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
//        super(renderer);
//        cowModel = new CowModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.COW));
//        cowModel.young = false;
//    }
//
//    @Override
//    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight, @Nonnull AbstractClientPlayer player, float limbSwing, float limbPower, float partialTick, float u, float headY, float headX) {
//        CowData.get(player).ifPresent(cowData -> {
//            if (cowData.isClientCow(player)) {
//                cowModel.young = false;
//                cowModel.setupAnim(player, limbSwing, limbPower, 0, headY, headX);
//                cowModel.getHead().y = 2.0f;
//                if (cowData.isEating()) {
//                    cowModel.getHead().y = cowData.getHeadEatPositionScale(Minecraft.getInstance().getFrameTime());
//                    cowModel.getHead().xRot = cowData.getHeadEatAngleScale(player, Minecraft.getInstance().getFrameTime());
//                }
//                cowModel.renderToBuffer(poseStack, multiBufferSource.getBuffer(RenderType.entityCutout(COW_LOCATION)), packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
//            }
//        });
//    }
//}
