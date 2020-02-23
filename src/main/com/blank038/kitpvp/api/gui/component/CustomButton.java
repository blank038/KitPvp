package com.blank038.kitpvp.api.gui.component;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import com.blank038.kitpvp.api.interfaces.InteractExecute;

public class CustomButton extends ElementButton {
    private InteractExecute execute;

    public CustomButton(String text, InteractExecute execute) {
        super(text);
        this.execute = execute;
    }

    public void run(Player player) {
        execute.run(player);
    }
}