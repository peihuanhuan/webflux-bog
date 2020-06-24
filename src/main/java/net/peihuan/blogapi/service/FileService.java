package net.peihuan.blogapi.service;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.peihuan.blogapi.entity.Article;
import net.peihuan.blogapi.repository.ArticleRepository;
import net.peihuan.blogapi.utils.CommandUtil;
import net.peihuan.blogapi.utils.FileTypeUtil;
import net.peihuan.blogapi.vo.FileInfoVO;
import net.peihuan.blogapi.vo.RestResult;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.peihuan.blogapi.vo.ResponseAPIStatus.FILE_NOT_EXIST;
import static net.peihuan.blogapi.vo.ResponseAPIStatus.INTERNAL_ERROR;

@Service
@Slf4j
public class FileService {

    @Value("${path.file}")
    private String fileRootPath;
    @Value("${site.file}")
    private String fileSite;

    @Autowired
    private ArticleRepository articleRepository;

    private Pattern fileCountPattern = Pattern.compile("^\\s*(\\d+)\\s*$");

    public Mono<ResponseEntity<RestResult>> upload(FilePart filePart) {
        try {
            String newName = System.currentTimeMillis() + "." + FilenameUtils.getExtension(filePart.filename());
            String pathName = fileRootPath + newName;
            Path path = Paths.get(pathName);
            Path file = Files.createFile(path);
            filePart.transferTo(file.toFile());
            return Mono.just(ResponseEntity.ok(new RestResult("url", fileSite + newName)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Mono.error(e);
        }
    }

    public Mono<ResponseEntity<RestResult>> delete(String fileName) {
        String filePath = fileRootPath + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                return Mono.just(ResponseEntity.ok(new RestResult()));
            } else {
                return Mono.just(ResponseEntity.status(INTERNAL_ERROR.getHttpStatus()).body(new RestResult(INTERNAL_ERROR, "删除图片失败：" + fileName)));
            }
        } else {
            return Mono.just(ResponseEntity.status(FILE_NOT_EXIST.getHttpStatus()).body(new RestResult(FILE_NOT_EXIST, fileName + "不存在")));
        }
    }

    public Flux unUsedFile() {


        List<FileInfoVO> fileInfoVOS = obtainAllFiles();

        log.info("所有的大小 {}", fileInfoVOS.size());

        //List<String> unUsed = new ArrayList<>();

        for (FileInfoVO v:fileInfoVOS) {
            if (v.getName().equals("Heap.rar")) {
                log.info("1111");
            }
        }

        class Obj {
            int cnt = 0;
        }

        Obj obj = new Obj();
        Obj obj2 = new Obj();

        articleRepository.findAllById(595758011L).doOnNext(System.out::println).subscribe();

        Flux<String> usedFiles = articleRepository.findAllById(595758011L)
                .doOnEach(System.out::println)
                .flatMap(article -> {
                    obj.cnt++;
                    log.info("{}", obj.cnt);
                    return Flux.just(article.getTitle());

                    //List<String> list = new ArrayList<>();
                    //obj2.cnt++;
                    //for (FileInfoVO fileInfoVO : fileInfoVOS) {
                    //    String fileName = fileInfoVO.getName();
                    //    if (article.getContent().contains(fileName)) {
                    //        //iterator.remove();
                    //        //unUsed.add(fileName);
                    //        //log.info("未使用{}", unUsed.size());
                    //        //log.info("{} {}", article.getId(), fileName);
                    //
                    //        if (fileName.equals("Heap.rar")) {
                    //            list.add(fileName);
                    //            obj.cnt++;
                    //        }
                    //    }
                    //}
                    //log.info("{} {}", obj2.cnt,obj.cnt);
                    //return Flux.fromIterable(list);
                })
                .distinct();
        //.groupBy(String::toString)
        //.filter(grouped -> grouped.count().equals(Mono.just(1L)))
        //.count()
        //.map(a->ResponseEntity.ok(a)));

        Flux<Integer> anotherFlux = Flux.just(3, 7);
        return Flux.fromStream(fileInfoVOS.stream().map(FileInfoVO::getName))
                .filterWhen(vo -> usedFiles.hasElement(vo).map(a->!a));

    }

    public static void main(String[] args) {
        Flux<Integer> just = Flux.just(1, 2, 3, 4);
        Flux<Integer> just1 = Flux.just(1, 3);
        just.filterWhen(o -> just1.hasElement(o).map(a -> !a))
                .subscribe(System.out::println);
    }

    private List<FileInfoVO> obtainAllFiles() {
        List<FileInfoVO> fileInfoVOS = new ArrayList<>();
        File parentFile = new File(fileRootPath);
        if (!parentFile.exists()) {
            return Lists.newArrayList();
        }
        if (!parentFile.isDirectory()) {
            return Lists.newArrayList();
        }
        File[] subFiles = parentFile.listFiles();
        if (subFiles == null) {
            return Lists.newArrayList();
        }
        for (File file : subFiles) {
            if (file.isFile()) {
                long len=file.length();
                String modifyTime = new DateTime(file.lastModified()).toString("yyyy/MM/dd HH:mm:ss");
                fileInfoVOS.add(new FileInfoVO(file.getName(),len,modifyTime, FileTypeUtil.fileType(file.getName())));
            }
        }
        return fileInfoVOS;
    }

    public Long countFiles() {
        String ls = "ls -lR " + fileRootPath + "| grep \"^-\" | wc -l";
        String countStr = CommandUtil.RunCmmand(ls);
        Matcher matcher = fileCountPattern.matcher(countStr);
        if (matcher.matches()) {
            return Long.valueOf(matcher.group(1));
        }
        return 0L;
    }



}
