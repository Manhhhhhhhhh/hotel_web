package com.web.hotel.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {
    private String name;
    private String address;
    private String price;
    private String rate;
    private List<String> categories;
    private String description;
    private String detail;
    private List<String> fileStrings;
    private List<CommentResponse> commentResponses;
}
