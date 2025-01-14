package net.yukulab.robandpeace.mixin.spiderwalker;

import net.minecraft.server.network.ServerPlayerEntity;
import net.yukulab.robandpeace.extension.MovementPayloadHolder;
import net.yukulab.robandpeace.network.payload.PlayerMovementPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity implements MovementPayloadHolder {
    @Unique
    PlayerMovementPayload robandpeace$playerMovementPayload = new PlayerMovementPayload(UUID.randomUUID(), false, 0.0f, false, false);

    @Override
    public PlayerMovementPayload robandpeace$getPlayerMovementPayload() {
        return robandpeace$playerMovementPayload;
    }

    @Override
    public void robandpeace$setPlayerMovementPayload(PlayerMovementPayload payload) {
        robandpeace$playerMovementPayload = payload;
    }
}
