package me.wardsculks.restrictednether;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestrictedNether implements ModInitializer{
    public static final Logger LOGGER = LoggerFactory.getLogger("restrictednether");

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Restricted Nether mod.");
    }
}
