package com.mingsiamz.xuanlingmod.util.gui.theme;

import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;

public final class GuiTheme implements ColorPalette {

        private final int mask, panelBg, panelBgDark, borderGold, borderCyan, borderDim, gridLine;
        private final int textPrimary, textSecondary, textGold, textDanger, textSuccess, textInfo;
        private final int accent, success, danger, warning, info;
        private final int buttonBg, buttonHoverBg, buttonBorder, buttonHoverBorder, hoverCyan, hoverRed,
                        softOverlay, subtleLine;
        private final int inputBg, inputBorder, cursor, scrollTrack, scrollThumb, progressHighlight,
                        progressShadow, white;

        private final String name;

        private GuiTheme(Builder b) {
                this.name = b.name;
                this.mask = b.mask;
                this.panelBg = b.panelBg;
                this.panelBgDark = b.panelBgDark;
                this.borderGold = b.borderGold;
                this.borderCyan = b.borderCyan;
                this.borderDim = b.borderDim;
                this.gridLine = b.gridLine;
                this.textPrimary = b.textPrimary;
                this.textSecondary = b.textSecondary;
                this.textGold = b.textGold;
                this.textDanger = b.textDanger;
                this.textSuccess = b.textSuccess;
                this.textInfo = b.textInfo;
                this.accent = b.accent;
                this.success = b.success;
                this.danger = b.danger;
                this.warning = b.warning;
                this.info = b.info;
                this.buttonBg = b.buttonBg;
                this.buttonHoverBg = b.buttonHoverBg;
                this.buttonBorder = b.buttonBorder;
                this.buttonHoverBorder = b.buttonHoverBorder;
                this.hoverCyan = b.hoverCyan;
                this.hoverRed = b.hoverRed;
                this.softOverlay = b.softOverlay;
                this.subtleLine = b.subtleLine;
                this.inputBg = b.inputBg;
                this.inputBorder = b.inputBorder;
                this.cursor = b.cursor;
                this.scrollTrack = b.scrollTrack;
                this.scrollThumb = b.scrollThumb;
                this.progressHighlight = b.progressHighlight;
                this.progressShadow = b.progressShadow;
                this.white = b.white;
        }

        @Override
        public int mask() {
                return mask;
        }

        @Override
        public int panelBg() {
                return panelBg;
        }

        @Override
        public int panelBgDark() {
                return panelBgDark;
        }

        @Override
        public int borderGold() {
                return borderGold;
        }

        @Override
        public int borderCyan() {
                return borderCyan;
        }

        @Override
        public int borderDim() {
                return borderDim;
        }

        @Override
        public int gridLine() {
                return gridLine;
        }

        @Override
        public int textPrimary() {
                return textPrimary;
        }

        @Override
        public int textSecondary() {
                return textSecondary;
        }

        @Override
        public int textGold() {
                return textGold;
        }

        @Override
        public int textDanger() {
                return textDanger;
        }

        @Override
        public int textSuccess() {
                return textSuccess;
        }

        @Override
        public int textInfo() {
                return textInfo;
        }

        @Override
        public int accent() {
                return accent;
        }

        @Override
        public int success() {
                return success;
        }

        @Override
        public int danger() {
                return danger;
        }

        @Override
        public int warning() {
                return warning;
        }

        @Override
        public int info() {
                return info;
        }

        @Override
        public int buttonBg() {
                return buttonBg;
        }

        @Override
        public int buttonHoverBg() {
                return buttonHoverBg;
        }

        @Override
        public int buttonBorder() {
                return buttonBorder;
        }

        @Override
        public int buttonHoverBorder() {
                return buttonHoverBorder;
        }

        @Override
        public int hoverCyan() {
                return hoverCyan;
        }

        @Override
        public int hoverRed() {
                return hoverRed;
        }

        @Override
        public int softOverlay() {
                return softOverlay;
        }

