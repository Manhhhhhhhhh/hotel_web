package com.web.hotel.enums;

import java.util.HashMap;
import java.util.Map;

public enum CategoryEnum {
    Resort("Khách sạn nghỉ dưỡng"),
    Commercial("Khách sạn thương mại"),
    Hostel("Khách sạn nhà nghỉ bình dân"),
    Condotel("Căn hộ khách sạn"),
    Casino("Khách sạn Casino"),
    Airport("Khách sạn sân bay"),
    Motel("Khách sạn ven xa lộ"),
    Pod("Khách sạn buồng kén");

    private final String name;
    CategoryEnum(String name) {
        this.name = name;
    }

    public static Map<String, String> getCategoryEnumMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (CategoryEnum categoryEnum : CategoryEnum.values()) {
            map.put(categoryEnum.toString(), categoryEnum.name());
        }
        return map;
    }
}
