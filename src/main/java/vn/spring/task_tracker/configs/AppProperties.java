package vn.spring.task_tracker.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Setter
@Getter
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private Cors cors = new Cors();

    @Setter
    @Getter
    public static class Cors {
        private List<String> allowedOrigins = List.of("http://localhost:5173");
    }
}
