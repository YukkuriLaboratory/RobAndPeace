package net.yukulab.robandpeace.mixin.steal;

import net.minecraft.entity.damage.DamageSource;
import net.yukulab.robandpeace.extension.CriticalHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DamageSource.class)
public abstract class MixinDamageSource implements CriticalHolder {
    @Unique
    private boolean robandpeace$critical = false;

    @Override
    public void robandpeace$markCritical() {
        this.robandpeace$critical = true;
    }

    @Override
    public boolean robandpeace$isCritical() {
        return robandpeace$critical;
    }
}
