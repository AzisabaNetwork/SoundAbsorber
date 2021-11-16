package net.azisaba.soundabsorber;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;

import net.md_5.bungee.api.ChatColor;

public class SoundAbsorbListener {

	private static SoundAbsorber plugin = null;

	
	private static final HashMap<Sound, SoundData> adjustSounds = new HashMap<>();

	static {
		adjustSounds.add(Sound.ENTITY_BAT_TAKEOFF, new SoundData(Sound.ENTITY_BAT_TAKEOFF, 2.0f, 5d));
		//		cancelSounds.put(Sound.BLOCK_NOTE_HARP, 1d);
	}

	public static void register(SoundAbsorber plugin) {
		SoundAbsorbListener.plugin = plugin;

		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
					@Override
					public void onPacketSending(PacketEvent event) {
						if (event.getPacketType() != PacketType.Play.Server.NAMED_SOUND_EFFECT) {
							return;
						}

						Player p = event.getPlayer();
						PacketContainer packet = event.getPacket().deepClone();
						StructureModifier<Sound> data = packet.getSoundEffects();

						double volume = packet.getFloat().getValues().get(0);
						double pitch = packet.getFloat().getValues().get(0);
						double percent = SoundPercentageContainer.getAbsorbLevel(p);


						if (percent >= 100 && p.getName().equals("siloneco")) {

							List<String> sounds = data.getValues().stream()
									.map(Sound::toString)
									.collect(Collectors.toList());

							String msg = ChatColor.YELLOW + "Sounds: " + ChatColor.RED + "["
									+ String.join(", ", sounds) + "] "
									+ ChatColor.YELLOW + "Volume: " + ChatColor.RED + String.format("%.3f", volume);

							JSONMessage.create(msg).actionbar(p);
							p.sendMessage(msg);
						}

						double adjust = 1d;
					
						for (Sound s : data.getValues()) {
								if (adjustSounds.containsKey(s) && (soundData.getPitch() < 0 || soundData.getPitch() == pitch)) {
									adjust = adjustSounds.get(s).getAdjust();
								}
						}
						

						volume *= adjust;
						double times = percent * adjust;
						float changeVolume = (float) (volume * times / 100);

						try {
							boolean before = packet.getFloat().getFields().get(0).isAccessible();
							packet.getFloat().getFields().get(0).setAccessible(true);
							packet.getFloat().getFields().get(0).set(packet.getFloat().getTarget(), changeVolume);
							packet.getFloat().getFields().get(0).setAccessible(before);
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}

						event.setPacket(packet);
					}
				});
	}

	public static void unregister() {
		if (plugin != null) {
			ProtocolLibrary.getProtocolManager().removePacketListeners(plugin);
		}
	}
}
