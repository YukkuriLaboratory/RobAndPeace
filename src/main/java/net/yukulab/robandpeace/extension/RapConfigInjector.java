package net.yukulab.robandpeace.extension;

import net.yukulab.robandpeace.config.RapServerConfig;

import java.util.function.Supplier;

public interface RapConfigInjector {
    default Supplier<RapServerConfig> robandpeace$getServerConfigSupplier() {
        throw new AssertionError();
    }

    default void robandpeace$setServerConfigSupplier(Supplier<RapServerConfig> supplier) {
        throw new AssertionError();
    }
}
