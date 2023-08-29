package zerobase.mission1.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookmarkDTO {
    private int bookmarkId;
    private String bookmarkGroupName;
    private String wifiName;
    private LocalDateTime regDate;
}
