package com.firesoftitan.play.titanbox.portals.managers;

import java.util.HashMap;
import java.util.UUID;

public class PlayerPacketInfo {
    private UUID uuid;
    private HashMap<String, Long> packetsCount;
    private Long totalCount;

    public PlayerPacketInfo(UUID uuid) {
        this.uuid = uuid;
        packetsCount = new HashMap<String, Long>();
        totalCount = 0L;
    }

    public void addPacket(String packet)
    {
        String key = packet.split("@")[0];
        Long count = 0L;
        if (packetsCount.containsKey(key)) count = packetsCount.get(key);
        count++;
        packetsCount.put(key, count);
        totalCount++;
    }
    public void reset()
    {
        totalCount = 0L;
        packetsCount.clear();

    }

    public HashMap<String, Long> getPacketsCount() {
        return packetsCount;
    }

    public Long totalCount() {
        return totalCount;
    }

    public UUID getUuid() {
        return uuid;
    }
}
