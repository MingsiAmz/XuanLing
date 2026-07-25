package com.mingsiamz.xuanlingmod;


public enum XuanLingTypeEnum {
    ART("gong_fa"),
    SPELL("shen_tong"),
    REALM("jing_jie");
    public String name;
    private XuanLingTypeEnum(String name) {
	this.name = name;
    }
    
    static enum artComponent {

    }

    static enum spellComponent {
	
    }

    static enum realmComponent {
	
    }
}