        @Override
        public int subtleLine() {
                return subtleLine;
        }

        @Override
        public int inputBg() {
                return inputBg;
        }

        @Override
        public int inputBorder() {
                return inputBorder;
        }

        @Override
        public int cursor() {
                return cursor;
        }

        @Override
        public int scrollTrack() {
                return scrollTrack;
        }

        @Override
        public int scrollThumb() {
                return scrollThumb;
        }

        @Override
        public int progressHighlight() {
                return progressHighlight;
        }

        @Override
        public int progressShadow() {
                return progressShadow;
        }

        @Override
        public int white() {
                return white;
        }

        public String getName() {
                return name;
        }

        public static final GuiTheme JADE = builder("jade").build();

        public static final GuiTheme EMBER = builder("ember")
                        .accent(0xFFFF7043).borderCyan(0xFFFF8A50).info(0xFFFF7043)
                        .buttonBg(0x30FF7043).buttonHoverBg(0x70FF7043)
                        .scrollThumb(0x80FF7043).textInfo(0xFFFFB088)
                        .build();

        public static final GuiTheme AZURE = builder("azure")
                        .accent(0xFF4FC3F7).borderCyan(0xFF4FC3F7).info(0xFF4FC3F7)
                        .borderGold(0xFFB3E5FC)
                        .buttonBg(0x304FC3F7).buttonHoverBg(0x704FC3F7)
                        .scrollThumb(0x804FC3F7)
                        .build();

        public static final GuiTheme VIOLET = builder("violet")
                        .accent(0xFFB388FF).borderCyan(0xFFB388FF).info(0xFFB388FF)
                        .borderGold(0xFFD1C4E9)
                        .buttonBg(0x30B388FF).buttonHoverBg(0x70B388FF)
                        .scrollThumb(0x80B388FF)
                        .build();

        public static GuiTheme[] presets() {
                return new GuiTheme[] { JADE, EMBER, AZURE, VIOLET };
        }

        private static GuiTheme current = JADE;

        public static GuiTheme active() {
                return current;
        }

        public static void setActive(GuiTheme theme) {
                if (theme != null)
                        current = theme;
        }

        public static Builder builder(String name) {
                return new Builder(name);
        }

        public static final class Builder {
                private final String name;
                private int mask = CultivationTheme.BG_MASK;
                private int panelBg = CultivationTheme.PANEL_BG;
                private int panelBgDark = CultivationTheme.PANEL_BG_DARK;
                private int borderGold = CultivationTheme.BORDER_GOLD;
                private int borderCyan = CultivationTheme.BORDER_CYAN;
                private int borderDim = CultivationTheme.BORDER_DIM;
                private int gridLine = CultivationTheme.GRID_LINE;
                private int textPrimary = CultivationTheme.TEXT_PRIMARY;
                private int textSecondary = CultivationTheme.TEXT_SECONDARY;
                private int textGold = CultivationTheme.TEXT_GOLD;
                private int textDanger = CultivationTheme.TEXT_DANGER;
                private int textSuccess = CultivationTheme.TEXT_SUCCESS;
                private int textInfo = CultivationTheme.TEXT_INFO;
                private int accent = CultivationTheme.ACCENT;
                private int success = CultivationTheme.SUCCESS;
                private int danger = CultivationTheme.DANGER;
                private int warning = CultivationTheme.WARNING;
                private int info = CultivationTheme.INFO;
                private int buttonBg = CultivationTheme.BUTTON_BG;
                private int buttonHoverBg = CultivationTheme.BUTTON_HOVER_BG;
                private int buttonBorder = CultivationTheme.BORDER_CYAN;
                private int buttonHoverBorder = CultivationTheme.BORDER_GOLD;
                private int hoverCyan = CultivationTheme.HOVER_CYAN;
                private int hoverRed = CultivationTheme.HOVER_RED;
                private int softOverlay = CultivationTheme.SOFT_OVERLAY;
                private int subtleLine = CultivationTheme.SUBTLE_LINE;
                private int inputBg = CultivationTheme.INPUT_BG;
                private int inputBorder = CultivationTheme.INPUT_BORDER;
                private int cursor = CultivationTheme.CURSOR;
                private int scrollTrack = CultivationTheme.SCROLL_TRACK;
                private int scrollThumb = CultivationTheme.SCROLL_THUMB;
                private int progressHighlight = CultivationTheme.PROGRESS_HILITE;
                private int progressShadow = CultivationTheme.PROGRESS_SHADOW;
                private int white = CultivationTheme.WHITE;

