
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import service.InitService;

@SpringBootApplication
@EnableJpaRepositories("repository")
@ComponentScan({"controller", "config"})
public class App {

    // POINT D'ENTREE
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            InitService initService = ctx.getBean(InitService.class);

            // Initialisation
            initService.clear();
            initService.demo();

            // Production of sensor data.
            if (args.length > 0) {
                initService.fill(Long.parseLong(args[0]));
            } else {
                initService.fill();
            }
        };
    }
}
