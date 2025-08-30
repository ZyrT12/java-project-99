package hexlet.code.config;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

class FlywayRepairConfigTest {

    private FlywayMigrationStrategy strategy;

    @BeforeEach
    void setUp() throws Exception {
        var cfg = new FlywayRepairConfig();
        Method factory = Arrays.stream(FlywayRepairConfig.class.getDeclaredMethods())
                .filter(m -> FlywayMigrationStrategy.class.isAssignableFrom(m.getReturnType()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("No FlywayMigrationStrategy factory in FlywayRepairConfig"));
        factory.setAccessible(true);
        strategy = (FlywayMigrationStrategy) factory.invoke(cfg);
    }

    @Test
    void strategyCallsRepairThenMigrate() {
        Flyway flyway = mock(Flyway.class);
        strategy.migrate(flyway);
        InOrder order = inOrder(flyway);
        order.verify(flyway).repair();
        order.verify(flyway).migrate();
        order.verifyNoMoreInteractions();
    }
}
