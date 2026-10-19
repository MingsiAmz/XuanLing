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
                        this.add(GUI_STRING + "state_title", "道  枢");
                        this.add(GUI_STRING + "nick_name", "道号:%s");
                        this.add(GUI_STRING + "sect", "宗门:%s");
                        this.add(GUI_STRING + "realm", "境界: ");
                        this.add(GUI_STRING + "make_cultivation_component", "衍化神通");
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
                        this.add(GUI_STRING + "state_title", "State Title");
                        this.add(GUI_STRING + "nick_name", "Nick Name:%s");
                        this.add(GUI_STRING + "sect", "Sect:%s");
                        this.add(GUI_STRING + "realm", "Realm: ");
                        this.add(GUI_STRING + "make_cultivation_component", "Make Cultivation Component");
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
                }
        }
}
