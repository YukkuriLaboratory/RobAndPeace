package net.yukulab.robandpeace.extension;

public interface CustomClimbingStateHolder {
    default boolean robandpeace$isClimbing() {
        throw new AssertionError();
    }

    default void robandpeace$setClimbing(boolean wallClimbing) {
        throw new AssertionError();
    }
}
