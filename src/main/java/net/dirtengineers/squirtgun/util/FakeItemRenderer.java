package net.dirtengineers.squirtgun.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.smashingmods.chemlib.common.items.ChemicalItem;
import com.smashingmods.chemlib.common.items.ElementItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FakeItemRenderer {
    private static final Minecraft MINECRAFT = Minecraft.getInstance();
    private static final ItemRenderer ITEM_RENDERER;
    private static final TextureManager TEXTURE_MANAGER;

    public FakeItemRenderer() {
    }

    public static void renderFakeItem(ItemStack pItemStack, int pX, int pY, int pSize) {
        renderFakeItem(pItemStack, pX, pY, pSize, 1.0F);
    }

    public static void renderFakeItem(ItemStack pItemStack, int pX, int pY, int pSize, float pAlpha) {
        if (!pItemStack.isEmpty()) {
            BakedModel model = getBakedModel(pItemStack);
            TEXTURE_MANAGER.getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(true, false);
            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.enableBlend();
            PoseStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushPose();
            modelViewStack.translate((double)pX + 8.0, (double)pY + 8.0, 0.0);
            modelViewStack.scale(pSize, -pSize, pSize);
            RenderSystem.applyModelViewMatrix();
            if (!model.usesBlockLight()) {
                Lighting.setupForFlatItems();
            }

            MultiBufferSource.BufferSource bufferSource = MINECRAFT.renderBuffers().bufferSource();
            ITEM_RENDERER.render(pItemStack, ItemTransforms.TransformType.GUI, false, new PoseStack(), getWrappedBuffer(bufferSource, pAlpha), 15728880, OverlayTexture.NO_OVERLAY, model);
            bufferSource.endBatch();
            RenderSystem.enableDepthTest();
            if (!model.usesBlockLight()) {
                Lighting.setupFor3DItems();
            }
            modelViewStack.popPose();
            RenderSystem.applyModelViewMatrix();
        }
    }

    private static MultiBufferSource getWrappedBuffer(MultiBufferSource pBufferSource, float pAlpha) {
        return (pRenderType) -> {
            return new WrappedVertexConsumer(pBufferSource.getBuffer(RenderType.entityTranslucent(InventoryMenu.BLOCK_ATLAS)), 1.0F, 1.0F, 1.0F, pAlpha);
        };
    }

    private static BakedModel getBakedModel(ItemStack pItemStack) {
        ModelResourceLocation chemicalModel = getChemicalModel(pItemStack);
        return chemicalModel != null
                ? ITEM_RENDERER.getItemModelShaper().getModelManager().getModel(chemicalModel)
                : ITEM_RENDERER.getModel(pItemStack, null, MINECRAFT.player, 0);
    }

    private static @Nullable ModelResourceLocation getChemicalModel(ItemStack pItemStack) {
        ModelResourceLocation modelResourceLocation = null;
        Item var4 = pItemStack.getItem();
        if (var4 instanceof ElementItem elementItem) {
            modelResourceLocation = switch (elementItem.getMatterState()) {
                case LIQUID ->
                        new ModelResourceLocation(new ResourceLocation("chemlib", "element_liquid_model"), "inventory");
                case GAS ->
                        new ModelResourceLocation(new ResourceLocation("chemlib", "element_gas_model"), "inventory");
                default ->
                        new ModelResourceLocation(new ResourceLocation("chemlib", "element_solid_model"), "inventory");
            };
        } else {
            var4 = pItemStack.getItem();
            if (var4 instanceof ChemicalItem chemicalItem) {
                modelResourceLocation = switch (chemicalItem.getItemType()) {
                    case DUST ->
                            new ModelResourceLocation(new ResourceLocation("chemlib", "chemical_dust_model"), "inventory");
                    case NUGGET ->
                            new ModelResourceLocation(new ResourceLocation("chemlib", "chemical_nugget_model"), "inventory");
                    case INGOT ->
                            new ModelResourceLocation(new ResourceLocation("chemlib", "chemical_ingot_model"), "inventory");
                    case PLATE ->
                            new ModelResourceLocation(new ResourceLocation("chemlib", "chemical_plate_model"), "inventory");
                    default -> modelResourceLocation;
                };
            }
        }

        return modelResourceLocation;
    }

    static {
        ITEM_RENDERER = MINECRAFT.getItemRenderer();
        TEXTURE_MANAGER = MINECRAFT.getTextureManager();
    }
}
