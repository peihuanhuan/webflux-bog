package net.peihuan.blogapi.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
public class CommandUtil {

    public static String RunCmmand(String command) {
        try {
            String[] commands = { "/bin/sh", "-c", command};
            Process process = Runtime.getRuntime().exec(commands);
            int i = process.waitFor();
            System.out.println(i);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        throw new RuntimeException("执行命令异常");
    }
}
