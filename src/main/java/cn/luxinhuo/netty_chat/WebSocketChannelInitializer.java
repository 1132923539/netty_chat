package cn.luxinhuo.netty_chat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 初始化通道
     * 在这个方法中去加载对应的ChannelHandler
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        //获取管道，将一个个 ChannelHandler 添加到管道中
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 当channel进来之后会依次调用 pipe中的 Handler

        //添加一个http的编解码器
        pipeline.addLast(new HttpServerCodec());
        //添加一个用于支持大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //添加一个聚合器，这个聚合器主要是将HTTPMessage聚合成 FullHttpRequest/Response
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));


        //需要添加指定的路由
        pipeline.addLast(new WebSocketServerProtocolHandler("/hahaha"));

        //添加自定义的Handler，来处理业务
        pipeline.addLast(new ChatHander());
    }
}
