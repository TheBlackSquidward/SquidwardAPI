package io.github.TheBlackSquidward.squidwardapi.network;

import io.github.TheBlackSquidward.squidwardapi.SquidwardAPI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private int id = 0;
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(SquidwardAPI.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public <T> void registerPacket(IMessage<T> msg) {
        INSTANCE.registerMessage(id++, (Class<T>) msg.getClass(), msg::encode, msg::decode, msg::handle);
    }

    public <T> void sendToServer(T message) {
        INSTANCE.sendToServer(message);
    }

    public <T> void sendToAll(T msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }

    public <T> void sendToPlayer(T message, PlayerEntity playerEntity) {
        if(playerEntity instanceof ServerPlayerEntity) {
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) playerEntity), message);
        }
    }

}
