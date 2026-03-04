package com.ordana.dimensional_tears.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class CylindricalBillboardParticle extends TextureSheetParticle {

    private final Quaternionf rotation;

    protected CylindricalBillboardParticle(ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(clientLevel, x, y, z, velocityX, velocityY, velocityZ);
        this.rotation = new Quaternionf();
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        this.transformRotation(partialTicks, camera);

        Vector3f[] vertices = this.getTransformedVertices(partialTicks, camera.getPosition());

        float minU = this.getU0();
        float maxU = this.getU1();
        float minV = this.getV0();
        float maxV = this.getV1();

        int color = this.argb();
        int light = this.getLightColor(partialTicks);

        this.addVertex(buffer, vertices[0], maxU, maxV, color, light);
        this.addVertex(buffer, vertices[1], maxU, minV, color, light);
        this.addVertex(buffer, vertices[2], minU, minV, color, light);
        this.addVertex(buffer, vertices[3], minU, maxV, color, light);
    }

    private void addVertex(VertexConsumer buffer, Vector3f vertex, float u, float v, int color, int light) {
        buffer.addVertex(vertex.x(), vertex.y(), vertex.z()).setUv(u, v).setColor(color).setLight(light);
    }

    private int argb() {
        return FastColor.ARGB32.colorFromFloat(this.alpha, this.rCol, this.gCol, this.bCol);
    }

    private Vector3f getSmoothPosition(float partialTicks, Vec3 cameraPosition) {
        float x = (float) (lerpTicks(partialTicks, this.xo, this.x) - cameraPosition.x());
        float y = (float) (lerpTicks(partialTicks, this.yo, this.y) - cameraPosition.y());
        float z = (float) (lerpTicks(partialTicks, this.zo, this.z) - cameraPosition.z());

        return new Vector3f(x, y, z);
    }

    private Vector3f[] getTransformedVertices(float partialTicks, Vec3 cameraPosition) {
        Vector3f[] vertices = new Vector3f[] {
            new Vector3f(-1.0F, -1.0F, 0.0F),
            new Vector3f(-1.0F, 1.0F, 0.0F),
            new Vector3f(1.0F, 1.0F, 0.0F),
            new Vector3f(1.0F, -1.0F, 0.0F)
        };

        float size = this.getQuadSize(partialTicks);
        Vector3f position = this.getSmoothPosition(partialTicks, cameraPosition);

        for (Vector3f vertex : vertices) {
            vertex.rotate(this.rotation);
            vertex.mul(size);
            vertex.add(position);
        }

        return vertices;
    }

    private void transformRotation(float partialTicks, Camera camera) {
        this.rotation.set(new Quaternionf());
        this.rotation.mul(Axis.YN.rotationDegrees(camera.getYRot()));

        if (this.roll != 0.0F)
            this.rotation.mul(Axis.ZP.rotation((float) lerpTicks(partialTicks, this.oRoll, this.roll)));
    }

    private static double lerpTicks(float partialTicks, double a, double b) {
        return a + partialTicks * (b - a);
    }

}
