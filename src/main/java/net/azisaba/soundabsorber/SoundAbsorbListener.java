package net.azisaba.soundabsorber;

import java.util.ArrayList;
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
	private static final List<SoundData> absorbSounds = new ArrayList<>();

	static {
		absorbSounds.add(new SoundData(Sound.BLOCK_NOTE_BASS));
		absorbSounds.add(new SoundData(Sound.BLOCK_NOTE_PLING));
		absorbSounds.add(new SoundData(Sound.ENTITY_BLAZE_HURT));
		absorbSounds.add(new SoundData(Sound.BLOCK_NOTE_BASEDRUM));
		absorbSounds.add(new SoundData(Sound.ENTITY_GENERIC_EXPLODE));
		absorbSounds.add(new SoundData(Sound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD));

		absorbSounds.add(new SoundData(Sound.ENTITY_BAT_TAKEOFF, 2.0f, 5d));
		absorbSounds.add(new SoundData(Sound.ENTITY_BAT_TAKEOFF));
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

						if (percent >= 100) {
							if (p.getName().equals("siloneco")) {

								List<String> sounds = data.getValues().stream()
										.map(Sound::toString)
										.collect(Collectors.toList());

								String msg = ChatColor.YELLOW + "Sounds: " + ChatColor.RED + "["
										+ String.join(", ", sounds) + "] "
										+ ChatColor.YELLOW + "Volume: " + ChatColor.RED + String.format("%.3f", volume);

								JSONMessage.create(msg).actionbar(p);
								p.sendMessage(msg);
							}
							return;
						}

						boolean include = false;
						double adjust = 1d;

						for (Sound s : data.getValues()) {
							for (SoundData soundData : absorbSounds) {
								if (soundData.getSound().equals(s)
										&& (soundData.getPitch() < 0 || soundData.getPitch() == pitch)) {
									include = true;
									adjust = soundData.getAdjust();
									break;
								}
							}
						}

						if (!include) {
							return;
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