                private Builder(String name) {
                        this.name = name;
                }

                public Builder mask(int v) {
                        this.mask = v;
                        return this;
                }

                public Builder panelBg(int v) {
                        this.panelBg = v;
                        return this;
                }

                public Builder panelBgDark(int v) {
                        this.panelBgDark = v;
                        return this;
                }

                public Builder borderGold(int v) {
                        this.borderGold = v;
                        return this;
                }

                public Builder borderCyan(int v) {
                        this.borderCyan = v;
                        return this;
                }

                public Builder borderDim(int v) {
                        this.borderDim = v;
                        return this;
                }

                public Builder gridLine(int v) {
                        this.gridLine = v;
                        return this;
                }

                public Builder textPrimary(int v) {
                        this.textPrimary = v;
                        return this;
                }

                public Builder textSecondary(int v) {
                        this.textSecondary = v;
                        return this;
                }

                public Builder textGold(int v) {
                        this.textGold = v;
                        return this;
                }

                public Builder textDanger(int v) {
                        this.textDanger = v;
                        return this;
                }

                public Builder textSuccess(int v) {
                        this.textSuccess = v;
                        return this;
                }

                public Builder textInfo(int v) {
                        this.textInfo = v;
                        return this;
                }

                public Builder accent(int v) {
                        this.accent = v;
                        return this;
                }

                public Builder success(int v) {
                        this.success = v;
                        return this;
                }

                public Builder danger(int v) {
                        this.danger = v;
                        return this;
                }

                public Builder warning(int v) {
                        this.warning = v;
                        return this;
                }

                public Builder info(int v) {
                        this.info = v;
                        return this;
                }

                public Builder buttonBg(int v) {
                        this.buttonBg = v;
                        return this;
                }

                public Builder buttonHoverBg(int v) {
                        this.buttonHoverBg = v;
                        return this;
                }

                public Builder buttonBorder(int v) {
                        this.buttonBorder = v;
                        return this;
                }

                public Builder buttonHoverBorder(int v) {
                        this.buttonHoverBorder = v;
                        return this;
                }

                public Builder hoverCyan(int v) {
                        this.hoverCyan = v;
                        return this;
                }

                public Builder hoverRed(int v) {
                        this.hoverRed = v;
                        return this;
                }

                public Builder softOverlay(int v) {
                        this.softOverlay = v;
                        return this;
                }

                public Builder subtleLine(int v) {
                        this.subtleLine = v;
                        return this;
                }

                public Builder inputBg(int v) {
                        this.inputBg = v;
                        return this;
                }

                public Builder inputBorder(int v) {
                        this.inputBorder = v;
                        return this;
                }

                public Builder cursor(int v) {
                        this.cursor = v;
                        return this;
                }

                public Builder scrollTrack(int v) {
                        this.scrollTrack = v;
                        return this;
                }

                public Builder scrollThumb(int v) {
                        this.scrollThumb = v;
                        return this;
                }

                public Builder progressHighlight(int v) {
                        this.progressHighlight = v;
                        return this;
                }

                public Builder progressShadow(int v) {
                        this.progressShadow = v;
                        return this;
                }

                public Builder white(int v) {
                        this.white = v;
                        return this;
                }

                public GuiTheme build() {
                        return new GuiTheme(this);
                }
        }
}
