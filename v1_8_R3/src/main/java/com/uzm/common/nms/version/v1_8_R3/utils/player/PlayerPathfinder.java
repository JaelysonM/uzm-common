package com.uzm.common.nms.version.v1_8_R3.utils.player;

;
import com.uzm.common.reflections.Accessors;
import net.minecraft.server.v1_8_R3.*;

import java.lang.reflect.Field;

public class PlayerPathfinder {
    private final Path a = new Path();
    private final PathPoint[] b = new PathPoint[32];
    private final PlayerPathfinderNormal c;

    public PlayerPathfinder(PlayerPathfinderNormal paramPathfinderAbstract) {
        this.c = paramPathfinderAbstract;
    }

    private PathEntity a(Entity paramEntity, PathPoint paramPathPoint1, PathPoint paramPathPoint2, float paramFloat) {
        float newF = 0.0F;
        try {
            E.set(paramPathPoint1, 0.0F);
            newF = paramPathPoint1.b(paramPathPoint2);
            F.set(paramPathPoint1, newF);
            G.set(paramPathPoint1, newF);
        } catch (IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }

        this.a.a();
        this.a.a(paramPathPoint1);

        PathPoint localObject = paramPathPoint1;
        while (!this.a.e()) {
            PathPoint localPathPoint1 = this.a.c();
            if (localPathPoint1.equals(paramPathPoint2)) {
                return a(paramPathPoint1, paramPathPoint2);
            }
            if (localPathPoint1.b(paramPathPoint2) < localObject.b(paramPathPoint2)) {
                localObject = localPathPoint1;
            }
            localPathPoint1.i = true;

            int i = this.c.a(this.b, paramEntity, localPathPoint1, paramPathPoint2, paramFloat);
            for (int j = 0; j < i; j++) {
                PathPoint localPathPoint2 = this.b[j];
                try {
                    float e = E.getFloat(localObject);
                    float f = e + localPathPoint1.b(localPathPoint2);
                    if ((f < paramFloat * 2.0F) && ((!localPathPoint2.a()) || (f < e))) {
                        H.set(localPathPoint2, localPathPoint1);
                        E.set(localPathPoint2, f);
                        newF = localPathPoint2.b(paramPathPoint2);
                        F.set(localPathPoint2, newF);
                        if (localPathPoint2.a()) {
                            this.a.a(localPathPoint2, f + newF);
                        } else {
                            G.set(localPathPoint2, f + newF);
                            this.a.a(localPathPoint2);
                        }
                    }
                } catch (IllegalArgumentException | IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }
        if (localObject == paramPathPoint1) {
            return null;
        }
        return a(paramPathPoint1, localObject);
    }

    public PathEntity a(IBlockAccess paramIBlockAccess, Entity paramEntity, BlockPosition paramBlockPosition,
            float paramFloat) {
        return a(paramIBlockAccess, paramEntity, paramBlockPosition.getX() + 0.5F, paramBlockPosition.getY() + 0.5F,
                paramBlockPosition.getZ() + 0.5F, paramFloat);
    }

    private PathEntity a(IBlockAccess paramIBlockAccess, Entity paramEntity, double paramDouble1, double paramDouble2,
            double paramDouble3, float paramFloat) {
        this.a.a();
        this.c.a(paramIBlockAccess, paramEntity);
        PathPoint localPathPoint1 = this.c.a(paramEntity);
        PathPoint localPathPoint2 = this.c.a(paramEntity, paramDouble1, paramDouble2, paramDouble3);

        PathEntity localPathEntity = a(paramEntity, localPathPoint1, localPathPoint2, paramFloat);

        this.c.a();
        return localPathEntity;
    }

    public PathEntity a(IBlockAccess paramIBlockAccess, Entity paramEntity1, Entity paramEntity2, float paramFloat) {
        return a(paramIBlockAccess, paramEntity1, paramEntity2.locX, paramEntity2.getBoundingBox().b, paramEntity2.locZ,
                paramFloat);
    }

    private PathEntity a(PathPoint paramPathPoint1, PathPoint paramPathPoint2) {
        int i = 1;
        PathPoint localPathPoint = paramPathPoint2;
        try {
            while (H.get(localPathPoint) != null) {
                i++;
                localPathPoint = (PathPoint) H.get(localPathPoint);
            }
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        PathPoint[] arrayOfPathPoint = new PathPoint[i];
        localPathPoint = paramPathPoint2;
        arrayOfPathPoint[(--i)] = localPathPoint;
        try {
            while (H.get(localPathPoint) != null) {
                localPathPoint = (PathPoint) H.get(localPathPoint);
                arrayOfPathPoint[(--i)] = localPathPoint;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new PathEntity(arrayOfPathPoint);
    }

    private static Field E = Accessors.getField(PathPoint.class, "e").getHandle();
    private static Field F = Accessors.getField(PathPoint.class, "f").getHandle();
    private static Field G = Accessors.getField(PathPoint.class, "g").getHandle();
    private static Field H = Accessors.getField(PathPoint.class, "h").getHandle();
}