package com.mingsiamz.xuanlingmod.animations.player;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.register.RegisterEffects;
import com.zigythebird.playeranim.animation.PlayerAnimResources;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.AnimationData;
import com.zigythebird.playeranimcore.animation.RawAnimation;
import com.zigythebird.playeranimcore.enums.PlayState;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

public final class AnmMeditation {

        public static final ResourceLocation LAYER_ID = XuanLing.prefix("meditation");
        public static final ResourceLocation INTRO_ANIMATION = XuanLing.prefix("meditation");

        private AnmMeditation() {
        }

        public static PlayerAnimationController create(AbstractClientPlayer player) {
                return new PlayerAnimationController(player, new MeditationStateHandler(player));
        }

        private static final class MeditationStateHandler implements AnimationController.AnimationStateHandler {

                private final AbstractClientPlayer player;
                private Animation intro;
                private RawAnimation raw;

                private boolean meditating;

                private MeditationStateHandler(AbstractClientPlayer player) {
                        this.player = player;
                }

                @Override
                public PlayState handle(AnimationController controller, AnimationData state,
                                AnimationController.AnimationSetter animationSetter) {
                        if (!player.hasEffect(RegisterEffects.EFFECT_MEDITATION)) {
                                meditating = false;
                                return PlayState.STOP;
                        }

                        Animation currentIntro = PlayerAnimResources.getAnimation(INTRO_ANIMATION);

                        if (currentIntro == null) {
                                meditating = false;
                                return PlayState.STOP;
                        }

                        if (currentIntro != intro) {
                                intro = currentIntro;
                                raw = RawAnimation.begin().thenPlay(intro);
                        }

                        if (!meditating) {
                                meditating = true;
                                controller.triggerAnimation(currentIntro);
                        }

                        animationSetter.setAnimation(raw);
                        return PlayState.CONTINUE;
                }
        }
}
