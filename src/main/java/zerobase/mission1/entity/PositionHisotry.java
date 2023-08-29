package zerobase.mission1.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PositionHisotry {
    private int historyId; // 히스토리 번호
    private double lnt; // x좌표
    private double lat; // y좌표
    private LocalDateTime searchDate; // 조회 일자
}
