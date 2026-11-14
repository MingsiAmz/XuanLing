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
                        this.add(GUI_STRING + "make_cultivation_spell", "神通");
                        this.add(GUI_STRING + "make_cultivation_art", "功法");
                        this.add(GUI_STRING + "make_cultivation_realm", "修炼体系");
                        this.add(GUI_STRING + "create_cultivation", "衍化");
                        this.add(GUI_STRING + "make_tips", "Shift+左键=删除 Ctrl+左键=连接 中建=居中");
                        this.add(GUI_STRING + "component_lib_title", "已知组件");
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
                }
        }
}
