import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.zepe.rpc.example.common.model.User;
import org.zepe.rpc.example.common.service.UserService;
import org.zepe.rpc.proxy.ServiceProxyFactory;

/**
 * @author zzpus
 * @datetime 2025/4/24 13:41
 * @description
 */
@Slf4j
public class MockServiceProxyTest {
    @Test
    public void testProxy() {
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = userService.getUserByName("test");

        log.info("User: {}", user);
    }

    @Test
    public void testMock() {
        UserService userService = ServiceProxyFactory.getMockProxy(UserService.class);

        User user = userService.getUserByName("test");

        log.info("User: {}", user);

        Assert.assertNull(user);
    }

}