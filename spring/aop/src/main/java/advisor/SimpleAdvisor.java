package advisor;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * SimpleAdvisor
 *
 * @author zjy
 * @date 2023/2/27
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class SimpleAdvisor extends AbstractPointcutAdvisor {

    private final Advice advice;

    private final Pointcut pointcut;

    public SimpleAdvisor() {
        this.advice = new SimpleAdvice();
        this.pointcut = buildPointcut();
    }

    protected Pointcut buildPointcut() {
        Pointcut classPointcut = new AnnotationMatchingPointcut(null, true);
        Pointcut methodPointcut = new AnnotationMatchingPointcut(null, null, true);
        return new ComposablePointcut(classPointcut).union(methodPointcut);
    }
}
