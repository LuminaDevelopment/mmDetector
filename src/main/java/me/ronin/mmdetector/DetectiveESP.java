    package me.ronin.mmdetector;

    import java.util.HashSet;
    import java.util.Set;

    import net.minecraft.client.Minecraft;
    import net.minecraft.client.renderer.BufferBuilder;
    import net.minecraft.client.renderer.GlStateManager;
    import net.minecraft.client.renderer.RenderGlobal;
    import net.minecraft.client.renderer.Tessellator;
    import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
    import net.minecraft.entity.Entity;
    import net.minecraft.entity.player.EntityPlayer;
    import net.minecraft.init.Blocks;
    import net.minecraft.init.Items;
    import net.minecraft.item.Item;
    import net.minecraft.item.ItemStack;
    import net.minecraft.util.ResourceLocation;
    import net.minecraft.util.math.AxisAlignedBB;
    import net.minecraftforge.client.event.RenderLivingEvent;
    import net.minecraftforge.client.event.RenderWorldLastEvent;
    import net.minecraftforge.event.world.WorldEvent;
    import net.minecraftforge.fml.common.Mod;
    import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
    import net.minecraftforge.fml.relauncher.Side;
    import org.lwjgl.opengl.GL11;

    import java.util.Arrays;
    import java.util.List;

    @Mod.EventBusSubscriber(Side.CLIENT)
    public class DetectiveESP {
        // Set of players who have held an item on the list of items to highlight
        @SubscribeEvent
        public static void onWorldLoad(WorldEvent.Load event) {
            PLAYERS_WITH_HIGHLIGHTED_ITEM.clear();
        }
        private static final Set<EntityPlayer> PLAYERS_WITH_HIGHLIGHTED_ITEM = new HashSet<>();

        // List of items to highlight players holding
        private static final List<Item> ITEMS_TO_HIGHLIGHT = Arrays.asList(
                Items.BOW
        );

        @SubscribeEvent
        public void onRenderWorldLast(RenderWorldLastEvent event) {
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

            GlStateManager.disableDepth();  // <-- Add this line to disable depth testing

            for (EntityPlayer player : mc.world.playerEntities) {
                if (player == mc.player) {
                    continue;
                }

                // Check if the player has held an item on the list of items to highlight
                if (PLAYERS_WITH_HIGHLIGHTED_ITEM.contains(player)) {
                    // Draw a box around the player's body
                    drawOutlinedBoundingBox(player.getEntityBoundingBox(), 0.0F, 0.0F, 1.0F, 0.2F);

                    // Draw a line connecting the box to the player's head
                    drawLineToEntity(player);
                } else {
                    // Check if the player is currently holding an item on the list of items to highlight
                    ItemStack heldItem = player.getHeldItemMainhand();
                    if (!heldItem.isEmpty() && ITEMS_TO_HIGHLIGHT.contains(heldItem.getItem())) {
                        // Add the player to the set of players who have held an item on the list
                        PLAYERS_WITH_HIGHLIGHTED_ITEM.add(player);
                    }
                }
            }

            GlStateManager.enableDepth();  // <-- Add this line to re-enable depth testing

            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

        @SubscribeEvent
        public void onRenderLiving(RenderLivingEvent.Post event) {
            Entity entity = event.getEntity();
            if (!(entity instanceof EntityPlayer)) {
                return;
            }

            EntityPlayer player = (EntityPlayer) entity;

            // Check if the player has held an item on the list of items to highlight
            if (PLAYERS_WITH_HIGHLIGHTED_ITEM.contains(player)) {
                // Draw a box around the player's head
                drawOutlinedBoundingBox(player.getEntityBoundingBox().grow(0.0, 0.0, 0.0).offset(0.0, -player.getEyeHeight(), 0.0), 0.0F, 0.0F, 1.0F, 0.2F);
            }
        }

        private void drawOutlinedBoundingBox(AxisAlignedBB bb, float red, float green, float blue, float alpha) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
            buffer.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
            buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            buffer.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            buffer.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
            tessellator.draw();

            buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
            buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
            buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            buffer.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
            tessellator.draw();

            buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
            buffer.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
            tessellator.draw();
            buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
            buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
            tessellator.draw();
            buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            tessellator.draw();
            buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            tessellator.draw();
        }
        private void drawLineToEntity(Entity entity) {
            Minecraft mc = Minecraft.getMinecraft();
            Entity camera = mc.getRenderViewEntity();

            if (camera == null) {
                return;
            }

            double x = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * mc.getRenderPartialTicks();
            double y = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * mc.getRenderPartialTicks();
            double z = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * mc.getRenderPartialTicks();

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(x, y, z).color(0.0F, 0.0F, 1.0F, 0.2F).endVertex();
            buffer.pos(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ).color(0.0F, 0.0F, 1.0F, 0.2F).endVertex();
            tessellator.draw();
        }
    }
