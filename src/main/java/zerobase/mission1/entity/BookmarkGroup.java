package zerobase.mission1.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookmarkGroup {
    private int bookmarkGroupId; // 북마크 그룹의 아이디
    private String bookmarkGroupName; // 북마크 이름
    private int bookmarkGroupSeq; // 순서
    private LocalDateTime regDate; // 북마크 그룹 등록일자
    private LocalDateTime modifyDate; // 북마크 그룹 수정일자
}
