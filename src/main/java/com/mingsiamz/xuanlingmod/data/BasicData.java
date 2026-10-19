package com.mingsiamz.xuanlingmod.data;

import com.mingsiamz.xuanlingmod.Config;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class BasicData {
        private double normalLife = 100.0;
        private double soulLife = 100.0;
        private double moveSpeedMof = 1.0;
        private double flySpeedMof = 1.0;
        private double lucky = 0.0;
        private double charm = 0.0;
        private double TPAO = 0.0; // The Primordial Aperture of the One -> 先天一窍
        private double insight = 0.0;
        private double inspiration = 0.0;
        private double mindSense = 0.0;

        public static final Codec<BasicData> CODEC = RecordCodecBuilder.create(builder -> builder
                        .group(
                                        Codec.DOUBLE.fieldOf("normalLife").forGetter(d -> d.normalLife),
                                        Codec.DOUBLE.fieldOf("soul_life").forGetter(d -> d.soulLife),
                                        Codec.DOUBLE.fieldOf("move_speed_mof").forGetter(d -> d.moveSpeedMof),
                                        Codec.DOUBLE.fieldOf("fly_speed_mof").forGetter(d -> d.flySpeedMof),
                                        Codec.DOUBLE.fieldOf("TPAO").forGetter(d -> d.TPAO),
                                        Codec.DOUBLE.fieldOf("lucky").forGetter(d -> d.lucky),
                                        Codec.DOUBLE.fieldOf("insight").forGetter(d -> d.insight),
                                        Codec.DOUBLE.fieldOf("inspiration").forGetter(d -> d.inspiration),
                                        Codec.DOUBLE.fieldOf("charm").forGetter(d -> d.charm),
                                        Codec.DOUBLE.fieldOf("mind_sense").forGetter(d -> d.mindSense))
                        .apply(builder, BasicData::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, BasicData> STREAM_CODEC = StreamCodec
                        .ofMember(BasicData::encode, BasicData::new);

        protected BasicData() {

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
                        double mindSense) {
                this.normalLife = normalLife;
                this.soulLife = soulLife;
                this.moveSpeedMof = moveSpeedMof;
                this.flySpeedMof = flySpeedMof;
                this.TPAO = TPAO;
                this.lucky = lucky;
                this.charm = charm;
                this.insight = insight;
                this.inspiration = inspiration;
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
                        this.mindSense = Config.getMaxValue(Config.RANGE_M_SENSE);
                } else if (mindSense <= Config.getMinValue(Config.RANGE_M_SENSE)) {
                        this.mindSense = Config.getMinValue(Config.RANGE_M_SENSE);
                }
                this.mindSense = mindSense;
        }

        public double getMindSense() {
                return mindSense;
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
        }

        public static BasicData initData() {
                BasicData data = new BasicData();
                double randTPAO = XuanLing.RANDOM.nextDouble(Config.getMinValue(Config.RANGE_TPAO), 100);
                data.setTPAO(randTPAO);
                double randInsight = XuanLing.RANDOM.nextDouble(Config.getMinValue(Config.RANGE_INSIGHT), 100);
                data.setInsight(randInsight);
                double randLucky = XuanLing.RANDOM.nextDouble(Config.getMinValue(Config.RANGE_LUCKY), 100);
                data.setLucky(randLucky);
                double randCharm = XuanLing.RANDOM.nextDouble(Config.getMinValue(Config.RANGE_CHARM), 100);
                data.setCharm(randCharm);
                double randMSense = XuanLing.RANDOM.nextDouble(Config.getMinValue(Config.RANGE_M_SENSE), 100);
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
                                && this.mindSense == b.mindSense;
        }

        public static BasicData copy(BasicData data) {
                return new BasicData(data.normalLife, data.soulLife, data.moveSpeedMof, data.flySpeedMof, data.TPAO,
                                data.lucky, data.charm, data.insight, data.inspiration, data.mindSense);
        }

}
