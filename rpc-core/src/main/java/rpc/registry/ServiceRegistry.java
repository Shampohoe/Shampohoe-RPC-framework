package rpc.registry;

/**
 * ClassName:ServiceRegistry
 * Package:rpc.registry
 * Description:
 *
 * @Author kkli
 * @Create 2023/9/12 23:34
 * #Version 1.1
 */
public interface ServiceRegistry {
    /**
     * @description 将一个服务注册进注册表
     * @param [service] 待注册的服务实体
     * @param <T> 服务实体类
     */
    <T> void register(T service);

    /**
     * @description 根据服务名获取服务实体
     * @param [serviceName] 服务名称
     */
    Object getService(String serviceName);
}