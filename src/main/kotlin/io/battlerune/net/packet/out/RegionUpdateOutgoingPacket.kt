package io.battlerune.net.packet.out

import io.battlerune.game.world.actor.Player
import io.battlerune.net.codec.game.ByteModification
import io.battlerune.net.codec.game.ByteOrder
import io.battlerune.net.codec.game.RSByteBufWriter
import io.battlerune.net.packet.Packet
import io.battlerune.net.packet.PacketType
import io.battlerune.net.packet.PacketEncoder
import java.util.*

class RegionUpdateOutgoingPacket(val buf: RSByteBufWriter) : PacketEncoder {

    override fun encode(player: Player): Optional<Packet> {
        val builder = RSByteBufWriter.wrap(buf.buffer)
            val chunkX = player.position.chunkX
            val chunkY = player.position.chunkY

            var forceSend = false

            if (((chunkX / 8) == 48 || (chunkX / 8) == 49) && (chunkY / 8) == 48) {
                forceSend = true
            }

            if (chunkX / 8 == 48 && chunkY / 8 == 149) {
                forceSend = true
            }

            var count = 0

            val xtea = RSByteBufWriter.alloc()
            for (xCalc in (chunkX - 6) / 8..(6 + chunkX) / 8) {
                for (yCalc in (chunkY - 6) / 8..(6 + chunkY) / 8) {
                    val region = yCalc + (xCalc shl 8)
                    if (!forceSend || yCalc != 49 && 149 != yCalc && 147 != yCalc && xCalc != 50 && (xCalc != 49 || yCalc != 47)) {
                        val keys = player.context.regionManager.keys[region]

                        keys.forEach { xtea.writeInt(it) }

                    }
                    count++
                }
            }

            builder.writeShort(chunkY, ByteOrder.LE)
            builder.writeShort(chunkX, ByteModification.ADD)
            builder.writeShort(count)

        return Optional.of(builder.toPacket(60, PacketType.VAR_SHORT))
    }

}