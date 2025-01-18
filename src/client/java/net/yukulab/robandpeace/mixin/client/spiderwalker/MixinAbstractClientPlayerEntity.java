package net.yukulab.robandpeace.mixin.client.spiderwalker;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.yukulab.robandpeace.extension.MovementPayloadHolder;
import net.yukulab.robandpeace.network.payload.PlayerMovementPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinAbstractClientPlayerEntity implements MovementPayloadHolder {
    @Unique
    private PlayerMovementPayload sentPayload = null;
    @Override
    public PlayerMovementPayload robandpeace$getPlayerMovementPayload() {
        return sentPayload;
    }

    @Override
    public void robandpeace$setPlayerMovementPayload(PlayerMovementPayload payload) {
        sentPayload = payload;
    }
}
