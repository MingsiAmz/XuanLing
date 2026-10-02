package com.mingsiamz.xuanlingmod.gui.component.data;

public record ComponentDef(
                String id,
                String displayName,
                ComponentCategory category,
                int width,
                int height,
                int color) {
        public ComponentDef(String id, String name, ComponentCategory cat) {
                this(id, name, cat, 110, 46, 0xFF4DD0E1);
        }
}
