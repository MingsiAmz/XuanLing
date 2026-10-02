package com.mingsiamz.xuanlingmod.render;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.entities.SkillEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SkillEntityRender extends EntityRenderer<SkillEntity> {

        public ItemRenderer itemRenderer;

        public SkillEntityRender(Context context) {
                super(context);
                this.itemRenderer = context.getItemRenderer();
        }

        @Override
        public void render(SkillEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                        MultiBufferSource bufferSource, int packedLight) {
                super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
                poseStack.pushPose();

                float floatOffset = (float) Math.sin((entity.tickCount + partialTick) * 0.1)
                                * 0.05f;
                poseStack.translate(0, floatOffset, 0);

                poseStack.mulPose(Axis.YP.rotationDegrees(-45 - entity.getYRot()));
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                poseStack.scale(2.0F, 2.0F, 2.0F);

                itemRenderer.renderStatic(Items.DIAMOND_SWORD.getDefaultInstance(), ItemDisplayContext.GROUND,
                                packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), 1);

                poseStack.popPose();

        }

        @Override
        public ResourceLocation getTextureLocation(SkillEntity entity) {
                return TextureAtlas.LOCATION_BLOCKS;
        }

}
