package chat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.util.List;

public class ResponseDataDecoder extends ReplayingDecoder<ResponseData> {

    private final Charset charset = Charset.forName("UTF-8");

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        ResponseData data = new ResponseData();
        data.setIntValue(in.readInt());
        int strLen = in.readInt();
        data.setStringValue(in.readCharSequence(strLen, charset).toString());
        out.add(data);
    }
}