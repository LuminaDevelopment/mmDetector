package me.ronin.mmdetector;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(
        modid = Mmdetector.MOD_ID,
        name = Mmdetector.MOD_NAME,
        version = Mmdetector.VERSION
)
public class Mmdetector {


    public static final String MOD_ID = "mmdetector";
    public static final String MOD_NAME = "MmDetector";
    public static final String VERSION = "1.0-SNAPSHOT";


    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModEventHandler.init();
    }

    @Mod.EventHandler
    public void initServer(FMLInitializationEvent event) {
        ModEventHandler.initServer();
    }
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PlayerESP());
        MinecraftForge.EVENT_BUS.register(new ItemESP());
        MinecraftForge.EVENT_BUS.register(new DetectiveESP());
    }
}

