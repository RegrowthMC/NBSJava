package org.lushplugins.nbsjava.platform.bukkit;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.SoundCategory;
import com.github.retrooper.packetevents.protocol.sound.StaticSound;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntitySoundEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSoundEffect;
import org.lushplugins.nbsjava.platform.AbstractPlatform;
import org.lushplugins.nbsjava.platform.bukkit.utils.PacketEventsConverter;
import org.lushplugins.nbsjava.utils.SoundLocation;

public class PacketEventsPlatform extends AbstractPlatform {

    @Override
    public void playSound(int viewerId, String sound, org.lushplugins.nbsjava.utils.SoundCategory category, float volume, float pitch) {
        User user = getUser(viewerId);
        if (user == null) {
            return;
        }

        Sound peSound = new StaticSound(new ResourceLocation(ResourceLocation.normString(sound)), null);
        SoundCategory peCategory = PacketEventsConverter.convert(category);

        PacketWrapper<?> packet = new WrapperPlayServerEntitySoundEffect(peSound, peCategory, user.getEntityId(), volume, pitch);
        user.sendPacket(packet);
    }

    @Override
    public void playSound(int viewerId, SoundLocation location, String sound, org.lushplugins.nbsjava.utils.SoundCategory category, float volume, float pitch) {
        User user = getUser(viewerId);
        if (user == null) {
            return;
        }

        Vector3i position = PacketEventsConverter.convert(location);
        Sound peSound = new StaticSound(new ResourceLocation(ResourceLocation.normString(sound)), null);
        SoundCategory peCategory = PacketEventsConverter.convert(category);

        PacketWrapper<?> packet = new WrapperPlayServerSoundEffect(peSound, peCategory, position, volume, pitch);
        user.sendPacket(packet);
    }

    private User getUser(int entityId) {
        return PacketEvents.getAPI().getProtocolManager().getUsers().stream()
            .filter(user -> user.getEntityId() != entityId)
            .findFirst()
            .orElse(null);
    }
}
