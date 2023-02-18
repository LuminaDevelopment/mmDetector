package me.ronin.mmdetector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.SERVER)
public class MurderDetector {

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        detectedPlayers.clear();
        playerStatus.clear();
    }

    private static final long COOLDOWN_DURATION = TimeUnit.SECONDS.toMillis(3);  // 3 seconds in milliseconds
    private static final Map<UUID, Long> playerCooldowns = new HashMap<>();
    static final Set<UUID> detectedPlayers = new HashSet<>();
    static final Map<UUID, String> playerStatus = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(PlayerEvent.LivingUpdateEvent event) {
        UUID playerId = event.getEntityLiving().getUniqueID();
        Long lastMessageTime = playerCooldowns.get(playerId);
        long currentTime = System.currentTimeMillis();

        // Check that the entity is a player
        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }


        if (lastMessageTime != null && currentTime - lastMessageTime < COOLDOWN_DURATION) {
            // The player has already received a message within the past 3 seconds, so do nothing.
            return;
        }


        ItemStack item = event.getEntityLiving().getHeldItemMainhand();
        ItemStack ironSword = new ItemStack(Items.IRON_SWORD);
        ItemStack stoneSword = new ItemStack(Items.STONE_SWORD);
        ItemStack ironShovel = new ItemStack(Items.IRON_SHOVEL);
        ItemStack stick = new ItemStack(Items.STICK);
        ItemStack woodAxe = new ItemStack(Items.WOODEN_AXE);
        ItemStack woodSword = new ItemStack(Items.WOODEN_SWORD);
        ItemStack deadBush = new ItemStack(Blocks.DEADBUSH);
        ItemStack stoneShovel = new ItemStack(Items.STONE_SHOVEL);
        ItemStack blazeRod = new ItemStack(Items.BLAZE_ROD);
        ItemStack diamondShovel = new ItemStack(Items.DIAMOND_SHOVEL);
        ItemStack quartz = new ItemStack(Items.QUARTZ);
        ItemStack pumpkinPie = new ItemStack(Items.PUMPKIN_PIE);
        ItemStack goldPickaxe = new ItemStack(Items.GOLDEN_PICKAXE);
        ItemStack apple = new ItemStack(Items.APPLE);
        ItemStack nameTag = new ItemStack(Items.NAME_TAG);
        ItemStack sponge = new ItemStack(Blocks.SPONGE);
        ItemStack carrotOnAStick = new ItemStack(Items.CARROT_ON_A_STICK);
        ItemStack bone = new ItemStack(Items.BONE);
        ItemStack carrot = new ItemStack(Items.CARROT);
        ItemStack goldenCarrot = new ItemStack(Items.GOLDEN_CARROT);
        ItemStack cookie = new ItemStack(Items.COOKIE);
        ItemStack diamondAxe = new ItemStack(Items.DIAMOND_AXE);
        ItemStack doublePlant = new ItemStack(Blocks.DOUBLE_PLANT);
        ItemStack prismarineShard = new ItemStack(Items.PRISMARINE_SHARD);
        ItemStack cookedBeef = new ItemStack(Items.COOKED_BEEF);
        ItemStack netherBrick = new ItemStack(Items.NETHERBRICK);
        ItemStack cookedChicken = new ItemStack(Items.COOKED_CHICKEN);
        ItemStack recordBlocks = new ItemStack(Items.RECORD_BLOCKS);
        ItemStack goldSword = new ItemStack(Items.GOLDEN_SWORD);
        ItemStack diamondSword = new ItemStack(Items.DIAMOND_SWORD);
        ItemStack diamondHoe = new ItemStack(Items.DIAMOND_HOE);
        ItemStack shears = new ItemStack(Items.SHEARS);
        ItemStack fish = new ItemStack(Items.FISH);
        ItemStack dye = new ItemStack(Items.DYE);
        ItemStack boat = new ItemStack(Items.BOAT);
        ItemStack speckledMelon = new ItemStack(Items.SPECKLED_MELON);
        ItemStack book = new ItemStack(Items.BOOK);
        // Other existing code goes here...


        if (item.isItemEqual(diamondSword) || item.isItemEqual(ironSword) || item.isItemEqual(stoneSword) ||
                item.isItemEqual(diamondAxe) || item.isItemEqual(diamondShovel) || item.isItemEqual(ironShovel) || item.isItemEqual(stoneShovel) ||
                item.isItemEqual(diamondHoe) || item.isItemEqual(woodSword) || item.isItemEqual(stick) || item.isItemEqual(woodAxe) ||
                item.isItemEqual(deadBush) || item.isItemEqual(blazeRod) || item.isItemEqual(quartz) || item.isItemEqual(pumpkinPie) ||
                item.isItemEqual(goldPickaxe) || item.isItemEqual(apple) || item.isItemEqual(nameTag) || item.isItemEqual(sponge) ||
                item.isItemEqual(carrotOnAStick) || item.isItemEqual(bone) || item.isItemEqual(carrot) || item.isItemEqual(goldenCarrot) || item.isItemEqual(cookie) || item.isItemEqual(doublePlant) || item.isItemEqual(prismarineShard) || item.isItemEqual(cookedBeef) ||
                item.isItemEqual(netherBrick) || item.isItemEqual(cookedChicken) || item.isItemEqual(recordBlocks) || item.isItemEqual(goldSword) ||
                item.isItemEqual(shears) || item.isItemEqual(fish) || item.isItemEqual(dye) || item.isItemEqual(boat) || item.isItemEqual(speckledMelon) ||
                item.isItemEqual(book)) {
            detectedPlayers.add(playerId);
            playerStatus.put(playerId, event.getEntityLiving().getName() + " is a murderer!");
            String message = event.getEntityLiving().getName() + " is a murderer!";
            TextComponentString textComponent = new TextComponentString(message);
            textComponent.getStyle().setColor(TextFormatting.DARK_RED);


            // Send the message to the player and update the player's cooldown.
            event.getEntityLiving().sendMessage(textComponent);
            playerCooldowns.put(playerId, currentTime);
        }
        ItemStack bow = new ItemStack(Items.BOW);

        if (item.isItemEqual(bow)) {
            detectedPlayers.add(playerId);
            playerStatus.put(playerId, event.getEntityLiving().getName() + " has a bow!");
            String message = event.getEntityLiving().getName() + " has a bow!";
            TextComponentString textComponent = new TextComponentString(message);
            textComponent.getStyle().setColor(TextFormatting.AQUA);
            event.getEntityLiving().sendMessage(textComponent);

            event.getEntityLiving().sendMessage(textComponent);
            playerCooldowns.put(playerId, currentTime);
        }

    }

    @SubscribeEvent
    public void renderOverlay(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc);

        int x = res.getScaledWidth() - 170;
        int y = 10;

        for (Map.Entry<UUID, String> entry : playerStatus.entrySet()) {
            String message = entry.getValue();

            // Set the color of the text based on whether the player is a murderer or has a bow
            TextFormatting color;
            if (message.endsWith("is a murderer!")) {
                color = TextFormatting.DARK_RED;
            } else if (message.endsWith("has a bow!")) {
                color = TextFormatting.AQUA;
            } else {
                // Set the color of the text to the default color.
                color = TextFormatting.WHITE;
            }

            // Draw the textbox background
            Gui.drawRect(x - 5, y - 5, x + 145, y + mc.fontRenderer.FONT_HEIGHT + 5, 0x80000000);

            // Draw the player's name in bold text
            mc.fontRenderer.drawStringWithShadow(color + "" + TextFormatting.BOLD + message, x, y, 0xffffff);

            // Increment the y position for the next player
            y += mc.fontRenderer.FONT_HEIGHT + 10;
        }
    }
}
