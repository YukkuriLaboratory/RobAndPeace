package net.yukulab.robandpeace.mixin;

import net.minecraft.entity.mob.MobEntity;
import net.yukulab.robandpeace.extension.StanHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
abstract public class MixinMobEntity implements StanHolder {
    @Shadow
    public abstract void setAiDisabled(boolean aiDisabled);

    @Unique
    private int robandpeace$stanTick = 0;

    @Override
    public void robandpeace$SetStan(int tick) {
        robandpeace$stanTick = tick;
        setAiDisabled(true);
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void countStanTick(CallbackInfo ci) {
        if (robandpeace$stanTick > 0) {
            if (--robandpeace$stanTick == 0) {
                setAiDisabled(false);
            }
        }
    }
}
