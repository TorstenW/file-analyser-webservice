package net.wmann.fileanalyser.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ExampleFileController {

    private static final Logger log = LogManager.getLogger();

    private static final String EXAMPLE_FILE = "example_speeches.csv";

    @RequestMapping(value = "/examplefile/{prefix}", method = RequestMethod.GET)
    public ResponseEntity exampleFile(@PathVariable(name = "prefix") String prefix) throws IOException {
        log.info("Received GET request to /examplefile/" + prefix);
        final String filename = prefix + "_" + EXAMPLE_FILE;
        ClassPathResource csvFile = new ClassPathResource("static/" + filename);

        return ResponseEntity
                .ok()
                .contentLength(csvFile.contentLength())
                .contentType(MediaType.parseMediaType("text/csv"))
                .header("Content-disposition", "attachment; filename=" + filename)
                .body(new InputStreamResource(csvFile.getInputStream()));
    }

}
