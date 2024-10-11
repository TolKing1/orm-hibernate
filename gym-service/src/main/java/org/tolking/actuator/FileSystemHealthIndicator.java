package org.tolking.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class FileSystemHealthIndicator implements HealthIndicator {
    private static final long MIN_FREE_DISK_SPACE = 100 * 1024 * 1024; // 100 MB

    @Override
    public Health health() {
        String localDir = System.getProperty("user.dir");
        Path path = Paths.get(localDir);
        File directory = path.toFile();

        if (!directory.canRead() || !directory.canWrite()) {
            return Health.down()
                    .withDetail("error", "Directory does not have required read/write permissions")
                    .build();
        }

        return getHealth(path);
    }

    private static Health getHealth(Path path) {
        try {
            long freeDiskSpace = Files.getFileStore(path).getUsableSpace();
            String freeDiskSpaceStr = freeDiskSpace / 1024 / 1024 + " mb";

            if (freeDiskSpace < MIN_FREE_DISK_SPACE) {
                return Health.down()
                        .withDetail("error", "Insufficient disk space (min: 100mb)")
                        .withDetail("freeSpace", freeDiskSpaceStr )
                        .build();
            }else {
                return Health.up()
                        .withDetail("freeSpace", freeDiskSpaceStr)
                        .withDetail("directory", path.toAbsolutePath().toString())
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", "Error checking disk space: " + e.getMessage())
                    .build();
        }
    }
}
