package com.example.examplemod;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ExampleLegacyMod.MODID, name = ExampleLegacyMod.NAME, version = ExampleLegacyMod.VERSION)
public class ExampleLegacyMod {

    public static Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "examplemod";
    public static final String NAME = "Example Legacy Mod";
    public static final String VERSION = "1.0";


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

}
