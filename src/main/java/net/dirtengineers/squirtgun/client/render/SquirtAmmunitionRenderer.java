package net.dirtengineers.squirtgun.client.render;

import net.dirtengineers.squirtgun.Squirtgun;
import net.dirtengineers.squirtgun.common.entity.ammunition.SquirtSlug;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SquirtAmmunitionRenderer extends ArrowRenderer<SquirtSlug> {

    public static final ResourceLocation TEXTURE = new ResourceLocation( Squirtgun.MOD_ID, "textures/entity/test.png");

    public SquirtAmmunitionRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SquirtSlug pEntity) {
        return TEXTURE;
    }
}
