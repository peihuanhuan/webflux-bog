package net.peihuan.blogapi.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.peihuan.blogapi.service.FileService;
import net.peihuan.blogapi.vo.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@Slf4j
@RequestMapping("file")
public class FileController {


    @Autowired
    private FileService fileService;

    @PostMapping("upload")
    public Mono<ResponseEntity<RestResult>> singleFileUpload(@RequestPart("file") FilePart filePart) {
        return fileService.upload(filePart);
    }

    @DeleteMapping
    public Mono<ResponseEntity<RestResult>> delelte(String fileName) {
        return fileService.delete(fileName);
    }

    @GetMapping("unUsed")
    public Flux unUsed() {
        return fileService.unUsedFile();
    }
}

