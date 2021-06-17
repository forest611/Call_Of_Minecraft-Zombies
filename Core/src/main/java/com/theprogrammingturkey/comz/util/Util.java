package com.theprogrammingturkey.comz.util;

import com.theprogrammingturkey.comz.COMZombies;
import com.theprogrammingturkey.comz.api.NMSParticleType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Util
{
	private static final char[] VALID_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

	public static String genRandId()
	{
		StringBuilder id = new StringBuilder();
		for(int i = 0; i < 8; i++)
			id.append(VALID_CHARS[COMZombies.rand.nextInt(VALID_CHARS.length)]);
		return id.toString();
	}

	public void playChestAction(Location location, boolean open)
	{
		World world = ((CraftWorld) location.getWorld()).getHandle();
		BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());
		TileEntityChest tileChest = (TileEntityChest) world.getTileEntity(position);
		if(tileChest != null)
			world.playBlockAction(position, tileChest.getBlock().getBlock(), 1, open ? 1 : 0);
	}

	public void playBlockBreakAction(Player player, int damage, Block block)
	{
		PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(0, new BlockPosition(block.getX(), block.getY(), block.getZ()), damage);
		for(EntityPlayer entityplayer : ((CraftServer) player.getServer()).getHandle().players)
		{
			double d4 = block.getX() - entityplayer.locX();
			double d5 = block.getY() - entityplayer.locY();
			double d6 = block.getZ() - entityplayer.locZ();
			if(d4 * d4 + d5 * d5 + d6 * d6 < 64 * 64)
				entityplayer.playerConnection.sendPacket(packet);
		}
	}

	public void playSound(Player player, String sound)
	{
		PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(new SoundEffect(new MinecraftKey(sound)), SoundCategory.AMBIENT, player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ(), 1.0F, 1.0F);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public void sendActionBarMessage(Player player, String message)
	{
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
	}

	public void sendParticleToPlayer(NMSParticleType particleType, Player player, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count)
	{
		ParticleType particle = Particles.AMBIENT_ENTITY_EFFECT;
		switch(particleType)
		{
			case WITCH:
				particle = Particles.WITCH;
				break;
			case LAVA:
				particle = Particles.LAVA;
				break;
			case FIREWORK:
				particle = Particles.FIREWORK;
				break;
			case HEART:
				particle = Particles.HEART;
				break;
		}
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, location.getX(), location.getY(), location.getZ(), offsetX, offsetY, offsetZ, speed, count);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

}
