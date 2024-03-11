package io.weesvc.springboot.weesvc.api;

import io.weesvc.springboot.weesvc.WeesvcApplication;
import io.weesvc.springboot.weesvc.domain.PlacesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.output.WaitingConsumer;
import org.testcontainers.k6.K6Container;
import org.testcontainers.utility.MountableFile;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = { WeesvcApplication.class, PlacesRestController.class, PlacesRepository.class },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "spring.r2dbc.url=r2dbc:tc:postgresql:///testdb?TC_IMAGE_TAG=15.3-alpine" }
)
public class APIContractTests {

    @LocalServerPort
    private Integer port;

    @Test
    public void validateAPIContract() throws Exception {

        Testcontainers.exposeHostPorts(port);
        try (
            K6Container container = new K6Container("grafana/k6:0.49.0")
                    .withTestScript(MountableFile.forClasspathResource("api-compliance.js"))
                    .withScriptVar("HOST", "host.testcontainers.internal")
                    .withScriptVar("PORT", port.toString())
                    .withCmdOptions("--no-usage-report")
        ) {
            container.start();

            WaitingConsumer consumer = new WaitingConsumer();
            container.followOutput(consumer);

            // Wait for test script results to be collected
            consumer.waitUntil(
                    frame -> frame.getUtf8String().contains("iteration_duration"),
                    1,
                    TimeUnit.MINUTES
            );

            assertThat(container.getLogs()).doesNotContain("thresholds on metrics 'checks' have been crossed");
// Force a "passing" test to fail to see output results from k6 Test
//            assertThat(container.getLogs()).contains("thresholds on metrics 'checks' have been crossed");

        }

    }

}
