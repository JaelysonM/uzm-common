package com.uzm.common.nms.version.v1_12_R1.utils.player;

import com.uzm.common.nms.NMS;
import com.uzm.common.nms.version.v1_12_R1.entity.EntityNPCPlayer;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.MathHelper;

public class PlayerControllerLook {
    private final EntityNPCPlayer a;
    private float b;
    private float c;
    private boolean d;
    private double e;
    private double f;
    private double g;

    public PlayerControllerLook(EntityNPCPlayer entityinsentient) {
        this.a = entityinsentient;
    }

    public void a() {
        if (!NMS.isNavigationFinished(this.a.getNavigation()))
            return;
        // this.a.pitch = 0.0F;
        if (this.d) {
            this.d = false;

            double d1 = this.e - this.a.locX;
            double d2 = this.f - (this.a.locY + this.a.getHeadHeight());
            double d3 = this.g - this.a.locZ;
            double d4 = MathHelper.sqrt(d1 * d1 + d3 * d3);

            float f1 = (float) (MathHelper.c(d3, d1) * 57.2957763671875D) - 90.0F;
            float f2 = (float) -(MathHelper.c(d2, d4) * 57.2957763671875D);
            this.a.pitch = a(this.a.pitch, f2, this.c);
            this.a.aP = a(this.a.aP, f1, this.b);
            this.a.yaw = this.a.aP;
            while (this.a.aP >= 180F) {
                this.a.aP -= 360F;
            }
            while (this.a.aP < -180F) {
                this.a.aP += 360F;
            }
        } else {
            // this.a.yaw = this.b(this.a.yaw, this.a.aP, -40F);
            // this.a.aP = a(this.a.aP, this.a.aN, 10.0F);
        }
        float f3 = MathHelper.g(this.a.aP - this.a.aN);
        if (!this.a.getNavigation().o()) {
            if (f3 < -75.0F) {
                this.a.aP = (this.a.aN - 75.0F);
            }
            if (f3 > 75.0F) {
                this.a.aP = (this.a.aN + 75.0F);
            }
        }
    }

    public void a(double d0, double d1, double d2, float f, float f1) {
        double d = Math.pow(this.e - d0, 2) + Math.pow(this.f - d1, 2) + Math.pow(this.g - d2, 2);
        if (d < 0.01) {
            // return;
        }
        this.e = d0;
        this.f = d1;
        this.g = d2;
        this.b = f;
        this.c = f1;
        this.d = true;
    }

    public void a(Entity entity, float f, float f1) {
        this.e = entity.locX;
        if ((entity instanceof EntityLiving))
            this.f = (entity.locY + entity.getHeadHeight());
        else {
            this.f = ((entity.getBoundingBox().b + entity.getBoundingBox().e) / 2.0D);
        }

        this.g = entity.locZ;
        this.b = f;
        this.c = f1;
        this.d = true;
    }

    private float a(float f, float f1, float f2) {
        float f3 = MathHelper.g(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    public boolean b() {
        return this.d;
    }

    public float b(float var0, float var1, float var2) {
        float var3 = c(var0, var1);
        float var4 = MathHelper.a(var3, -var2, var2);
        return var1 - var4;
    }

    public float c(float var0, float var1) {
        return MathHelper.g(var1 - var0);
    }

    public double e() {
        return this.e;
    }

    public double f() {
        return this.f;
    }

    public double g() {
        return this.g;
    }
}