package zerobase.mission1.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Bookmark {
    private int bookmarkId; // 북마크 하나하나의 아이디
    private String bookmarkGroupId; // 북마크 그룹의 아이디
    private String X_SWIFI_MGR_NO; // 와이파이 관리번호
    private LocalDateTime regDate; // 북마크 하나의 등록일자
}
