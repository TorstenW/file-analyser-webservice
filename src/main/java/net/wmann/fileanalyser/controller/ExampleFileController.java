package net.wmann.fileanalyser.controller;

import lombok.extern.slf4j.Slf4j;
import net.wmann.fileanalyser.controller.api.ExampleFileControllerApi;
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
@Slf4j
public class ExampleFileController implements ExampleFileControllerApi {

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
