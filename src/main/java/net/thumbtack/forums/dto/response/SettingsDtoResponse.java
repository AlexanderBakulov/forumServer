package net.thumbtack.forums.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingsDtoResponse {


    private Integer banTimes;
    private Integer maxBanCount;
    private int maxNameLength;
    private int minPasswordLength;

    public SettingsDtoResponse( Integer banTimes, Integer maxBanCount, int maxNameLength, int minPasswordLength) {
        this.banTimes = banTimes;
        this.maxBanCount = maxBanCount;
        this.maxNameLength = maxNameLength;
        this.minPasswordLength = minPasswordLength;

    }

    public SettingsDtoResponse(int maxNameLength, int minPasswordLength) {
        this(null, null, maxNameLength, minPasswordLength);
    }

    public Integer getBanTimes() {
        return banTimes;
    }

    public Integer getMaxBanCount() {
        return maxBanCount;
    }

    public int getMaxNameLength() {
        return maxNameLength;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

}
