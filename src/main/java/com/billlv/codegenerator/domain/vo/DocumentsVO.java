package com.billlv.codegenerator.domain.vo;

import lombok.Data;




@Data
public class DocumentsVO  {


    private Long id;


    private Long businessId;

    private String fileName;

    private String filePath;

    private String fileType;

    private java.util.Date uploadDate;
}

