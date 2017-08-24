package somepack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@Dependent
public class LogProducer {
    @Produces @Default
    public Logger getLogger(InjectionPoint point) {
        return LoggerFactory.getLogger(point.getMember().getDeclaringClass());
    }
}
