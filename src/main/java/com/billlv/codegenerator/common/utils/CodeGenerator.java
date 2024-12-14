package com.billlv.codegenerator.common.utils;

import com.billlv.codegenerator.domain.entity.TableFieldMetadata;
import com.billlv.codegenerator.domain.entity.TableMetaData;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeGenerator {
    private static final String TEMPLATE_PATH = "templates/";



    public static void generateCode(TableMetaData tableMetaData, List<TableFieldMetadata> columns, String basePath, String basePackage) throws IOException {
        VelocityEngine velocityEngine = VelocityEngineInitializer.initVelocityEngine();

        Map<String, Object> contextMap = new HashMap<>();
        contextMap.put("packageName", basePackage);
        contextMap.put("className", tableMetaData.getClassName());
        contextMap.put("classNameLower", CodeGenUtils.capitalizeFirstLetter(tableMetaData.getClassName()));
        contextMap.put("tableName", tableMetaData.getTableName());
        contextMap.put("fields", columns);
        contextMap.put("primaryKeyType", getPrimaryKeyType(columns));

        // 根据模板类型生成文件
        generateFile(velocityEngine, contextMap, "entity.vm", basePath + "/domain/entity/" + tableMetaData.getClassName()+"Entity" + ".java");
        generateFile(velocityEngine, contextMap, "dto.vm", basePath + "/domain/dto/" + tableMetaData.getClassName()+"DTO" + ".java");
        generateFile(velocityEngine, contextMap, "vo.vm", basePath + "/domain/vo/" + tableMetaData.getClassName() +"VO"+ ".java");
        generateFile(velocityEngine, contextMap, "repository.vm", basePath + "/repository/" + tableMetaData.getClassName() + "Repository.java");
        generateFile(velocityEngine, contextMap, "service.vm", basePath + "/service/" + tableMetaData.getClassName() + "Service.java");
        generateFile(velocityEngine, contextMap, "service-impl.vm", basePath + "/service/impl/" + tableMetaData.getClassName() + "ServiceImpl.java");
        generateFile(velocityEngine, contextMap, "controller.vm", basePath + "/controller/" + tableMetaData.getClassName() + "Controller.java");
        generateFile(velocityEngine, contextMap, "mapper.vm", basePath + "/mapper/" + tableMetaData.getClassName()+ "Mapper.java");
        generateFile(velocityEngine, contextMap, "specification.vm", basePath + "/specification/" + tableMetaData.getClassName()+ "Specification.java");
        generateFile(velocityEngine, contextMap, "query-condition.vm", basePath + "/specification/" + "QueryCondition.java");
    }


    private static void generateFile(VelocityEngine velocityEngine, Map<String, Object> contextMap, String templateName, String outputFilePath) throws IOException {
        Template template = velocityEngine.getTemplate(TEMPLATE_PATH + templateName);
        VelocityContext context = new VelocityContext(contextMap);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        File outputFile = new File(outputFilePath);
        if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
            throw new IOException("Failed to create directories for " + outputFilePath);
        }

        try (FileWriter fileWriter = new FileWriter(outputFile)) {
            fileWriter.write(writer.toString());
        }
    }

    private static String getPrimaryKeyType(List<TableFieldMetadata> columns) {
        return columns.stream()
                .filter(c->"PRI".equals(c.getColumnKey()))
                .map(TableFieldMetadata::getJavaType)
                .findFirst()
                .orElse("Object");
    }

}
