package me.ronin.mmdetector;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.stream.Collectors;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ItemESP {
    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity camera = mc.getRenderViewEntity();

        if (camera == null) {
            return;
        }

        double x = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * event.getPartialTicks();
        double y = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * event.getPartialTicks();
        double z = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * event.getPartialTicks();

        GlStateManager.pushMatrix();
        GlStateManager.translate(-x, -y, -z);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        for (EntityPlayer player : mc.world.playerEntities) {
            if (player == camera) {
                continue;
            }

            double x1 = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
            double y1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
            double z1 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
            double x2 = x1 + player.width;
            double y2 = y1 + player.height;
            double z2 = z1 + player.width;

            GlStateManager.disableDepth();
            RenderGlobal.drawSelectionBoundingBox(player.getEntityBoundingBox(), 0.0F, 1.0F, 0.0F, 0.2F);
            GlStateManager.enableDepth();
        }

        // Render bounding boxes for item entities
        for (EntityItem item : mc.world.getLoadedEntityList().stream()
                .filter(e -> e instanceof EntityItem)
                .map(e -> (EntityItem) e)
                .collect(Collectors.toList())) {
            double x1 = item.lastTickPosX + (item.posX - item.lastTickPosX) * event.getPartialTicks();
            double y1 = item.lastTickPosY + (item.posY - item.lastTickPosY) * event.getPartialTicks();
            double z1 = item.lastTickPosZ + (item.posZ - item.lastTickPosZ) * event.getPartialTicks();
            double x2 = x1 + item.width;
            double y2 = y1 + item.height;
            double z2 = z1 + item.width;

            GlStateManager.disableDepth();
            RenderGlobal.drawSelectionBoundingBox(item.getEntityBoundingBox(), 1.0F, 1.0F, 0.0F, 0.8F);
            GlStateManager.enableDepth();
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}




//can you make a java class for forge 1.12.2 that makes a 2d player esp that draws the box around players and the colour is red ?
