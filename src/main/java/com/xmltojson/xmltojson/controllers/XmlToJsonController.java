package com.xmltojson.xmltojson.controllers;


import com.xmltojson.xmltojson.utils.XmlToJsonConverter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/xml-to-json")
public class XmlToJsonController {

    @PostMapping("upload")
    public ResponseEntity<String> uploadXml(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload.");
        }
        try {
            String jsonResult = XmlToJsonConverter.convertXmlToJson(file).toString();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonResult);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error processing the XML file: " + e.getMessage());
        }
    }

}

