package com.uzm.common.controllers;

public class LagController implements Runnable {

    private int tick;
    private double tps;
    private double lastFinish;
    private boolean lowTps;


    @Override
    public void run() {
        tick++;
        if (tick == 20) {
            tps = tick;
            tick = 0;
            if (lastFinish + 1000 < System.currentTimeMillis()) tps /= (System.currentTimeMillis() - lastFinish) / 1000;
            lastFinish = System.currentTimeMillis();
            if (tps < 15) {
                if (!lowTps)
                    lowTps = true;
            }
        } else {
            if (lowTps)
                lowTps = false;
        }

    }

    public double getLastFinish() {
        return lastFinish;
    }

    public int getTick() {
        return tick;
    }

    public double getTps() {
        return tps;
    }

    public boolean isLowTps() {
        return lowTps;
    }
}
