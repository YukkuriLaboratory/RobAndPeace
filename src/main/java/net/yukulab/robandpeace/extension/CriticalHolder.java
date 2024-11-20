package net.yukulab.robandpeace.extension;

public interface CriticalHolder {
    default void robandpeace$markCritical() {
        throw new AssertionError();
    }

    default boolean robandpeace$isCritical() {
        throw new AssertionError();
    }
}
