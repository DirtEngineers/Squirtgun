package net.dirtengineers.squirtgun.client.render;

import net.dirtengineers.squirtgun.Constants;
import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.entity.SquirtSlug;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SquirtSlugRenderer extends ArrowRenderer<SquirtSlug> {
    public SquirtSlugRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    public static final ResourceLocation SQUIRT_SLUG_TEXTURE = new ResourceLocation( Squirtgun.MOD_ID, Constants.slugTextureLocation);

    @Override
    public ResourceLocation getTextureLocation(SquirtSlug pEntity) {
        return SQUIRT_SLUG_TEXTURE;
    }
}
