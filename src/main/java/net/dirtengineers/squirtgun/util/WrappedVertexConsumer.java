package net.dirtengineers.squirtgun.util;

import com.mojang.blaze3d.vertex.VertexConsumer;

class WrappedVertexConsumer implements VertexConsumer {
    protected final VertexConsumer consumer;
    protected final float red;
    protected final float green;
    protected final float blue;
    protected final float alpha;

    public WrappedVertexConsumer(VertexConsumer pConsumer, float pRed, float pGreen, float pBlue, float pAlpha) {
        this.consumer = pConsumer;
        this.red = pRed;
        this.green = pGreen;
        this.blue = pBlue;
        this.alpha = pAlpha;
    }

    public VertexConsumer vertex(double pX, double pY, double pZ) {
        return this.consumer.vertex(pX, pY, pZ);
    }

    public VertexConsumer color(int pRed, int pGreen, int pBlue, int pAlpha) {
        return this.consumer.color((int)((float)pRed * this.red), (int)((float)pGreen * this.green), (int)((float)pBlue * this.blue), (int)((float)pAlpha * this.alpha));
    }

    public VertexConsumer uv(float pU, float pV) {
        return this.consumer.uv(pU, pV);
    }

    public VertexConsumer overlayCoords(int pU, int pV) {
        return this.consumer.overlayCoords(pU, pV);
    }

    public VertexConsumer uv2(int pU, int pV) {
        return this.consumer.uv2(pU, pV);
    }

    public VertexConsumer normal(float pX, float pY, float pZ) {
        return this.consumer.normal(pX, pY, pZ);
    }

    public void endVertex() {
        this.consumer.endVertex();
    }

    public void defaultColor(int pDefaultR, int pDefaultG, int pDefaultB, int pDefaultA) {
        this.consumer.defaultColor(pDefaultR, pDefaultG, pDefaultB, pDefaultA);
    }

    public void unsetDefaultColor() {
        this.consumer.unsetDefaultColor();
    }
}
