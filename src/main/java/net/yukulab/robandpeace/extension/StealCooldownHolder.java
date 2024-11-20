package net.yukulab.robandpeace.extension;

public interface StealCooldownHolder {
    default long robandpeace$getStealCooldown() {
        throw new AssertionError();
    }
}
