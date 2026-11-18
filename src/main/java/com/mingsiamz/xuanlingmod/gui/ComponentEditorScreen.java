package com.mingsiamz.xuanlingmod.gui;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.client.ArtSpellCache;
import com.mingsiamz.xuanlingmod.Config;
import com.mingsiamz.xuanlingmod.data.ArtComponent;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.data.ComponentCodec;
import com.mingsiamz.xuanlingmod.data.ComponentGroup;
import com.mingsiamz.xuanlingmod.data.SpellComponent;
import com.mingsiamz.xuanlingmod.gui.component.DragPayload;
import com.mingsiamz.xuanlingmod.gui.component.ParamPopup;
import com.mingsiamz.xuanlingmod.items.TalismanPaperItem;
import com.mingsiamz.xuanlingmod.network.CreateDataPacket;
import com.mingsiamz.xuanlingmod.network.Networking;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.RegistryComponent;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;
import com.mingsiamz.xuanlingmod.type.spell.ParamType;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import com.mingsiamz.xuanlingmod.util.gui.component.GuiButton;
import com.mingsiamz.xuanlingmod.util.gui.component.ModalDialog;
import com.mingsiamz.xuanlingmod.util.gui.component.canvas.CanvasNode;
import com.mingsiamz.xuanlingmod.util.gui.component.canvas.CanvasPanel;
import com.mingsiamz.xuanlingmod.util.gui.component.canvas.Connection;
import com.mingsiamz.xuanlingmod.util.gui.component.canvas.EditorState;
import com.mingsiamz.xuanlingmod.util.gui.component.palette.PalettePanel;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ComponentEditorScreen extends Screen {

        private static final int MARGIN = 10;
        private static final int LEFT_W = 180;
        private static final int GAP = 8;
        private static final int BUTTON_H = 20;
        private static final int BUTTON_GAP = 6;

        private final EditorState state = new EditorState();

        private PalettePanel palette;
        private CanvasPanel canvas;
        private GuiButton createButton;

        private Component toastMessage = null;
        private long toastTime = 0;
        private ParamPopup activePopup = null;
        private ImpComponent selectedComponent;
        private int toastColor = CultivationTheme.DANGER;

        private DragPayload dragPayload = null;
        private XuanLingTypeEnum type;
        private String name = "";
        private String description = "";
        private ComponentGroup componentGroup = new ComponentGroup();
        private ModalDialog nameDialog;

        private boolean showNamePopup = false;
        private EditBox nameInput;
        private EditBox descriptionInput;
        private GuiButton confirmButton;
        private GuiButton cancelButton;

        private int popupX;
        private int popupY;
        private final int popupW = 280;
        private final int popupH = 180;
        private int inputX;
        private int inputY;
        private int inputW;
        private int descInputY;
        private int descInputH;
        private static final int INPUT_H = 18;
        private static final int DESC_INPUT_H = 60;

        public ComponentEditorScreen(XuanLingTypeEnum type) {
                super(Component.literal("make cultivation method"));
                this.type = type;
        }

        @Override
        protected void init() {
                super.init();

                int totalH = this.height - MARGIN * 2;
                int paletteH = totalH - BUTTON_H - BUTTON_GAP;

                palette = new PalettePanel(LEFT_W, paletteH);
                palette.setPosition(MARGIN, MARGIN);
                palette.setOnDragStart((payload, pos) -> dragPayload = payload);

                createButton = new GuiButton(
                                LEFT_W, BUTTON_H,
                                XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_cultivation").getString());
                createButton.setPosition(MARGIN, MARGIN + paletteH + BUTTON_GAP);
                createButton.onClick(this::openNamePopup);

                canvas = new CanvasPanel(state, this.width - LEFT_W - GAP - MARGIN * 2, totalH);
                canvas.setPosition(MARGIN + LEFT_W + GAP, MARGIN);

                List<ImpComponent> defs = RegistryComponent.getAvailable(this.type,
                                minecraft == null ? null : minecraft.player);
                palette.loadComponents(defs);

                computePopupLayout();
                nameInput = new EditBox(this.font, inputX, inputY, inputW, INPUT_H, Component.literal("name"));
                nameInput.setMaxLength(64);
                nameInput.setBordered(false);
                nameInput.setVisible(false);
                nameInput.setValue("");

                descriptionInput = new EditBox(this.font, inputX, descInputY, inputW, descInputH,
                                Component.literal("输入功法/神通/境界描述（幽灵提示，不存储）"));
                descriptionInput.setMaxLength(512);
                descriptionInput.setBordered(false);
                descriptionInput.setVisible(false);
                descriptionInput.setValue("");

                confirmButton = new GuiButton(80, 20,
                                XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_cultivation").getString());
                cancelButton = new GuiButton(80, 20,
                                XuanLing.trans(XuanlingLangPro.GUI_STRING, "give_up").getString());
                confirmButton.onClick(this::confirmName);
                cancelButton.onClick(this::closeNamePopup);

                int btnY = popupY + popupH - 32;
                int spacing = 10;
                int startX = popupX + (popupW - (80 * 2 + spacing)) / 2;
                confirmButton.setPosition(startX, btnY);
                cancelButton.setPosition(startX + 80 + spacing, btnY);
        }

        private void onCreate() {
                if (minecraft == null || minecraft.player == null)
                        return;

                boolean creative = minecraft.player.isCreative();

                // A3：创造模式下跳过连线、名称合法性与组件非空校验，便于测试
                if (!creative && !ensureComponentsConnected())
                        return;

                String trimmed = this.name == null ? "" : this.name.trim();

                if (!creative) {
                        if (trimmed.isEmpty()) {
                                toast(XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_name_empty"));
                                return;
                        }

                        if (!isValidName(trimmed)) {
                                toast(XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_name_invalid"));
                                return;
                        }

                        ComponentGroup built = collectComponentGroup();
                        if (built.isEmpty()) {
                                toast(XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_component_empty"));
                                return;
                        }
                        this.componentGroup = built;
                } else {
                        this.componentGroup = collectComponentGroup();
                }

                this.name = trimmed;

                boolean valid = switch (this.type) {
                        case SPELL -> new SpellComponent(this.name, this.componentGroup).check();
                        case ART -> new ArtComponent(this.name, this.componentGroup).check();
                        default -> false;
                };

                if (!creative && !valid) {
                        toast(this.type == XuanLingTypeEnum.REALM || this.type == XuanLingTypeEnum.ENERGY
                                        ? XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_not_support")
                                        : XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_cultivation_failed"));
                        return;
                }

                // D3：创建消耗灵感，不足则阻止，成功后扣除
                double cost = calculateInspirationCost(this.componentGroup);
                BasicData data = RegisterAttachments.getBasicData(minecraft.player);
                if (data == null || data.getInspiration() < cost) {
                        toast(XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_inspiration_insufficient"));
                        return;
                }
                data.setInspiration(data.getInspiration() - cost);

                String payload = ComponentCodec.encodeGroup(this.componentGroup).toString();
                if (payload.getBytes(
                                java.nio.charset.StandardCharsets.UTF_8).length > CreateDataPacket.MAX_PAYLOAD_LENGTH) {
                        toast(XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_cultivation_failed"));
                        return;
                }

                Networking.sendToServer(new CreateDataPacket(this.type, this.name, payload));

                String key = XuanLing.MODID + this.type.type;
                CompoundTag root = minecraft.player.getPersistentData();
                if (!root.contains(key, net.minecraft.nbt.Tag.TAG_COMPOUND)) {
                        root.put(key, new CompoundTag());
                }
                root.getCompound(key).putString(this.name, this.name);
                ArtSpellCache.updateCache(key, root.getCompound(key));

                // A5：创建后不再自动给予玉简/符纸，玉简由玩家自行制作后右键打开界面存储数据
                toast(XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_cultivation_success", this.name),
                                CultivationTheme.BORDER_GOLD);
        }

        /** D3：计算创建所需灵感 = 类型基础消耗 + Σ(组件基础消耗 + 调整后参数 × 单参数系数) */
        private double calculateInspirationCost(ComponentGroup group) {
                double base = switch (this.type) {
                        case SPELL -> Config.getInspirationCreateSpellBase();
                        case ART -> Config.getInspirationCreateArtBase();
                        case REALM -> Config.getInspirationCreateRealmBase();
                        default -> 0.0;
                };
                if (group == null)
                        return base;
                for (String basicType : group.getMaps().keySet()) {
                        for (ConcurrentLinkedQueue<ImpComponent> comps : group.getMaps()
                                        .get(basicType).values()) {
                                for (ImpComponent comp : comps) {
                                        base += Config.getInspirationComponentBaseCost();
                                        for (Object value : comp.getParams().values()) {
                                                if (value instanceof Number number) {
                                                        base += Math.abs(number.doubleValue())
                                                                        * Config.getInspirationParamCostPerUnit();
                                                }
                                        }
                                }
                        }
                }
                return base;
        }

        private double calculateCultivationSpeed(ComponentGroup group) {
                double totalSpeed = 0.0;
                if (group == null)
                        return 1.0;

                String[] basicTypes = {
                                com.mingsiamz.xuanlingmod.type.spell.BasicType.PROJECTLE.name,
                                com.mingsiamz.xuanlingmod.type.spell.BasicType.SELF.name,
                                com.mingsiamz.xuanlingmod.type.spell.BasicType.AOE.name
                };

                for (String basicType : basicTypes) {
                        ConcurrentLinkedQueue<ImpComponent> comps = group.getComponents(this.type, basicType);
                        if (comps != null) {
                                for (ImpComponent comp : comps) {
                                        ConcurrentHashMap<ParamType, Object> params = comp.getParams();
                                        if (params != null && params.containsKey(ParamType.SPEED)) {
                                                Object speedObj = params.get(ParamType.SPEED);
                                                if (speedObj instanceof Number) {
                                                        totalSpeed += ((Number) speedObj).doubleValue();
                                                }
                                        }
                                }
                        }
                }
                return Math.max(1.0, totalSpeed);
        }

        private void giveTalismanPaper(XuanLingTypeEnum type, String nickname, String description, double speed) {
                if (minecraft == null || minecraft.player == null)
                        return;

                ItemStack paper = new ItemStack(com.mingsiamz.xuanlingmod.register.RegisterItems.TALISMAN_PAPER.get());
                TalismanPaperItem.setData(paper, type, nickname, description, speed);

                if (!minecraft.player.getInventory().add(paper)) {
                        minecraft.player.drop(paper, false);
                }
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                this.renderBackground(g, mouseX, mouseY, partialTick);

                palette.render(g, mouseX, mouseY, partialTick);
                canvas.render(g, mouseX, mouseY, partialTick);
                renderAdjustedParameters(g);

                if (dragPayload != null) {
                        int w = 110;
                        int h = 30;
                        int gx = mouseX - w / 2;
                        int gy = mouseY - h / 2;
                        if (dragPayload.type == DragPayload.Type.CATEGORY) {
                                g.fill(gx, gy, gx + w, gy + h, CultivationTheme.BAR_OUTLINE
                                                | (CultivationTheme.BORDER_GOLD & 0x00FFFFFF));
                                GuiRenderUtil.border(g, gx, gy, gx + w, gy + h, CultivationTheme.BORDER_GOLD);
                                g.drawString(font, dragPayload.categoryName, gx + 6, gy + 11,
                                                CultivationTheme.TEXT_GOLD, false);
                        } else {
                                g.fill(gx, gy, gx + w, gy + h, CultivationTheme.BAR_OUTLINE
                                                | (CultivationTheme.TEXT_INFO & 0x00FFFFFF));
                                GuiRenderUtil.border(g, gx, gy, gx + w, gy + h, CultivationTheme.TEXT_INFO);
                                g.drawString(font, dragPayload.component.trans(), gx + 6, gy + 11,
                                                CultivationTheme.TEXT_PRIMARY, false);
                        }
                }

                createButton.render(g, mouseX, mouseY, partialTick);

                if (activePopup != null)
                        activePopup.render(g, mouseX, mouseY);

                if (showNamePopup)
                        renderNamePopup(g, mouseX, mouseY, partialTick);

                renderToast(g, mouseX, mouseY);
        }

        private void renderAdjustedParameters(GuiGraphics g) {
                if (selectedComponent == null) {
                        return;
                }
                List<String> lines = new ArrayList<>();
                selectedComponent.getParams().forEach((key, value) -> {
                        if (value instanceof Number number && number.doubleValue() != 0) {
                                lines.add(key.name() + " = " + value);
                        }
                });
                if (lines.isEmpty()) {
                        return;
                }
                int panelWidth = 180;
                int x = this.width - panelWidth - 12;
                int y = 44;
                int panelHeight = 34 + lines.size() * 12;
                g.fill(x, y, x + panelWidth, y + panelHeight, 0xE0101518);
                GuiRenderUtil.border(g, x, y, x + panelWidth, y + panelHeight, CultivationTheme.BORDER_CYAN);
                g.drawString(this.font, Component.literal("已调整参数"), x + 8, y + 7,
                                CultivationTheme.TEXT_GOLD, false);
                g.drawString(this.font, selectedComponent.trans(), x + 8, y + 19,
                                CultivationTheme.TEXT_SECONDARY, false);
                for (int i = 0; i < lines.size(); i++) {
                        g.drawString(this.font, lines.get(i), x + 8, y + 33 + i * 12,
                                        CultivationTheme.TEXT_PRIMARY, false);
                }
        }

        private void renderToast(GuiGraphics g, int mouseX, int mouseY) {
                if (toastMessage == null)
                        return;

                long elapsed = System.currentTimeMillis() - toastTime;
                if (elapsed > 2000) {
                        toastMessage = null;
                        return;
                }

                float alpha = 1.0f;
                if (elapsed > 1500)
                        alpha = 1.0f - (elapsed - 1500) / 500.0f;

                int alphaInt = (int) (alpha * 255) << 24;
                int textW = font.width(toastMessage);
                int boxW = textW + 12;
                int boxH = 16;
                int boxX = Math.max(5, Math.min(this.width - boxW - 5, mouseX - boxW / 2));
                int boxY = (mouseY - 25 < 5) ? mouseY + 15 : mouseY - 25;

                int bgColor = (CultivationTheme.TOAST_BG & 0x00FFFFFF) | alphaInt;
                int borderColor = (CultivationTheme.DANGER & 0x00FFFFFF) | alphaInt;
                int textColor = (CultivationTheme.DANGER & 0x00FFFFFF) | alphaInt;

                g.fill(boxX, boxY, boxX + boxW, boxY + boxH, bgColor);
                GuiRenderUtil.border(g, boxX, boxY, boxX + boxW, boxY + boxH, borderColor);
                g.drawString(font, toastMessage, boxX + 6, boxY + 4, textColor, false);
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (showNamePopup) {
                        if (confirmButton.mouseClicked(mx, my, button))
                                return true;
                        if (cancelButton.mouseClicked(mx, my, button))
                                return true;
                        if (mx >= inputX - 2 && mx <= inputX + inputW + 2
                                        && my >= inputY - 2 && my <= inputY + INPUT_H + 2) {
                                nameInput.setFocused(true);
                                descriptionInput.setFocused(false);
                                return true;
                        }
                        if (mx >= inputX - 2 && mx <= inputX + inputW + 2
                                        && my >= descInputY - 2 && my <= descInputY + descInputH + 2) {
                                descriptionInput.setFocused(true);
                                nameInput.setFocused(false);
                                descriptionInput.mouseClicked(mx, my, button);
                                return true;
                        } else {
                                nameInput.setFocused(false);
                                descriptionInput.setFocused(false);
                        }
                        return true;
                }

                if (activePopup != null) {
                        if (activePopup.isMouseOver(mx, my)) {
                                activePopup.mouseClicked(mx, my, button);
                                return true;
                        } else {
                                activePopup = null;
                        }
                }

                if (createButton.mouseClicked(mx, my, button))
                        return true;
                if (palette.mouseClicked(mx, my, button))
                        return true;
                if (canvas.mouseClicked(mx, my, button))
                        return true;
                return super.mouseClicked(mx, my, button);
        }

        @Override
        public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
                if (palette.mouseDragged(mx, my, button, dx, dy))
                        return true;
                if (canvas.mouseDragged(mx, my, button, dx, dy))
                        return true;
                return super.mouseDragged(mx, my, button, dx, dy);
        }

        @Override
        public boolean mouseReleased(double mx, double my, int button) {
                boolean paletteHandled = palette.mouseReleased(mx, my, button);
                boolean canvasHandled = canvas.mouseReleased(mx, my, button);

                if (dragPayload != null) {
                        if (canvas.contains(mx, my)) {
                                float cx = canvas.toCanvasX(mx);
                                float cy = canvas.toCanvasY(my);

                                if (dragPayload.type == DragPayload.Type.CATEGORY) {
                                        state.nodes.add(new CanvasNode(dragPayload.categoryName, (int) cx - 70,
                                                        (int) cy - 20));
                                } else {
                                        int dragW = 110;
                                        int dragH = 46;
                                        int dragX = (int) cx - dragW / 2;
                                        int dragY = (int) cy - dragH / 2;
                                        int magnet = 20;

                                        CanvasNode targetGroup = null;
                                        for (int i = state.nodes.size() - 1; i >= 0; i--) {
                                                CanvasNode node = state.nodes.get(i);
                                                if (node.isGroup()) {
                                                        int nx = node.getCanvasX();
                                                        int ny = node.getCanvasY();

                                                        int expandW = node.getWidth() + magnet * 2;
                                                        int expandH = node.getHeight() + magnet * 2;

                                                        if (node.hitTest(nx, ny, cx, cy) ||
                                                                        intersects(dragX, dragY, dragW, dragH,
                                                                                        nx - magnet, ny - magnet,
                                                                                        expandW, expandH)) {
                                                                targetGroup = node;
                                                                break;
                                                        }
                                                }
                                        }

                                        if (targetGroup != null) {
                                                if (!CanvasNode.canJoinGroup(targetGroup, dragPayload.component)) {
                                                        toast(XuanLing.trans(XuanlingLangPro.GUI_STRING,
                                                                        "the_component_not_join_group"));
                                                        CanvasNode normalNode = new CanvasNode(dragPayload.component,
                                                                        (int) cx - 55, (int) cy - 23);
                                                        setupNodeClick(normalNode);
                                                        state.nodes.add(normalNode);
                                                } else {
                                                        CanvasNode childNode = new CanvasNode(dragPayload.component, 0,
                                                                        0);
                                                        setupNodeClick(childNode);
                                                        targetGroup.children.add(childNode);
                                                }
                                        } else {
                                                CanvasNode normalNode = new CanvasNode(dragPayload.component,
                                                                (int) cx - 55, (int) cy - 23);
                                                setupNodeClick(normalNode);
                                                state.nodes.add(normalNode);
                                        }
                                }
                        }
                        dragPayload = null;
                        return true;
                }

                return paletteHandled || canvasHandled || super.mouseReleased(mx, my, button);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                if (keyCode == 257 || keyCode == 335) {
                        if (showNamePopup && descriptionInput.isFocused()) {
                                if ((modifiers & 1) == 0) {
                                        descriptionInput.setValue(descriptionInput.getValue() + "\n");
                                        return true;
                                }
                        }
                        confirmName();
                        return true;
                }
                if (keyCode == 1) {
                        closeNamePopup();
                        return true;
                }
                if (showNamePopup) {
                        if (nameInput.isFocused() && nameInput.keyPressed(keyCode, scanCode, modifiers))
                                return true;
                        if (descriptionInput.isFocused() && descriptionInput.keyPressed(keyCode, scanCode, modifiers))
                                return true;
                }
                return super.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public boolean charTyped(char codePoint, int modifiers) {
                if (showNamePopup) {
                        if (nameInput.isFocused() && nameInput.charTyped(codePoint, modifiers))
                                return true;
                        if (descriptionInput.isFocused() && descriptionInput.charTyped(codePoint, modifiers))
                                return true;
                }
                return super.charTyped(codePoint, modifiers);
        }

        private boolean intersects(int ax, int ay, int aw, int ah, int bx, int by, int bw, int bh) {
                return ax < bx + bw && ax + aw > bx && ay < by + bh && ay + ah > by;
        }

        @Override
        public boolean mouseScrolled(double mx, double my, double sx, double sy) {
                if (activePopup != null && activePopup.isMouseOver(mx, my))
                        return true;
                if (palette.mouseScrolled(mx, my, sx, sy))
                        return true;
                if (canvas.mouseScrolled(mx, my, sx, sy))
                        return true;
                return super.mouseScrolled(mx, my, sx, sy);
        }

        @Override
        public boolean isPauseScreen() {
                return false;
        }

        @Override
        public void onClose() {
                minecraft.setScreen(new CreateGui());
        }

        private void setupNodeClick(CanvasNode node) {
                node.setOnConfigClick(() -> {
                        if (node.getDef() != null) {
                                selectedComponent = node.getDef();
                                if (node.getDef().getParams() == null || node.getDef().getParams().isEmpty()) {
                                        toast(XuanLing.trans(XuanlingLangPro.GUI_STRING,
                                                        "this_component_not_select_more"));
                                        return;
                                }
                                int popupX = canvas.toScreenX(node.getCanvasX() + node.getWidth() + 10);
                                int popupY = canvas.toScreenY(node.getCanvasY());

                                activePopup = new ParamPopup(node.getDef(), popupX, popupY);
                        }
                });
        }

        private void computePopupLayout() {
                this.popupX = Math.max(10, (this.width - popupW) / 2);
                this.popupY = Math.max(10, (this.height - popupH) / 2);
                this.inputW = popupW - 40;
                this.inputX = popupX + 20;
                this.inputY = popupY + 40;
                this.descInputY = inputY + INPUT_H + 10;
                this.descInputH = DESC_INPUT_H;
        }

        private void openNamePopup() {
                if (!ensureComponentsConnected())
                        return;

                this.showNamePopup = true;
                this.nameInput.setValue(this.name == null ? "" : this.name);
                this.nameInput.setVisible(true);
                this.nameInput.setFocused(true);
                this.descriptionInput.setValue(this.description == null ? "" : this.description);
                this.descriptionInput.setVisible(true);
        }

        private void closeNamePopup() {
                this.showNamePopup = false;
                this.nameInput.setVisible(false);
                this.nameInput.setFocused(false);
                this.descriptionInput.setVisible(false);
                this.descriptionInput.setFocused(false);
        }

        private void confirmName() {
                this.name = this.nameInput.getValue().trim();
                String desc = this.descriptionInput.getValue().trim();
                if (desc.length() > 1024) {
                        desc = desc.substring(0, 1024);
                }
                this.description = desc;
                closeNamePopup();
                onCreate();
        }

        private void renderNamePopup(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                g.fill(0, 0, this.width, this.height, CultivationTheme.BG_MASK);

                g.fill(popupX, popupY, popupX + popupW, popupY + popupH, CultivationTheme.PANEL_BG);
                GuiRenderUtil.border(g, popupX, popupY, popupX + popupW, popupY + popupH, CultivationTheme.BORDER_GOLD);

                Component title = XuanLing.trans(XuanlingLangPro.GUI_STRING, "input_cultivation_name");
                g.drawString(font, title, popupX + (popupW - font.width(title)) / 2, popupY + 14,
                                CultivationTheme.TEXT_GOLD, true);

                g.fill(inputX - 2, inputY - 2, inputX + inputW + 2, inputY + INPUT_H + 2,
                                CultivationTheme.PANEL_BG_DARK);
                GuiRenderUtil.border(g, inputX - 2, inputY - 2, inputX + inputW + 2, inputY + INPUT_H + 2,
                                CultivationTheme.BORDER_CYAN);

                Component descLabel = XuanLing.trans(XuanlingLangPro.GUI_STRING, "input_cultivation_description");
                g.drawString(font, descLabel, inputX, descInputY - 12, CultivationTheme.TEXT_SECONDARY, false);

                g.fill(inputX - 2, descInputY - 2, inputX + inputW + 2, descInputY + descInputH + 2,
                                CultivationTheme.PANEL_BG_DARK);
                GuiRenderUtil.border(g, inputX - 2, descInputY - 2, inputX + inputW + 2, descInputY + descInputH + 2,
                                CultivationTheme.BORDER_CYAN);

                String text = nameInput.getValue();
                int textW = font.width(text);
                int textX = inputX + (inputW - textW) / 2;
                int textY = inputY + (INPUT_H - 8) / 2;
                g.drawString(font, text, textX, textY, CultivationTheme.TEXT_PRIMARY, false);

                if (nameInput.isFocused() && (System.currentTimeMillis() / 500) % 2 == 0) {
                        g.fill(textX + textW + 1, textY - 1, textX + textW + 2, textY + 9,
                                        CultivationTheme.TEXT_PRIMARY);
                }

                String descText = descriptionInput.getValue();
                if (!descText.isEmpty()) {
                        List<String> wrappedLines = wrapText(descText, inputW - 4);
                        for (int i = 0; i < wrappedLines.size(); i++) {
                                String line = wrappedLines.get(i);
                                int lineY = descInputY + 4 + i * 10;
                                if (lineY + 10 <= descInputY + descInputH) {
                                        g.drawString(font, line, inputX + 2, lineY, CultivationTheme.TEXT_PRIMARY,
                                                        false);
                                }
                        }
                }

                if (descriptionInput.isFocused() && (System.currentTimeMillis() / 500) % 2 == 0) {
                        String descTextCursor = descriptionInput.getValue();
                        int cursorPos = descriptionInput.getCursorPosition();
                        List<String> wrappedLines = wrapText(descTextCursor, inputW - 4);

                        int charCount = 0;
                        int cursorLine = 0;
                        int cursorX = inputX + 2;
                        int cursorY = descInputY + 4;

                        for (int i = 0; i < wrappedLines.size(); i++) {
                                String line = wrappedLines.get(i);
                                int lineLen = line.length();
                                int lineCharCount = lineLen + (i < wrappedLines.size() - 1 ? 1 : 0);

                                if (cursorPos <= charCount + lineCharCount) {
                                        cursorLine = i;
                                        int posInLine = cursorPos - charCount;
                                        String textBeforeCursor = line.substring(0, Math.min(posInLine, lineLen));
                                        cursorX = inputX + 2 + font.width(textBeforeCursor);
                                        cursorY = descInputY + 4 + i * 10;
                                        break;
                                }
                                charCount += lineCharCount;
                        }

                        if (wrappedLines.size() > 0 && cursorPos > charCount) {
                                String lastLine = wrappedLines.get(wrappedLines.size() - 1);
                                cursorX = inputX + 2 + font.width(lastLine);
                                cursorY = descInputY + 4 + (wrappedLines.size() - 1) * 10;
                        }

                        if (cursorY + 10 <= descInputY + descInputH) {
                                g.fill(cursorX, cursorY, cursorX + 1, cursorY + 10, CultivationTheme.TEXT_PRIMARY);
                        }
                }

                confirmButton.render(g, mouseX, mouseY, partialTick);
                cancelButton.render(g, mouseX, mouseY, partialTick);
        }

        private List<String> wrapText(String text, int maxWidth) {
                List<String> lines = new ArrayList<>();
                String[] paragraphs = text.split("\n");
                for (String paragraph : paragraphs) {
                        if (font.width(paragraph) <= maxWidth) {
                                lines.add(paragraph);
                        } else {
                                String[] words = paragraph.split(" ");
                                StringBuilder currentLine = new StringBuilder();
                                for (String word : words) {
                                        String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;
                                        if (font.width(testLine) <= maxWidth) {
                                                currentLine = new StringBuilder(testLine);
                                        } else {
                                                if (!currentLine.isEmpty()) {
                                                        lines.add(currentLine.toString());
                                                }
                                                currentLine = new StringBuilder(word);
                                        }
                                }
                                if (!currentLine.isEmpty()) {
                                        lines.add(currentLine.toString());
                                }
                        }
                }
                return lines.isEmpty() ? List.of("") : lines;
        }

        
        private boolean ensureComponentsConnected() {
                if (areComponentsConnected())
                        return true;
                toast(XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_component_not_connected"));
                return false;
        }

        private boolean areComponentsConnected() {
                if (state.nodes.isEmpty())
                        return true;
                if (state.connections.isEmpty())
                        return false;

                Set<CanvasNode> visited = new HashSet<>();
                ArrayDeque<CanvasNode> queue = new ArrayDeque<>();
                CanvasNode start = state.nodes.get(0);
                visited.add(start);
                queue.add(start);

                while (!queue.isEmpty()) {
                        CanvasNode current = queue.poll();
                        for (Connection connection : state.connections) {
                                CanvasNode other;
                                if (connection.getFrom() == current)
                                        other = connection.getTo();
                                else if (connection.getTo() == current)
                                        other = connection.getFrom();
                                else
                                        continue;

                                if (state.nodes.contains(other) && visited.add(other))
                                        queue.add(other);
                        }
                }

                return visited.size() == state.nodes.size();
        }

        private ComponentGroup collectComponentGroup() {
                ComponentGroup group = new ComponentGroup();
                for (CanvasNode node : state.nodes) {
                        if (node.isGroup()) {
                                for (CanvasNode child : node.children)
                                        addDefTo(group, child.getDef());
                        } else {
                                addDefTo(group, node.getDef());
                        }
                }
                return group;
        }

        private void addDefTo(ComponentGroup group, ImpComponent def) {
                if (def != null)
                        group.addComponent(this.type, def.type(), def);
        }

        private boolean isValidName(String s) {
                return CreateDataPacket.isValidName(s);
        }

        private void toast(Component message) {
                toast(message, CultivationTheme.DANGER);
        }

        private void toast(Component message, int color) {
                this.toastMessage = message;
                this.toastColor = color;
                this.toastTime = System.currentTimeMillis();
        }

}
