package com.mingsiamz.xuanlingmod.gui.component;

import com.mingsiamz.xuanlingmod.api.IComponent;

public class DragPayload {
        public enum Type {CATEGORY, COMPONENT};

        public final Type type;
        public final String categoryName;
        public final IComponent<?> component;

        public DragPayload(Type type, String categoryName, IComponent<?> component) {
                this.type = type;
                this.categoryName = categoryName;
                this.component = component;
        }
}
