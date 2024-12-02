package com.web.hotel.repository.custom.impl;

import com.web.hotel.model.dto.HotelDTO;
import com.web.hotel.model.entity.HotelEntity;
import com.web.hotel.repository.custom.HotelRepositoryCustom;
import com.web.hotel.util.DoubleUtil;
import com.web.hotel.util.LongUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class HotelRepositoryCustomImpl implements HotelRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public void joinTable(StringBuilder sql, HotelDTO hotel) {
    }

    public void queryNormal(StringBuilder sql, HotelDTO hotel) {
        try{
            Field[] fields = hotel.getClass().getDeclaredFields();
            for(Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if(!fieldName.equals("price") && !fieldName.equals("categories")
                        && !fieldName.equals("description") && !fieldName.equals("detail")
                        && !fieldName.equals("files") && !fieldName.equals("numberOfRoom")) {
                    Object obj = field.get(hotel);
                    if(obj != null){
                        String value = obj.toString();
                        if(value != null && !value.equals("")){
                            if(LongUtil.isLong(value) || DoubleUtil.isDouble(value)){
                                sql.append(" AND h." + fieldName + " = " + value + " ");
                            }
                            else {
                                sql.append(" AND  h." + fieldName + " LIKE '%" + value + "%' ");
                            }
                        }

                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void querySpecial(StringBuilder sql, HotelDTO hotel) {
        Double price = hotel.getPrice();
        if(price != null){
            double max = price + 100;
            double min = price - 100;
            sql.append(" AND h.price <= " + max + " AND h.price >= " + min + " ");
        }
        List<String> categories = hotel.getCategories();
        if(categories != null && !categories.isEmpty()){
            sql.append(" AND ( ");
            sql.append(categories.stream().map(it -> " h.category LIKE '%" + it + "%'")
                    .collect(Collectors.joining(" OR ")));
            sql.append(") ");
        }
        sql.append(" AND h.number_of_room > 0 ");
    }


    @Override
    public List<HotelEntity> findByRequestParam(HotelDTO hotelDTO) {
        StringBuilder sql = new StringBuilder("SELECT * FROM hotel h ");
        sql.append(" WHERE 1 = 1 ");
        queryNormal(sql, hotelDTO);
        querySpecial(sql, hotelDTO);
        sql.append(" ORDER BY h.name ASC, h.price ASC, h.number_of_room ASC ");
        Query query = entityManager.createNativeQuery(sql.toString(), HotelEntity.class);
        return query.getResultList();
    }
}
