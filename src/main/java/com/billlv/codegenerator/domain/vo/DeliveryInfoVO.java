package com.billlv.codegenerator.domain.vo;

import lombok.Data;




@Data
public class DeliveryInfoVO  {


    private Long id;


    private String deliveryName;

    private String deliveryNumber;

    private Integer quantity;

    private String shipmentDate;

    private String taskId;
}

