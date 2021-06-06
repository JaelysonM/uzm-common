package com.uzm.common.nms.version.v1_12_R1.utils;

import net.minecraft.server.v1_12_R1.AxisAlignedBB;
import net.minecraft.server.v1_12_R1.MovingObjectPosition;
import net.minecraft.server.v1_12_R1.Vec3D;

import javax.annotation.Nullable;

/**
 * @author Maxter
 */
public class NullBoundingBox extends AxisAlignedBB {

  public NullBoundingBox() {
    super(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
  }

  public double a() {
    return 0.0D;
  }

  public double a(AxisAlignedBB arg0, double arg1) {
    return 0.0D;
  }

  public AxisAlignedBB a(AxisAlignedBB arg0) {
    return this;
  }

  public AxisAlignedBB a(double arg0, double arg1, double arg2) {
    return this;
  }

  public MovingObjectPosition b(Vec3D vec3D, Vec3D vec3D1) {
    return super.b(vec3D, vec3D1);
  }

  @Override
  public boolean b(Vec3D vec3D) {
    return false;
  }

  public double b(AxisAlignedBB arg0, double arg1) {
    return 0.0D;
  }
  public boolean c(AxisAlignedBB axisAlignedBB) {
    return false;
  }

  public double c(AxisAlignedBB arg0, double arg1) {
    return 0.0D;
  }

  public AxisAlignedBB c(double arg0, double arg1, double arg2) {
    return this;
  }

  public AxisAlignedBB grow(double arg0, double arg1, double arg2) {
    return this;
  }

  public AxisAlignedBB shrink(double arg0, double arg1, double arg2) {
    return this;
  }
}