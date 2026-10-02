package com.mingsiamz.xuanlingmod.provider;

import com.mingsiamz.xuanlingmod.XuanLing;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class XuanlingLangPro {

        public static final String GUI_STRING = XuanLing.MODID + "." + "gui" + ".";
        public static final String MISC_STRING = XuanLing.MODID + "." + "misc" + ".";

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
                }
        }
}
