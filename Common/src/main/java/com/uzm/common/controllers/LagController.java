package com.uzm.common.controllers;

import lombok.Getter;
import lombok.Setter;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.6
 */

@Getter
@Setter
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
}
