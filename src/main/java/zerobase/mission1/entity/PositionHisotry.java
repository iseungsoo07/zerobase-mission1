package zerobase.mission1.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PositionHisotry {
    private int historyId;
    private double lnt;
    private double lat;
    private LocalDateTime searchDate;
}
