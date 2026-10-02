package com.mingsiamz.xuanlingmod.gui.component.data;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;

import net.minecraft.network.chat.Component;

public enum ComponentCategory {
        BASIC(XuanLing.trans(XuanlingLangPro.COMPONEN_STRING, "basic_component")),
        LOGIC(XuanLing.trans(XuanlingLangPro.COMPONEN_STRING, "logic_component")),
        RENDER(XuanLing.trans(XuanlingLangPro.COMPONEN_STRING, "render_component")),
        DATA(XuanLing.trans(XuanlingLangPro.COMPONEN_STRING, "data_component"));

        private final Component display;

        ComponentCategory(Component d) {
                this.display = d;
        }

        public String getDisplay() {
                return display.getString();
        }
}
