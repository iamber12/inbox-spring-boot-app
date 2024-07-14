package io.javabrains;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Data
@Configuration
@ConfigurationProperties(prefix="datastax.astra")
public class DataStaxAstraProperties {

    private File secureConnectBundle;
}
