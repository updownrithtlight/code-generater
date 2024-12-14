package com.billlv.codegenerator.service;

import com.billlv.codegenerator.domain.entity.TableFieldMetadata;
import com.billlv.codegenerator.domain.entity.TableMetaData;
import com.billlv.codegenerator.common.utils.CodeGenerator;
import com.billlv.codegenerator.common.utils.FileUtils;
import com.billlv.codegenerator.domain.vo.ProjectConfigVO;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
@Service
public class CodeGenService {

    private final DatabaseService databaseService;
    private final ProjectConfigService projectConfigService;
    private final TableMetaDataService tableMetaDataService;

    public CodeGenService(TableMetaDataService tableMetaDataService,DatabaseService databaseService, ProjectConfigService projectConfigService) {
        this.tableMetaDataService = tableMetaDataService;
        this.databaseService = databaseService;
        this.projectConfigService = projectConfigService;
    }

    public Path generateAndZipCodeForTables(List<Long> tableIds) throws Exception {
        // 创建基于项目目录的临时目录
        Path tempDir = Paths.get(System.getProperty("user.dir"), "temp", "codegen");
        FileUtils.recreateDirectory(tempDir);

        ProjectConfigVO configVO = projectConfigService.findByConfigKey("basePackage");
        String basePackage=configVO.getConfigValue();
        // 指定 ZIP 文件路径
        Path zipFilePath = tempDir.resolve("generated_code.zip");

        // 定义基础包路径
        String basePackagePath = basePackage.replace('.', File.separatorChar);
        for (Long tableId : tableIds) {
            TableMetaData tableMetaData = tableMetaDataService.getTableMetaDataById(tableId);
            List<TableFieldMetadata> columns = tableMetaDataService.getColumnsByTableId(tableId);

            // 定义模块路径
            Path entityDir = tempDir.resolve(basePackagePath).resolve("domain.entity");
            Path dtoDir = tempDir.resolve(basePackagePath).resolve("domain.dto");
            Path voDir = tempDir.resolve(basePackagePath).resolve("domain.vo");
            Path serviceDir = tempDir.resolve(basePackagePath).resolve("service");
            Path serviceImplDir = serviceDir.resolve("impl");
            Path controllerDir = tempDir.resolve(basePackagePath).resolve("controller");
            Path repositoryDir = tempDir.resolve(basePackagePath).resolve("repository");
            Path mapperDir = tempDir.resolve(basePackagePath).resolve("mapper");
            Path specificationDir = tempDir.resolve(basePackagePath).resolve("specification");

            // 确保基础目录只创建一次
            Files.createDirectories(entityDir);
            Files.createDirectories(dtoDir);
            Files.createDirectories(voDir);
            Files.createDirectories(serviceDir);
            Files.createDirectories(serviceImplDir);
            Files.createDirectories(controllerDir);
            Files.createDirectories(repositoryDir);
            Files.createDirectories(mapperDir);
            Files.createDirectories(specificationDir);

            // 生成代码文件
            CodeGenerator.generateCode(tableMetaData, columns,tempDir.resolve(basePackagePath).toString(),basePackage);
        }

        // 压缩目录为 ZIP 文件
        zipDirectory(tempDir, zipFilePath);

        return zipFilePath;
    }



    private void zipDirectory(Path sourceDir, Path zipFilePath) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
            Files.walk(sourceDir)
                    .filter(Files::isRegularFile) // 只处理文件
                    .filter(file -> !file.equals(zipFilePath)) // 排除压缩包自身
                    .forEach(file -> {
                        try {
                            // 创建相对路径的 ZipEntry
                            ZipEntry zipEntry = new ZipEntry(sourceDir.relativize(file).toString().replace("\\", "/"));
                            zos.putNextEntry(zipEntry);

                            // 写入文件内容到 ZIP
                            Files.copy(file, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            throw new RuntimeException("Error while zipping files", e);
                        }
                    });
        }
    }






}