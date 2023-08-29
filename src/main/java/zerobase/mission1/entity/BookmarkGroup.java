package zerobase.mission1.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookmarkGroup {
    private int bookmarkGroupId;
    private String bookmarkGroupName;
    private int bookmarkGroupSeq;
    private LocalDateTime regDate;
    private LocalDateTime modifyDate;
}
