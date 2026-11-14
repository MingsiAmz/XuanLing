package com.mingsiamz.xuanlingmod.gui.component;

import com.mingsiamz.xuanlingmod.api.IComponent;
import com.mingsiamz.xuanlingmod.type.ImpComponent;

public class DragPayload {
        public enum Type {CATEGORY, COMPONENT};

        public final Type type;
        public final String categoryName;
        public final ImpComponent component;

        public DragPayload(Type type, String categoryName, ImpComponent component) {
                this.type = type;
                this.categoryName = categoryName;
                this.component = component;
        }
}
