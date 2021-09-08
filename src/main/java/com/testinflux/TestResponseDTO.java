package com.testinflux;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TestResponseDTO {
    private String time;
    private String username;
    private String password;
    private String avatar;
}
