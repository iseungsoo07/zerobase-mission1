package zerobase.mission1.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Bookmark {
    private int bookmarkId;
    private String bookmarkGroupId;
    private String X_SWIFI_MGR_NO;
    private LocalDateTime regDate;
}
