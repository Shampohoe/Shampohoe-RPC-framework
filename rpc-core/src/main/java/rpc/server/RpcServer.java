package rpc.server;

import lombok.extern.slf4j.Slf4j;
import rpc.registry.ServiceRegistry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * ClassName:RpcServer
 * Package:rpc.server
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/12 22:08
 * #Version 1.1
 */
@Slf4j
public class RpcServer {
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private final ExecutorService threadPool;
    private RequestHandler requestHandler = new RequestHandler();
    private final ServiceRegistry serviceRegistry;

    public RpcServer(ServiceRegistry serviceRegistry){
        this.serviceRegistry = serviceRegistry;
        /**
         * 设置上限为100个线程的阻塞队列
         */
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
         // 创建线程池实例
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    /**
     * @description 服务启动
     * @param [service, port]
     * @return [void]
     */
    public void start(int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            log.info("服务器启动……");
            Socket socket;
            //当未接收到连接请求时，accept()会一直阻塞
            while ((socket = serverSocket.accept()) != null){
                log.info("客户端连接！{}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serviceRegistry));
            }
            threadPool.shutdown();
        }catch (IOException e){
            log.info("服务器启动时有错误发生：" + e);
        }
    }
}
