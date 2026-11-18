package com.mingsiamz.xuanlingmod.provider;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicRealm;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class XuanlingLangPro {

        public static final String GUI_STRING = XuanLing.MODID + "." + "gui" + ".";
        public static final String MISC_STRING = XuanLing.MODID + "." + "misc" + ".";
        public static final String COMPONEN_STRING = XuanLing.MODID + "." + "component" + ".";
        public static final String REALM_STRING = XuanLing.MODID + "." + "realm" + ".";
        public static final String ENERGY_STRING = XuanLing.MODID + "." + "energy" + ".";

        public static final class LangZh extends LanguageProvider {

                public LangZh(PackOutput output) {
                        super(output, XuanLing.MODID, "zh_cn");
                }

                @Override
                protected void addTranslations() {
                        this.add(GUI_STRING + "normallife", "身寿:%s");
                        this.add(GUI_STRING + "soullife", "灵寿:%s");
                        this.add(GUI_STRING + "movespeedmof", "移速:%s");
                        this.add(GUI_STRING + "flyspeedmof", "飞速:%s");
                        this.add(GUI_STRING + "lucky", "机缘:%s");
                        this.add(GUI_STRING + "charm", "魅力:%s");
                        this.add(GUI_STRING + "tpao", "先天一窍:%s");
                        this.add(GUI_STRING + "insight", "悟性:%s");
                        this.add(GUI_STRING + "inspiration", "灵感:%s");
                        this.add(GUI_STRING + "mindsense", "神识:%s");
                        this.add(GUI_STRING + "body_age", "身龄:%s");
                        this.add(GUI_STRING + "mental_age", "心龄:%s");
                        this.add(GUI_STRING + "state_title", "道  枢");
                        this.add(GUI_STRING + "nick_name", "道号:%s");
                        this.add(GUI_STRING + "sect", "宗门:%s");
                        this.add(GUI_STRING + "realm", "境界: ");
                        this.add(GUI_STRING + "make_cultivation_component", "衍化");
                        this.add(GUI_STRING + "input_cultivation_name", "请输入名称");
                        this.add(GUI_STRING + "input_cultivation_description", "描述 (可换行)");
                        this.add(GUI_STRING + "give_up", "放弃");
                        this.add(GUI_STRING + "make_cultivation_spell", "神通");
                        this.add(GUI_STRING + "make_cultivation_art", "功法");
                        this.add(GUI_STRING + "make_cultivation_realm", "修炼体系");
                        this.add(GUI_STRING + "create_cultivation", "衍化");
                        this.add(GUI_STRING + "make_tips", "Shift+左键=删除 Ctrl+左键=连接 中建=居中");
                        this.add(GUI_STRING + "component_lib_title", "已知组件");
                        this.add(GUI_STRING + "not_component", "非组件");
                        this.add(ENERGY_STRING + "lingqi", "灵气");
                        this.add(ENERGY_STRING + "neili", "内力");
                        this.add(BasicRealm.getRealms()[0].getName(), "凡人");
                        this.add(BasicRealm.getRealms()[1].getName(), "练气");
                        this.add(BasicRealm.getRealms()[2].getName(), "筑基");
                        this.add(BasicRealm.getRealms()[3].getName(), "金丹");
                        this.add(BasicRealm.getRealms()[4].getName(), "元婴");
                        this.add(MISC_STRING + "entity_now_sect", "无门无派");
                        this.add(MISC_STRING + "entity_nick_name", "无道号");
                        this.add(GUI_STRING + "cultivate_title_btn", "修炼");
                        this.add(GUI_STRING + "level_progress_gui_title", "闭关修炼");
                        this.add(GUI_STRING + "cultivate_time_year", "%s年%s月");
                        this.add(GUI_STRING + "player_time_year", "%s岁");
                        this.add(GUI_STRING + "input_time_month", "修炼月数");
                        this.add(GUI_STRING + "start_train", "开始修炼");
                        this.add(GUI_STRING + "refining", "炼化");
                        this.add(GUI_STRING + "not_use_meditation", "无法打坐，你并未入门");
                        this.add(GUI_STRING + "battle_mode", "战  斗");
                        this.add(GUI_STRING + "battle_release_hint", "按 ~ 释 放");
                        this.add(GUI_STRING + "bind_title", "绑定招式：%s");
                        this.add(GUI_STRING + "bind_keys_hint", "按键 R/G/C/V/B 组合，可重复，最多 4 个");
                        this.add(GUI_STRING + "bind_confirm_hint", "右键/回车=绑定(空=解除)  退格=删除  Esc=取消");
                        this.add(GUI_STRING + "wheel_hint", "点击或按数字键选择释放，Esc 取消");
                        this.add("key.xuanlingmod.keys", "玄灵");
                        this.add("key.xuanlingmod.left_alt", "战斗模式");
                        this.add("key.xuanlingmod.opengui", "打开道枢界面");
                        this.add("key.xuanlingmod.meditation", "打坐");
                        this.add("key.xuanlingmod.g", "搓招 G");
                        this.add("key.xuanlingmod.c", "搓招 C");
                        this.add("key.xuanlingmod.v", "搓招 V");
                        this.add("key.xuanlingmod.b", "搓招 B");
                        this.add("key.xuanlingmod.release", "释放神通");
                        this.add(COMPONEN_STRING + "basic_type", "基础");
                        this.add(COMPONEN_STRING + "projectle", "弹射物");
                        this.add(COMPONEN_STRING + "aoe", "AOE");
                        this.add(COMPONEN_STRING + "self", "自身");
                        this.add(COMPONEN_STRING + "action", "触发效果");
                        this.add(COMPONEN_STRING + "damage", "伤害");
                        this.add(COMPONEN_STRING + "recover_health", "生命恢复");
                        this.add(COMPONEN_STRING + "recover_p_energy", "主能量恢复");
                        this.add(COMPONEN_STRING + "recover_c_energy", "子能量恢复");
                        this.add(COMPONEN_STRING + "special", "特殊效果");
                        this.add(COMPONEN_STRING + "normal", "常规");
                        this.add(COMPONEN_STRING + "continuos", "持续");
                        this.add(GUI_STRING + "cultivate_time_month", "%s 月");
                        this.add(GUI_STRING + "this_component_not_select_more", "该组件无法衍化更多");
                        this.add(GUI_STRING + "detail_data_gui_title", "详细数据");
                        this.add(GUI_STRING + "physical_defense", "体防:%s");
                        this.add(GUI_STRING + "magic_defense", "法防:%s");
                        this.add(GUI_STRING + "arcane_mastery", "通玄");
                        this.add(GUI_STRING + "arcane_mastery_gui_title", "通玄");
                        this.add(GUI_STRING + "arcane_mastery_back", "返回");
                        this.add(GUI_STRING + "arcane_mastery_art_title", "功法");
                        this.add(GUI_STRING + "arcane_mastery_spell_title", "神通");
                        this.add(GUI_STRING + "arcane_mastery_empty_slot", "空");
                        this.add(GUI_STRING + "the_component_not_join_group", "该组件无法放入此分类！");
                        this.add(GUI_STRING + "create_name_empty", "名称不能为空");
                        this.add(GUI_STRING + "create_name_invalid", "名称含非法字符或过长");
                        this.add(GUI_STRING + "create_component_empty", "画布为空，请先添加组件");
                        this.add(GUI_STRING + "create_component_not_connected", "请先将所有组件连接起来");
                        this.add(GUI_STRING + "create_not_support", "该修炼体系暂不支持衍化");
                        this.add(GUI_STRING + "create_cultivation_success", "§a衍化成功：%s");
                        this.add(GUI_STRING + "create_cultivation_failed", "衍化失败，组件组合不完整");
                        
                        this.add("item.xuanling.talisman_paper.empty", "空白玉简");
                        this.add("item.xuanling.talisman_paper.type", "类型: %s");
                        this.add("item.xuanling.talisman_paper.nickname", "昵称: %s");
                        this.add("item.xuanling.talisman_paper.speed", "秒速: %s");
                        
                        this.add("xuanling.type.gong_fa", "功法");
                        this.add("xuanling.type.shen_tong", "神通");
                        this.add("xuanling.type.jing_jie", "境界");
                        this.add("xuanling.type.neng_liang", "能量");
                        
                        this.add("item.xuanling.example_block", "示例方块");
                        this.add("item.xuanling.example_item", "示例物品");
                        this.add("item.xuanling.jade_slip", "玉简");
                        this.add("item.xuanling.jade_slip.tooltip.realm", "境界: %s");
                        this.add("item.xuanling.jade_slip.tooltip.level", "等级: %d");
                        this.add("item.xuanling.jade_slip.tooltip.exp", "经验: %d/%d");
                        this.add("item.xuanling.jade_slip.tooltip.source", "来源: %s");
                        this.add("item.xuanling.jade_slip.tooltip.killer", "击杀者: %s");
                        this.add("item.xuanling.jade_slip.tooltip.time", "时间: %s");
                        this.add("item.xuanling.jade_slip.tooltip.description", "蕴含修仙境界感悟");
                        this.add("item.xuanling.jade_slip.rarity.common", "普通");
                        this.add("item.xuanling.jade_slip.rarity.uncommon", "优秀");
                        this.add("item.xuanling.jade_slip.rarity.rare", "稀有");
                        this.add("item.xuanling.jade_slip.rarity.epic", "史诗");
                        this.add("item.xuanling.jade_slip.rarity.legendary", "传说");
                        this.add("item.xuanling.jade_slip.rarity.mythic", "神话");
                        this.add("item.xuanling.jade_slip.type.realm", "境界玉简");
                        this.add("item.xuanling.jade_slip.type.gongfa", "功法玉简");
                        this.add("item.xuanling.jade_slip.type.shentong", "神通玉简");
                        this.add("item.xuanling.jade_slip.type.mixed", "混合玉简");
                        this.add("item.xuanling.body_ex_stale_item", "停滞之躯");
                        
                        this.add("block.xuanling.example_block", "示例方块");
                        this.add("block.xuanling.body_ex_stale_block", "停滞之躯方块");
                        
                        this.add("entity.xuanling.skill_entity", "技能实体");
                        this.add("entity.xuanling.eject_entity", "弹射实体");
                        
                        this.add("effect.xuanling.meditation", "冥想");
                        this.add("effect.xuanling.deviation", "走火入魔");
                        this.add("effect.xuanling.critical_injury", "重伤");
                }
        }

        public static final class LangEn extends LanguageProvider {

                public LangEn(PackOutput output) {
                        super(output, XuanLing.MODID, "en_us");
                }

                @Override
                protected void addTranslations() {
                        this.add(GUI_STRING + "normallife", "Normal Life:%s");
                        this.add(GUI_STRING + "soullife", "Soul Life:%s");
                        this.add(GUI_STRING + "movespeedmof", "Move Speed:%s");
                        this.add(GUI_STRING + "flyspeedmof", "FlySpeed:%s");
                        this.add(GUI_STRING + "lucky", "Lucky:%s");
                        this.add(GUI_STRING + "charm", "Charm:%s");
                        this.add(GUI_STRING + "tpao", "TPAO:%s");
                        this.add(GUI_STRING + "insight", "Insight:%s");
                        this.add(GUI_STRING + "inspiration", "Inspiration:%s");
                        this.add(GUI_STRING + "mindsense", "Mind Sense:%s");
                        this.add(GUI_STRING + "body_age", "Body Age:%s");
                        this.add(GUI_STRING + "mental_age", "Mental Age:%s");
                        this.add(GUI_STRING + "state_title", "State Title");
                        this.add(GUI_STRING + "nick_name", "Nick Name:%s");
                        this.add(GUI_STRING + "sect", "Sect:%s");
                        this.add(GUI_STRING + "realm", "Realm: ");
                        this.add(GUI_STRING + "make_cultivation_component", "Make Cultivation Component");
                        this.add(GUI_STRING + "input_cultivation_name", "Input name");
                        this.add(GUI_STRING + "input_cultivation_description", "Description (multi-line)");
                        this.add(GUI_STRING + "give_up", "Give up");
                        this.add(GUI_STRING + "make_cultivation_spell", "Spell");
                        this.add(GUI_STRING + "make_cultivation_art", "ART");
                        this.add(GUI_STRING + "make_cultivation_realm", "Realm");
                        this.add(GUI_STRING + "create_cultivation", "Create");
                        this.add(GUI_STRING + "make_tips", "Shift+Left=delete Ctrl+Left=Connect Middle=Center");
                        this.add(GUI_STRING + "component_lib_title", "Component Lib");
                        this.add(GUI_STRING + "not_component", "Not Component");
                        this.add(ENERGY_STRING + "lingqi", "LingQi");
                        this.add(ENERGY_STRING + "neili", "NeiLi");
                        this.add(BasicRealm.getRealms()[0].getName(), "FanRen");
                        this.add(BasicRealm.getRealms()[1].getName(), "LianQi");
                        this.add(BasicRealm.getRealms()[2].getName(), "ZhuJi");
                        this.add(BasicRealm.getRealms()[3].getName(), "JinDan");
                        this.add(BasicRealm.getRealms()[4].getName(), "YuanYing");
                        this.add(MISC_STRING + "entity_now_sect", "None Sect");
                        this.add(MISC_STRING + "entity_nick_name", "None NickName");
                        this.add(GUI_STRING + "cultivate_title_btn", "Cultivate");
                        this.add(GUI_STRING + "level_progress_gui_title", "Secluding myself for training");
                        this.add(GUI_STRING + "cultivate_time_year", "%sYear");
                        this.add(GUI_STRING + "input_time_month", "training month");
                        this.add(GUI_STRING + "start_train", "Start Train");
                        this.add(GUI_STRING + "refining", "Refining");
                        this.add(GUI_STRING + "not_use_meditation",
                                        "You’re not ready to use meditation because you haven’t learned the basics yet.");
                        this.add(GUI_STRING + "battle_mode", "Combat");
                        this.add(GUI_STRING + "battle_release_hint", "Press ~ to release");
                        this.add(GUI_STRING + "bind_title", "Bind Move: %s");
                        this.add(GUI_STRING + "bind_keys_hint", "Keys R/G/C/V/B, repeatable, up to 4");
                        this.add(GUI_STRING + "bind_confirm_hint", "Right-click/Enter=Bind(empty=unbind)  Backspace=Delete  Esc=Cancel");
                        this.add(GUI_STRING + "wheel_hint", "Click or press number to release, Esc to cancel");
                        this.add("key.xuanlingmod.keys", "XuanLing");
                        this.add("key.xuanlingmod.left_alt", "Battle Mode");
                        this.add("key.xuanlingmod.opengui", "Open State Gui");
                        this.add("key.xuanlingmod.meditation", "Meditation");
                        this.add("key.xuanlingmod.g", "Combo G");
                        this.add("key.xuanlingmod.c", "Combo C");
                        this.add("key.xuanlingmod.v", "Combo V");
                        this.add("key.xuanlingmod.b", "Combo B");
                        this.add("key.xuanlingmod.release", "Release Spell");
                        this.add(COMPONEN_STRING + "basic_type", "basic");
                        this.add(COMPONEN_STRING + "projectle", "Projectle");
                        this.add(COMPONEN_STRING + "aoe", "Aoe");
                        this.add(COMPONEN_STRING + "self", "Self");
                        this.add(COMPONEN_STRING + "action", "action");
                        this.add(COMPONEN_STRING + "damage", "Damage");
                        this.add(COMPONEN_STRING + "recover_health", "Health");
                        this.add(COMPONEN_STRING + "recover_p_energy", "Parrent Energy");
                        this.add(COMPONEN_STRING + "recover_c_energy", "Children Energy");
                        this.add(COMPONEN_STRING + "special", "special");
                        this.add(COMPONEN_STRING + "normal", "Normal");
                        this.add(COMPONEN_STRING + "continuous", "Continuous");
                        this.add(GUI_STRING + "this_component_not_select_more", "This component not select more");
                        this.add(GUI_STRING + "detail_data_gui_title", "Detail Data");
                        this.add(GUI_STRING + "cultivate_time_month", "%s months");
                        this.add(GUI_STRING + "arcane_mastery", "Arcane Mastery");
                        this.add(GUI_STRING + "arcane_mastery_gui_title", "Arcane_Mastery");
                        this.add(GUI_STRING + "arcane_mastery_art_title", "Art Collection");
                        this.add(GUI_STRING + "arcane_mastery_spell_title", "Spell Collection");
                        this.add(GUI_STRING + "arcane_mastery_back", "Back");
                        this.add(GUI_STRING + "arcane_mastery_empty_slot", "Empty");
                        this.add(GUI_STRING + "the_component_not_join_group", "The component not join group！");
                        this.add(GUI_STRING + "create_name_empty", "Name cannot be empty");
                        this.add(GUI_STRING + "create_name_invalid", "Name contains invalid characters or too long");
                        this.add(GUI_STRING + "create_component_empty", "Canvas is empty, add components first");
                        this.add(GUI_STRING + "create_component_not_connected", "Connect all components first");
                        this.add(GUI_STRING + "create_not_support", "This cultivation system cannot be created yet");
                        this.add(GUI_STRING + "create_cultivation_success", "§aCreated: %s");
                        this.add(GUI_STRING + "create_cultivation_failed", "Create failed, component set incomplete");
                        
                        this.add("item.xuanling.talisman_paper.empty", "Blank Jade Slip");
                        this.add("item.xuanling.talisman_paper.type", "Type: %s");
                        this.add("item.xuanling.talisman_paper.nickname", "Nickname: %s");
                        this.add("item.xuanling.talisman_paper.speed", "Speed: %s");
                        
                        this.add("xuanling.type.gong_fa", "Art");
                        this.add("xuanling.type.shen_tong", "Spell");
                        this.add("xuanling.type.jing_jie", "Realm");
                        this.add("xuanling.type.neng_liang", "Energy");
                        
                        this.add("item.xuanling.example_block", "Example Block");
                        this.add("item.xuanling.example_item", "Example Item");
                        this.add("item.xuanling.jade_slip", "Jade Slip");
                        this.add("item.xuanling.jade_slip.tooltip.realm", "Realm: %s");
                        this.add("item.xuanling.jade_slip.tooltip.level", "Level: %d");
                        this.add("item.xuanling.jade_slip.tooltip.exp", "Exp: %d/%d");
                        this.add("item.xuanling.jade_slip.tooltip.source", "Source: %s");
                        this.add("item.xuanling.jade_slip.tooltip.killer", "Killer: %s");
                        this.add("item.xuanling.jade_slip.tooltip.time", "Time: %s");
                        this.add("item.xuanling.jade_slip.tooltip.description", "Contains cultivation realm insights");
                        this.add("item.xuanling.jade_slip.rarity.common", "Common");
                        this.add("item.xuanling.jade_slip.rarity.uncommon", "Uncommon");
                        this.add("item.xuanling.jade_slip.rarity.rare", "Rare");
                        this.add("item.xuanling.jade_slip.rarity.epic", "Epic");
                        this.add("item.xuanling.jade_slip.rarity.legendary", "Legendary");
                        this.add("item.xuanling.jade_slip.rarity.mythic", "Mythic");
                        this.add("item.xuanling.jade_slip.type.realm", "Realm Jade Slip");
                        this.add("item.xuanling.jade_slip.type.gongfa", "Gongfa Jade Slip");
                        this.add("item.xuanling.jade_slip.type.shentong", "Shentong Jade Slip");
                        this.add("item.xuanling.jade_slip.type.mixed", "Mixed Jade Slip");
                        this.add("item.xuanling.body_ex_stale_item", "Body Ex Stale Item");
                        
                        this.add("block.xuanling.example_block", "Example Block");
                        this.add("block.xuanling.body_ex_stale_block", "Body Ex Stale Block");
                        
                        this.add("entity.xuanling.skill_entity", "Skill Entity");
                        this.add("entity.xuanling.eject_entity", "Eject Entity");
                        
                        this.add("effect.xuanling.meditation", "Meditation");
                        this.add("effect.xuanling.deviation", "Deviation");
                        this.add("effect.xuanling.critical_injury", "Critical Injury");
                }
        }
}
