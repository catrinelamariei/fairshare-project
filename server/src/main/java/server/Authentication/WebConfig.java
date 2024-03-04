package server.Authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //use authenticator for all URLs regarding "/data" endpoint
        registry.addInterceptor(new Authenticator()).addPathPatterns("/data", "/data/**");
    }
}
