package com.seed.portal.controller.rest;

import com.seed.base.annotation.ApiInfo;
import com.seed.base.model.PackVo;
import com.seed.base.model.business.BusinessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/7 16:21
 */
@Slf4j
@RestController
@RequestMapping(path = "/rest/file")
@Api(tags = "file upload")
public class FileController {

    /**
     * Be sure to remove content type if you test the api in Postman.
     * That application will add Content-type automatically.
     *
     * @param file the file
     * @return     the result
     */
    @ApiInfo(auth = false)
    @ApiOperation("File upload")
    @PostMapping(value = "/upload")
    public BusinessResponse uploadFile(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) return PackVo.fail().toRawResponse();
        String fileName = file.getOriginalFilename();
        PackVo packVo;
        try {
            String path = "D:/upload/" ;
            File dest = new File(path + fileName);
            if(!dest.getParentFile().exists()){
                dest.getParentFile().mkdir();
            }
            file.transferTo(dest);
            packVo = PackVo.success();
        } catch (IOException e) {
            log.error("Failed to save file: ", e);
            packVo = PackVo.fail();
        }
        packVo.setUdf4(fileName);
        return packVo.toRawResponse();
    }

    @ResponseBody
    @ApiInfo(auth = false)
    @ApiOperation("File download")
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Object> downloadFile(@PathVariable(name = "fileName") String fileName) {
        String path = "D:/upload/" ;
        File file = new File(path + fileName);
        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add ( "Content-Disposition",String.format("attachment;filename=\"%s",fileName));
        headers.add ( "Cache-Control","no-cache,no-store,must-revalidate" );
        headers.add ( "Pragma","no-cache" );
        headers.add ( "Expires","0" );

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/txt"))
                .body(resource);
    }

    /**
     * Display image, this method used to display image in web page tag like image src.
     *
     * @param fileName image file name
     * @return         result, image data
     */
    @ResponseBody
    @ApiInfo(auth = false)
    @ApiOperation("Image display")
    @GetMapping("/get/{fileName}")
    public ResponseEntity<Object> getFile(@PathVariable(name = "fileName") String fileName) {
        String path = "D:/upload/" ;
        File file = new File(path + fileName);
        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add ( "Content-Disposition",String.format("inline;filename=\"%s",fileName));
        headers.add ( "Cache-Control","no-cache,no-store,must-revalidate" );
        headers.add ( "Pragma","no-cache" );
        headers.add ( "Expires","0" );

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }

}
