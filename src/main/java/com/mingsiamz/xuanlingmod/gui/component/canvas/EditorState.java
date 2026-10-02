package com.mingsiamz.xuanlingmod.gui.component.canvas;

import java.util.ArrayList;
import java.util.List;

public class EditorState {
        public final List<CanvasNode> nodes = new ArrayList<>();
        public final List<Connection> connections = new ArrayList<>();
        public int panX = 0;
        public int panY = 0;
}
