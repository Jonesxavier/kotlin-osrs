package io.battlerune.net.codec.update

import io.battlerune.net.NetworkService
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class UpdateEncoder : MessageToByteEncoder<FileRequest>() {

    override fun encode(ctx: ChannelHandlerContext, msg: FileRequest, out: ByteBuf) {
        val index = msg.index
        val file = msg.file

        val response = ctx.alloc().buffer()
        response.writeByte(index)
                .writeShort(file)

        if (index == 255 && file == 255) {
            val checksums = NetworkService.checksumTable.duplicate()

            response.writeByte(0)
                    .writeInt(checksums.limit())
                    .writeBytes(checksums)

        } else if (index == 255) {

        } else {

        }

        ctx.writeAndFlush(response)
    }

}