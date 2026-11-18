package com.mingsiamz.xuanlingmod.data;

import com.mingsiamz.xuanlingmod.Config;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public class BasicData {
        private double normalLife = 100.0;
        private double soulLife = 100.0;
        private double moveSpeedMof = 1.0;
        private double flySpeedMof = 1.0;
        private double lucky = 0.0;
        private double charm = 0.0;
        private double TPAO = 0.0;
        private double insight = 0.0;
        private double inspiration = 0.0;
        private double mindSense = 0.0;
        private boolean isMeditation = false;

        /** 道德值：正向行为提升，负向行为降低；高值可减轻/简化天劫 */
        private int morality = 0;
        /** 天谴值：负向行为累积，高值增加天劫伤害并可能触发非突破时的天罚 */
        private int condemnation = 0;
        /** 剩余寿命（月），每 7 个游戏天消耗 1 月，固定不随境界浮动 */
        private int lifespanMonth = 120;
        /** 上次扣除寿命时的世界天数（level.getDayTime()/24000），用于固定周期扣寿 */
        private long lastLifespanCheckDay = 0;
        /** 当前境界等级：0 凡人 … 4 元婴，突破 level4+ 需渡天劫 */
        private int realmLevel = 0;
        /** 当前修为，用于计算境界突破：needExp(level) = base × growthRate^level */
        private double realmExp = 0.0;
        /** 是否正在渡天劫中 */
        private boolean isTribulating = false;
        /** 天劫倒计时（tick），用于控制天劫持续时间和闪电频率 */
        private int tribulationTick = 0;

        /** 善恶标签：善 / 中立 / 恶 */
        public enum Alignment {
                GOOD, NEUTRAL, EVIL
        }
        /** 当前参悟等级 */
        private int comprehensionLevel = 0;
        /** 当前参悟值，用于计算参悟等级突破：needComprehensionExp(level) = base × growthRate^level */
        private double comprehensionExp = 0.0;
        /** 是否正在参悟中 */
        private boolean isComprehending = false;
        /** 参悟倒计时（tick），用于控制读条进度和寿命消耗 */
        private int comprehensionTick = 0;
        /** 当前参悟目标（功法/神通名称） */
        private String comprehensionTarget = "";
        
        private String sect = "";
        private String nickName = "";
        private double lastMediX = 0;
        private double lastMediY = 0;
        private double lastMediZ = 0;

        public static final Codec<BasicData> CODEC = RecordCodecBuilder.create(builder -> builder
                        .group(
                                        Codec.DOUBLE.fieldOf("normalLife").forGetter(BasicData::getNormalLife),
                                        Codec.DOUBLE.fieldOf("soul_life").forGetter(BasicData::getSoulLife),
                                        Codec.DOUBLE.fieldOf("move_speed_mof").forGetter(BasicData::getMoveSpeedMof),
                                        Codec.DOUBLE.fieldOf("fly_speed_mof").forGetter(BasicData::getFlySpeedMof),
                                        Codec.DOUBLE.fieldOf("TPAO").forGetter(BasicData::getTPAO),
                                        Codec.DOUBLE.fieldOf("lucky").forGetter(BasicData::getLucky),
                                        Codec.DOUBLE.fieldOf("charm").forGetter(BasicData::getCharm),
                                        Codec.DOUBLE.fieldOf("insight").forGetter(BasicData::getInsight),
                                        Codec.DOUBLE.fieldOf("inspiration").forGetter(BasicData::getInspiration),
                                        Codec.DOUBLE.fieldOf("mind_sense").forGetter(BasicData::getMindSense),
                                        Codec.INT.fieldOf("morality").orElse(0).forGetter(BasicData::getMorality),
                                        Codec.INT.fieldOf("condemnation").orElse(0).forGetter(BasicData::getCondemnation),
                                        Codec.INT.fieldOf("lifespan_month").orElse(120).forGetter(BasicData::getLifespanMonth),
                                        Codec.LONG.fieldOf("last_lifespan_check_day").orElse(0L).forGetter(BasicData::getLastLifespanCheckDay),
                                        Codec.STRING.fieldOf("sect").forGetter(BasicData::getSect),
                                        Codec.STRING.fieldOf("nick_name").forGetter(BasicData::getNickName)
                        )
                        .apply(builder, BasicData::fromCodec));

        public static final StreamCodec<RegistryFriendlyByteBuf, BasicData> STREAM_CODEC = StreamCodec
                        .ofMember(BasicData::encode, BasicData::new);

        public BasicData() {
        }

        private BasicData(double normalLife, double soulLife, double moveSpeedMof, double flySpeedMof,
                          double TPAO, double lucky, double charm, double insight, double inspiration,
                          double mindSense, boolean isMeditation, int morality, int condemnation, int lifespanMonth,
                          long lastLifespanCheckDay, String sect, String nickName,
                          double lastMediX, double lastMediY, double lastMediZ,
                          int realmLevel, double realmExp, boolean isTribulating, int tribulationTick, int comprehensionLevel,
            double comprehensionExp, boolean isComprehending, int comprehensionTick, String comprehensionTarget) {
            this.normalLife = normalLife;
            this.soulLife = soulLife;
            this.moveSpeedMof = moveSpeedMof;
            this.flySpeedMof = flySpeedMof;
            this.TPAO = TPAO;
            this.lucky = lucky;
            this.charm = charm;
            this.insight = insight;
            this.inspiration = inspiration;
            this.mindSense = mindSense;
            this.isMeditation = isMeditation;
            this.morality = morality;
            this.condemnation = condemnation;
            this.lifespanMonth = lifespanMonth;
            this.lastLifespanCheckDay = lastLifespanCheckDay;
            this.sect = sect;
            this.nickName = nickName;
            this.lastMediX = lastMediX;
            this.lastMediY = lastMediY;
            this.lastMediZ = lastMediZ;
            this.realmLevel = realmLevel;
            this.realmExp = realmExp;
            this.isTribulating = isTribulating;
            this.tribulationTick = tribulationTick;
            this.comprehensionLevel = comprehensionLevel;
            this.comprehensionExp = comprehensionExp;
            this.isComprehending = isComprehending;
            this.comprehensionTick = comprehensionTick;
            this.comprehensionTarget = comprehensionTarget;
        }

        private static BasicData fromCodec(double normalLife, double soulLife, double moveSpeedMof, double flySpeedMof,
                                           double TPAO, double lucky, double charm, double insight,
                                           double inspiration, double mindSense, int morality, int condemnation,
                                           int lifespanMonth, long lastLifespanCheckDay, String sect, String nickName) {
            return new BasicData(normalLife, soulLife, moveSpeedMof, flySpeedMof, TPAO, lucky, charm, insight,
                                 inspiration, mindSense, false, morality, condemnation, lifespanMonth,
                                 lastLifespanCheckDay, sect, nickName, 0, 0, 0, 0, 0.0, false, 0, 0, 0.0, false, 0, "");
        }

        public BasicData(RegistryFriendlyByteBuf buf) {
                this.normalLife = buf.readDouble();
                this.soulLife = buf.readDouble();
                this.moveSpeedMof = buf.readDouble();
                this.flySpeedMof = buf.readDouble();
                this.TPAO = buf.readDouble();
                this.lucky = buf.readDouble();
                this.charm = buf.readDouble();
                this.insight = buf.readDouble();
                this.inspiration = buf.readDouble();
                this.mindSense = buf.readDouble();
                this.isMeditation = buf.readBoolean();
                this.morality = buf.readInt();
                this.condemnation = buf.readInt();
                this.lifespanMonth = buf.readInt();
                this.lastLifespanCheckDay = buf.readLong();
                this.sect = buf.readUtf();
                this.nickName = buf.readUtf();
                this.lastMediX = buf.readDouble();
                this.lastMediY = buf.readDouble();
                this.lastMediZ = buf.readDouble();
                this.realmLevel = buf.readInt();
                this.realmExp = buf.readDouble();
                this.isTribulating = buf.readBoolean();
                this.tribulationTick = buf.readInt();
                this.comprehensionLevel = buf.readInt();
                this.comprehensionExp = buf.readDouble();
                this.isComprehending = buf.readBoolean();
                this.comprehensionTick = buf.readInt();
                this.comprehensionTarget = buf.readUtf();
        }

        public BasicData(
                        double normalLife,
                        double soulLife,
                        double moveSpeedMof,
                        double flySpeedMof,
                        double TPAO,
                        double lucky,
                        double charm,
                        double insight,
                        double inspiration,
                        double mindSense,
                        boolean isMeditation,
                        int morality,
                        int condemnation,
                        int lifespanMonth,
                        long lastLifespanCheckDay,
                        String sect,
                        String nickName,
                        double lastMediX,
                        double lastMediY,
                        double lastMediZ,
                        int realmLevel,
                        double realmExp,
                        int comprehensionLevel,
                        double comprehensionExp,
                        boolean isComprehending,
                        int comprehensionTick,
                        String comprehensionTarget) {
                this.normalLife = normalLife;
                this.soulLife = soulLife;
                this.moveSpeedMof = moveSpeedMof;
                this.flySpeedMof = flySpeedMof;
                this.TPAO = TPAO;
                this.lucky = lucky;
                this.charm = charm;
                this.insight = insight;
                this.inspiration = inspiration;
                this.isMeditation = isMeditation;
                this.morality = morality;
                this.condemnation = condemnation;
                this.lifespanMonth = lifespanMonth;
                this.lastLifespanCheckDay = lastLifespanCheckDay;
                this.sect = sect;
                this.nickName = nickName;
                this.lastMediX = lastMediX;
                this.lastMediY = lastMediY;
                this.lastMediZ = lastMediZ;
                this.realmLevel = realmLevel;
                this.realmExp = realmExp;
                this.comprehensionLevel = comprehensionLevel;
                this.comprehensionExp = comprehensionExp;
                this.isComprehending = isComprehending;
                this.comprehensionTick = comprehensionTick;
                this.comprehensionTarget = comprehensionTarget;
        }

        public double getNormalLife() {
                return normalLife;
        }

        public void setNormalLife(double normalLife) {
                if (normalLife >= Config.getMaxValue(Config.RANGE_NORMAL_LIFE)) {
                        normalLife = Config.getMaxValue(Config.RANGE_NORMAL_LIFE);
                } else if (normalLife <= Config.getMinValue(Config.RANGE_NORMAL_LIFE)) {
                        normalLife = Config.getMinValue(Config.RANGE_NORMAL_LIFE);
                }
                this.normalLife = normalLife;
        }

        public double getSoulLife() {
                return soulLife;
        }

        public void setSoulLife(double soulLife) {
                if (soulLife >= Config.getMaxValue(Config.RANGE_SOUL_LIFE)) {
                        soulLife = Config.getMaxValue(Config.RANGE_SOUL_LIFE);
                } else if (soulLife <= Config.getMinValue(Config.RANGE_SOUL_LIFE)) {
                        soulLife = Config.getMinValue(Config.RANGE_SOUL_LIFE);
                }
                this.soulLife = soulLife;
        }

        public double getMoveSpeedMof() {
                return moveSpeedMof;
        }

        public void setMoveSpeedMof(double moveSpeedMof) {
                if (moveSpeedMof >= Config.getMaxValue(Config.RANGE_MOVE_SPEED)) {
                        moveSpeedMof = Config.getMaxValue(Config.RANGE_MOVE_SPEED);
                } else if (moveSpeedMof <= Config.getMinValue(Config.RANGE_MOVE_SPEED)) {
                        moveSpeedMof = Config.getMinValue(Config.RANGE_MOVE_SPEED);
                }
                this.moveSpeedMof = moveSpeedMof;
        }

        public double getFlySpeedMof() {
                return flySpeedMof;
        }

        public void setFlySpeedMof(double flySpeedMof) {
                if (flySpeedMof >= Config.getMaxValue(Config.RANGE_FLY_SPEED)) {
                        flySpeedMof = Config.getMaxValue(Config.RANGE_FLY_SPEED);
                } else if (flySpeedMof <= Config.getMinValue(Config.RANGE_FLY_SPEED)) {
                        flySpeedMof = Config.getMinValue(Config.RANGE_FLY_SPEED);
                }
                this.flySpeedMof = flySpeedMof;
        }

        public double getTPAO() {
                return TPAO;
        }

        public void setTPAO(double tPAO) {
                if (tPAO >= Config.getMaxValue(Config.RANGE_TPAO)) {
                        tPAO = Config.getMaxValue(Config.RANGE_TPAO);
                } else if (tPAO <= Config.getMinValue(Config.RANGE_TPAO)) {
                        tPAO = Config.getMinValue(Config.RANGE_TPAO);
                }
                TPAO = tPAO;
        }

        public double getLucky() {
                return lucky;
        }

        public void setLucky(double lucky) {
                if (lucky >= Config.getMaxValue(Config.RANGE_LUCKY)) {
                        lucky = Config.getMaxValue(Config.RANGE_LUCKY);
                } else if (lucky <= Config.getMinValue(Config.RANGE_LUCKY)) {
                        lucky = Config.getMinValue(Config.RANGE_LUCKY);
                }
                this.lucky = lucky;
        }

        public double getCharm() {
                return charm;
        }

        public void setCharm(double charm) {
                if (charm >= Config.getMaxValue(Config.RANGE_CHARM)) {
                        charm = Config.getMaxValue(Config.RANGE_CHARM);
                } else if (charm <= Config.getMinValue(Config.RANGE_CHARM)) {
                        charm = Config.getMinValue(Config.RANGE_CHARM);
                }
                this.charm = charm;
        }

        public double getInsight() {
                return insight;
        }

        public void setInsight(double insight) {
                if (insight >= Config.getMaxValue(Config.RANGE_INSIGHT)) {
                        insight = Config.getMaxValue(Config.RANGE_INSIGHT);
                } else if (insight <= Config.getMinValue(Config.RANGE_INSIGHT)) {
                        insight = Config.getMinValue(Config.RANGE_INSIGHT);
                }
                this.insight = insight;
        }

        public double getInspiration() {
                return inspiration;
        }

        public void setInspiration(double inspiration) {
                if (inspiration >= Config.getMaxValue(Config.RANGE_INSPIRATION)) {
                        inspiration = Config.getMaxValue(Config.RANGE_INSPIRATION);
                } else if (inspiration <= Config.getMinValue(Config.RANGE_INSPIRATION)) {
                        inspiration = Config.getMinValue(Config.RANGE_INSPIRATION);
                }
                this.inspiration = inspiration;
        }

        public void setMindSense(double mindSense) {
                if (mindSense >= Config.getMaxValue(Config.RANGE_M_SENSE)) {
                        mindSense = Config.getMaxValue(Config.RANGE_M_SENSE);
                } else if (mindSense <= Config.getMinValue(Config.RANGE_M_SENSE)) {
                        mindSense = Config.getMinValue(Config.RANGE_M_SENSE);
                }
                this.mindSense = mindSense;
        }

        public double getMindSense() {
                return mindSense;
        }

        public void setMeditation(boolean isMeditation) {
                this.isMeditation = isMeditation;
        }

        public boolean isMeditation() {
                return isMeditation;
        }

        public int getMorality() {
                return morality;
        }

        public void setMorality(int morality) {
                int max = Config.getMaxValue(Config.MORALITY_MAX);
                if (morality >= max) {
                        morality = max;
                } else if (morality <= Config.getMinValue(Config.MORALITY_MAX)) {
                        morality = Config.getMinValue(Config.MORALITY_MAX);
                }
                this.morality = morality;
        }

        public int getCondemnation() {
                return condemnation;
        }

        public void setCondemnation(int condemnation) {
                int max = Config.getMaxValue(Config.CONDEMNATION_MAX);
                if (condemnation >= max) {
                        condemnation = max;
                } else if (condemnation <= Config.getMinValue(Config.CONDEMNATION_MAX)) {
                        condemnation = Config.getMinValue(Config.CONDEMNATION_MAX);
                }
                this.condemnation = condemnation;
        }

        /**
         * 按 morality / (morality + condemnation) 占比计算善恶标签
         * 分母为 0 时视为中立；>= goodThreshold 为善；<= evilThreshold 为恶；其余中立
         *
         * @return 当前的善恶标签
         */
        public Alignment getAlignmentLabel() {
                int total = morality + condemnation;
                if (total <= 0) {
                        return Alignment.NEUTRAL;
                }
                double ratio = (double) morality / total;
                if (ratio >= Config.getAlignmentGoodThreshold()) {
                        return Alignment.GOOD;
                }
                if (ratio <= Config.getAlignmentEvilThreshold()) {
                        return Alignment.EVIL;
                }
                return Alignment.NEUTRAL;
        }

        public int getLifespanMonth() {
                return lifespanMonth;
        }

        public void setLifespanMonth(int lifespanMonth) {
                if (lifespanMonth < 0) {
                        lifespanMonth = 0;
                }
                this.lifespanMonth = lifespanMonth;
        }

        public long getLastLifespanCheckDay() {
                return lastLifespanCheckDay;
        }

        public void setLastLifespanCheckDay(long lastLifespanCheckDay) {
                this.lastLifespanCheckDay = lastLifespanCheckDay;
        }

        public void setSect(String sect) {
                this.sect = sect;
        }

        public String getSect() {
                return this.sect;
        }

        public void setNickName(String nickName) {
                this.nickName = nickName;
        }

        public String getNickName() {
                return this.nickName;
        }

        public void setLastMediX(double lastMediX) {
                this.lastMediX = lastMediX;
        }

        public double getLastMediX() {
                return lastMediX;
        }

        public void setLastMediY(double lastMediY) {
                this.lastMediY = lastMediY;
        }

        public double getLastMediY() {
                return lastMediY;
        }
        
        public void setLastMediZ(double lastMediZ) {
                this.lastMediZ = lastMediZ;
        }

        public double getLastMediZ() {
                return lastMediZ;
        }

        public int getRealmLevel() {
                return realmLevel;
        }

        public void setRealmLevel(int realmLevel) {
                this.realmLevel = realmLevel;
        }

        public double getRealmExp() {
                return realmExp;
        }

        public void setRealmExp(double realmExp) {
                this.realmExp = realmExp;
        }

        public boolean isTribulating() {
                return isTribulating;
        }

        public void setTribulating(boolean isTribulating) {
                this.isTribulating = isTribulating;
        }

        public int getTribulationTick() {
                return tribulationTick;
        }

        public void setTribulationTick(int tribulationTick) {
                this.tribulationTick = tribulationTick;
        }

        public void setLastPos(Vec3 vec3) {
                this.lastMediX = vec3.x;
                this.lastMediY = vec3.y;
                this.lastMediZ = vec3.z;
        }

        public Vec3 getLastPos() {
                return new Vec3(this.lastMediX, this.lastMediY, this.lastMediZ);
        }

        public void encode(RegistryFriendlyByteBuf buf) {
                buf.writeDouble(this.normalLife);
                buf.writeDouble(this.soulLife);
                buf.writeDouble(this.moveSpeedMof);
                buf.writeDouble(this.flySpeedMof);
                buf.writeDouble(this.TPAO);
                buf.writeDouble(this.lucky);
                buf.writeDouble(this.charm);
                buf.writeDouble(this.insight);
                buf.writeDouble(this.inspiration);
                buf.writeDouble(this.mindSense);
                buf.writeBoolean(this.isMeditation);
                buf.writeInt(this.morality);
                buf.writeInt(this.condemnation);
                buf.writeInt(this.lifespanMonth);
                buf.writeLong(this.lastLifespanCheckDay);
                buf.writeUtf(this.sect);
                buf.writeUtf(this.nickName);
                buf.writeDouble(this.lastMediX);
                buf.writeDouble(this.lastMediY);
                buf.writeDouble(this.lastMediZ);
                buf.writeInt(this.realmLevel);
                buf.writeDouble(this.realmExp);
                buf.writeBoolean(this.isTribulating);
                buf.writeInt(this.tribulationTick);
                buf.writeInt(this.comprehensionLevel);
                buf.writeDouble(this.comprehensionExp);
                buf.writeBoolean(this.isComprehending);
                buf.writeInt(this.comprehensionTick);
                buf.writeUtf(this.comprehensionTarget);
        }

        public static BasicData initData() {
                BasicData data = new BasicData();
                double randTPAO = XuanLing.RANDOM.nextDouble(Config.getMinValue(Config.RANGE_TPAO), 100) + 50;
                data.setTPAO(randTPAO);
                double randInsight = XuanLing.RANDOM.nextDouble(Config.getMinValue(Config.RANGE_INSIGHT), 100) + 20;
                data.setInsight(randInsight);
                double randLucky = XuanLing.RANDOM.nextDouble(Config.getMinValue(Config.RANGE_LUCKY), 100);
                data.setLucky(randLucky);
                double randCharm = XuanLing.RANDOM.nextDouble(Config.getMinValue(Config.RANGE_CHARM), 100);
                data.setCharm(randCharm);
                double randMSense = XuanLing.RANDOM.nextDouble(Config.getMinValue(Config.RANGE_M_SENSE), 100) + 10;
                data.setMindSense(randMSense);
                return data;
        }

        @Override
        public String toString() {
                String str = "normalLife:" + (int) normalLife + "\n" +
                                "soulLife:" + (int) soulLife + "\n" +
                                "moveSpeedMof:" + (int) moveSpeedMof + "\n" +
                                "flySpeedMof:" + (int) flySpeedMof + "\n" +
                                "lucky:" + (int) lucky + "\n" +
                                "charm:" + (int) charm + "\n" +
                                "TPAO:" + (int) TPAO + "\n" +
                                "insight:" + (int) insight + "\n" +
                                "inspiration:" + (int) inspiration + "\n" +
                                "mindSense:" + (int) mindSense;
                return str;
        }

        @Override
        public boolean equals(Object obj) {
                BasicData b = (BasicData) obj;
                return this.normalLife == b.normalLife
                                && this.soulLife == b.soulLife
                                && this.moveSpeedMof == b.moveSpeedMof
                                && this.flySpeedMof == b.flySpeedMof
                                && this.lucky == b.lucky
                                && this.charm == b.charm
                                && this.TPAO == b.TPAO
                                && this.insight == b.insight
                                && this.inspiration == b.inspiration
                                && this.mindSense == b.mindSense
                                && this.morality == b.morality
                                && this.condemnation == b.condemnation
                                && this.lifespanMonth == b.lifespanMonth
                                && this.lastLifespanCheckDay == b.lastLifespanCheckDay
                                && this.realmLevel == b.realmLevel
                                && Double.compare(this.realmExp, b.realmExp) == 0
                                && this.isTribulating == b.isTribulating
                                && this.tribulationTick == b.tribulationTick
                                && this.comprehensionLevel == b.comprehensionLevel
                                && Double.compare(this.comprehensionExp, b.comprehensionExp) == 0
                                && this.isComprehending == b.isComprehending
                                && this.comprehensionTick == b.comprehensionTick
                                && this.comprehensionTarget.equals(b.comprehensionTarget);
        }

        public void setComprehending(boolean comprehending) {
                this.isComprehending = comprehending;
        }

        public void setComprehensionTick(int comprehensionTick) {
                this.comprehensionTick = comprehensionTick;
        }

        public void setComprehensionTarget(String comprehensionTarget) {
                this.comprehensionTarget = comprehensionTarget;
        }

        public static BasicData copy(BasicData data) {
                return new BasicData(data.normalLife, data.soulLife, data.moveSpeedMof, data.flySpeedMof, data.TPAO,
                                data.lucky, data.charm, data.insight, data.inspiration, data.mindSense,
                                     data.isMeditation, data.morality, data.condemnation, data.lifespanMonth, data.lastLifespanCheckDay,
                                     data.sect, data.nickName, data.lastMediX, data.lastMediY, data.lastMediZ,
                                     data.realmLevel, data.realmExp, data.isTribulating, data.tribulationTick,
                                     data.comprehensionLevel, data.comprehensionExp, data.isComprehending, data.comprehensionTick, data.comprehensionTarget);
        }

}
