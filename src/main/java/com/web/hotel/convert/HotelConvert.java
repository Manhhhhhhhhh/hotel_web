package com.web.hotel.convert;

import com.web.hotel.enums.CategoryEnum;
import com.web.hotel.model.dto.FileDTO;
import com.web.hotel.model.entity.FileEntity;
import com.web.hotel.model.entity.HotelEntity;
import com.web.hotel.model.response.HotelResponse;
import com.web.hotel.service.HotelService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HotelConvert {

    @Autowired
    private  ModelMapper modelMapper;

    public HotelResponse convertTo(HotelEntity hotelEntity) {
        HotelResponse hotelResponse = modelMapper.map(hotelEntity, HotelResponse.class);
        // convert category
        if(hotelEntity.getCategory() != null && !hotelEntity.getCategory().isEmpty()){
            String[] list = hotelEntity.getCategory().split(",");
            List<String> categories = new ArrayList<>();
            for(String category : list) {
                categories.add(CategoryEnum.getCategoryEnumMap().get(category));
            }
            hotelResponse.setCategories(categories);
        }
        // convert fileDTOs
        List<FileEntity> fileEntities = hotelEntity.getFiles();
        List<FileDTO> fileDTOs = new ArrayList<>();
        for(FileEntity fileEntity : fileEntities) {
            FileDTO fileDTO = modelMapper.map(fileEntity, FileDTO.class);
            fileDTOs.add(fileDTO);
        }
        hotelResponse.setFileDTOs(fileDTOs);

        return hotelResponse;
    }
}
