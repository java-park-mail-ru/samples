package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

class NIOExample {
    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));

        final Selector selector = Selector.open();

        serverSocketChannel.register(selector, serverSocketChannel.validOps());

        while (true) {
            selector.select();

            final Set<SelectionKey> selectedKeys = selector.selectedKeys();

            final Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {

                final SelectionKey key = keyIterator.next();

                if (key.isAcceptable()) {
                    final ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    final SocketChannel socketChannel = serverChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);

                } else if (key.isReadable()) {
                    read(key);
                }

                keyIterator.remove();
            }
        }
    }

    private static void read(SelectionKey key) throws IOException {
        final SocketChannel socketChannel = (SocketChannel) key.channel();
        final ByteBuffer buf = ByteBuffer.allocate(8196);
        socketChannel.read(buf);
        final String output = new String(buf.array()).trim();
        if (output.isEmpty()) {
            socketChannel.close();
            key.cancel();
            System.out.println("Client disconnected");
            return;
        }
        System.out.println("Message read from client: " + output);
    }
}