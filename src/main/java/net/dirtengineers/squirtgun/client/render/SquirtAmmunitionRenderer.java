package net.dirtengineers.squirtgun.client.render;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SquirtAmmunitionRenderer extends ArrowRenderer<SquirtSlug> {
    public SquirtAmmunitionRenderer(EntityRendererProvider.Context pContext) {super(pContext);}

    public static final ResourceLocation SQUIRT_SLUG_TEXTURE = new ResourceLocation( Squirtgun.MOD_ID, "textures/entity/projectile/squirt_slug.png");

    @Override
    public ResourceLocation getTextureLocation(SquirtSlug pEntity) {return SQUIRT_SLUG_TEXTURE;}
}
