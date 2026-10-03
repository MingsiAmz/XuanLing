package com.mingsiamz.xuanlingmod.util.gui.layout;

public final class Layouts {
        private Layouts() {
        }

        public static ColumnLayout column() {
                return new ColumnLayout();
        }

        public static RowLayout row() {
                return new RowLayout();
        }

        public static GridLayout grid(int cols) {
                return new GridLayout(cols);
        }

        public static GridLayout grid(int cols, int rows) {
                return new GridLayout(cols, rows);
        }

        public static FlowLayout flow() {
                return new FlowLayout();
        }

        public static FlowLayout flow(int maxWidth) {
                return new FlowLayout().setMaxWidth(maxWidth);
        }

        public static FloatLayout floatLayout() {
                return new FloatLayout();
        }
}
