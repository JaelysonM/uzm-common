package com.uzm.common.libraries.scoreboard.animations;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.ChatColor;

import javax.security.auth.Destroyable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A complete and updatable plugin for any usages.
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.5
 */

@Data
@AllArgsConstructor
public class Scroller implements Destroyable {

    private int frame;
    private int maxFrames;
    private String text;
    private String stripedText;
    private ScrollType scrollType;
    private final boolean bold;
    private List<String> restFrames;
    private String firstColor;
    private String sliderColor;
    private String finalColor;

    private boolean dynamic = true;
    private boolean dynamicController = true;

    public Scroller(String title, String firstColor, String sliderColor, String finalColor, boolean bold, ScrollType scrollType, String... restFrames) {
        this.text = title;
        this.stripedText = ChatColor.stripColor(title);
        this.finalColor = finalColor + (bold ? ChatColor.BOLD : "");
        this.firstColor = firstColor + (bold ? ChatColor.BOLD : "");
        this.sliderColor = sliderColor + (bold ? ChatColor.BOLD : "");
        this.bold = bold;
        this.scrollType = scrollType;
        this.restFrames = new ArrayList<>(Arrays.asList(restFrames));

        this.frame = scrollType == ScrollType.FORWARD ? 0 : (stripedText.length() + restFrames.length - 1);
        if (this.scrollType == ScrollType.DYNAMIC) dynamic = true;
        this.maxFrames = stripedText.length() + restFrames.length;
    }

    public String next() {


        if (this.frame >= 0 && this.frame <= this.maxFrames) {
            StringBuilder sb = new StringBuilder();
            if (this.frame < this.stripedText.length())
                sb.append(this.finalColor).append(this.frame > 0 ? this.stripedText.substring(0, this.frame) : "").append(this.sliderColor)
                        .append(this.stripedText.charAt(this.frame)).append(this.firstColor).append(this.stripedText.substring(this.frame + 1));
            else
                sb.append((this.getScrollType() == ScrollType.FORWARD) ? this.firstColor : this.finalColor).append(this.text);


            if (this.getScrollType() == ScrollType.FORWARD) this.frame += 1;
            else this.frame -= 1;
            return this.frame >= this.stripedText.length() && this.restFrames.size() > 0 ? restFrames.get(this.frame - this.stripedText.length()) : sb.toString();
        } else {

            ScrollType scrollType = this.getScrollType();
            if (this.dynamic) {
                if (this.scrollType == ScrollType.FORWARD) this.setScrollType(ScrollType.BACKWARD);
                else this.setScrollType(ScrollType.FORWARD);
            }
            if (this.getScrollType() == ScrollType.FORWARD)
                this.frame = 0;
            else this.frame = this.maxFrames;
            return ((scrollType == ScrollType.FORWARD) ? this.firstColor : this.finalColor) + this.text;
        }
    }


    public void destroy() {
        this.finalColor = null;
        this.restFrames.clear();
        this.restFrames = null;
        this.stripedText = null;
        this.text = null;
        this.firstColor = null;
        this.sliderColor = null;
        this.scrollType = null;

    }

    public enum ScrollType {
        FORWARD,
        BACKWARD,
        DYNAMIC,
    }

}