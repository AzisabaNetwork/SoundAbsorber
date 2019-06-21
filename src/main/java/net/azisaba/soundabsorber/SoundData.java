package net.azisaba.soundabsorber;

import org.bukkit.Sound;

public class SoundData {

	private final Sound sound;

	private final float pitch;
	private final double adjust;

	public SoundData(Sound sound) {
		this.sound = sound;
		pitch = -1;
		adjust = 1;
	}

	public SoundData(Sound sound, float pitch, double adjust) {
		this.sound = sound;
		this.pitch = pitch;
		this.adjust = adjust;
	}

	public Sound getSound() {
		return sound;
	}

	public float getPitch() {
		return pitch;
	}

	public double getAdjust() {
		return adjust;
	}
}