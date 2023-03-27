package advisor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * SimpleAdvisor
 *
 * @author zjy
 * @date 2023/2/27
 */

public class SimpleAdvice implements MethodInterceptor {


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return null;
    }
}
