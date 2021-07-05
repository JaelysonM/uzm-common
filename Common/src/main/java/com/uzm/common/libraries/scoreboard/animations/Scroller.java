package com.uzm.common.libraries.scoreboard.animations;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Scroller {

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


    public Scroller(String title, String firstColor, String sliderColor, String finalColor, boolean bold, ScrollType scrollType, String... restFrames) {
        this.text = title;
        this.stripedText = ChatColor.stripColor(title);
        this.finalColor = finalColor + (bold ? ChatColor.BOLD : "");
        this.firstColor = firstColor + (bold ? ChatColor.BOLD : "");
        this.sliderColor = sliderColor + (bold ? ChatColor.BOLD : "");
        this.bold = bold;
        this.scrollType = scrollType;
        this.restFrames = new ArrayList<>(Arrays.asList(restFrames));

        this.frame = scrollType == ScrollType.FORWARD ? 0 : stripedText.length() + restFrames.length;
        this.maxFrames = stripedText.length() + restFrames.length;
    }

    public String next() {
        if (this.scrollType == ScrollType.FORWARD) {
            if (this.frame <= this.maxFrames) {
                StringBuilder sb = new StringBuilder();
                // String composeText = text;
                if (this.frame < this.stripedText.length()) {
                    if (this.frame == 0) {
                        sb.append(this.sliderColor).append(this.stripedText.substring(0, 0)).append(this.firstColor).append(this.stripedText.substring(1, this.stripedText.length()));
                    } else {
                        if (frame == 2) {
                            System.out.println(this.stripedText.substring(0, this.frame - 1));
                            System.out.println(this.stripedText.split("")[frame]);
                            System.out.println(this.stripedText.substring(frame + 1));
                        }

                        sb.append(this.finalColor).append(this.stripedText.substring(0, this.frame - 1)).append(this.sliderColor).append(this.stripedText.substring(this.frame, this.frame))
                                .append(firstColor).append(this.stripedText.substring(this.frame + 1, this.stripedText.length()));

                    }
                }
                this.frame += 1;
                return sb.toString();
        /*} else {
          if (!this.restFrames.isEmpty()) {
            composeText = this.restFrames.get((this.frame - this.stripedText.length()) - 2);
          }else {
            composeText = this.finalColor+ this.stripedText;
          }
        }*/
            } else {
                this.frame = 0;
                return this.text;
            }
        } else {
            return null;
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
        BACKWARD
    }

}