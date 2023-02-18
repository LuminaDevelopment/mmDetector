package me.ronin.mmdetector;

import net.minecraftforge.common.MinecraftForge;

public class ModEventHandler {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new MurderDetector());
        MinecraftForge.EVENT_BUS.register(new PlayerESP());
        MinecraftForge.EVENT_BUS.register(new ItemESP());
        MinecraftForge.EVENT_BUS.register(new DetectiveESP());
    }

    public static void initServer() {
        MinecraftForge.EVENT_BUS.register(new MurderDetector());
        MinecraftForge.EVENT_BUS.register(new PlayerESP());
        MinecraftForge.EVENT_BUS.register(new ItemESP());
        MinecraftForge.EVENT_BUS.register(new DetectiveESP());
    }

}
