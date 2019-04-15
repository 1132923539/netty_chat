package cn.luxinhuo.netty_chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

public class ChatHander extends SimpleChannelInboundHandler</*接收到前端的消息会封装到文本Frame中*/TextWebSocketFrame> {

    //用来保存所有客户端的连接
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    //当Channel中有新的事件消息会自动调用
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        System.out.println("客户端：" + text);

        //将消息发送到所有客户端
        for (Channel client : clients) {
            client.writeAndFlush(new TextWebSocketFrame(LocalDateTime.now().toString() + ": " + text));
        }
    }

    @Override
    // 当有新的通道（即客户端建立连接）加入时会调用此方法
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接断开，对应的长 ID 为："+ctx.channel().id().asLongText());
        System.out.println("客户端连接断开，对应的短 ID 为："+ctx.channel().id().asShortText());
    }
}
