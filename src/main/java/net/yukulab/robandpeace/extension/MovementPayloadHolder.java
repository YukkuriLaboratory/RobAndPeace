package net.yukulab.robandpeace.extension;

import net.yukulab.robandpeace.network.payload.PlayerMovementPayload;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface MovementPayloadHolder {
    PlayerMovementPayload DEFAULT_PAYLOAD = new PlayerMovementPayload(UUID.randomUUID(), false, 0.0f, false, false);
    @Nullable
    default PlayerMovementPayload robandpeace$getPlayerMovementPayload() {
        throw new AssertionError();
    }

    default void robandpeace$setPlayerMovementPayload(PlayerMovementPayload payload) {
        throw new AssertionError();
    }
}
