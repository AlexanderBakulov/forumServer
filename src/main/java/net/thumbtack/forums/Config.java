package net.thumbtack.forums;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class Config {

    public static final String INVALID_EMAIL_FORMAT = "Invalid email format.";
    public static final String INVALID_NAME = "Pame must contains only letters and numbers.";
    public static final String INVALID_PASSWORD = "Password must contains at least one lowercase letter, " +
                                                    "one uppercase letter and one digit.";
    public static final String NOT_NULL = "Value can not be null.";
    public static final String NOT_BLANK = "Value can not be blank.";

    private final int maxNameLength;
    private final int minPasswordLength;
    private final int banTime;
    private final int maxBanCount;
    private final int port;

    public Config(@Value("${max_name_length}") int maxNameLength,
                  @Value("${min_password_length}") int minPasswordLength,
                  @Value("${ban_time}") int banTime,
                  @Value("${max_ban_count}") int maxBanCount,
                  @Value("${server.port}") int port) {
        this.maxNameLength = maxNameLength;
        this.minPasswordLength = minPasswordLength;
        this.banTime = banTime;
        this.maxBanCount = maxBanCount;
        this.port = port;
    }

    public int getMaxNameLength() {
        return maxNameLength;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    public int getBanTime() {
        return banTime;
    }

    public int getMaxBanCount() {
        return maxBanCount;
    }

    public int getPort() {
        return port;
    }
}
