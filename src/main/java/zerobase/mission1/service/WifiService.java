package zerobase.mission1.service;

import zerobase.mission1.Pos;
import zerobase.mission1.dto.WifiDTO;
import zerobase.mission1.entity.PositionHisotry;
import zerobase.mission1.repository.WifiRepository;

import java.util.ArrayList;

public class WifiService {
    WifiRepository wifiRepository = new WifiRepository();

    public boolean saveWifiData() {
        return wifiRepository.saveWifiData();
    }

    public ArrayList<WifiDTO> getWifiList(Pos pos) {
        return wifiRepository.getWifiList(pos);
    }

    public WifiDTO getWifi(String id) {
        return wifiRepository.getWifi(id);
    }

    public ArrayList<PositionHisotry> getHistory() {
        return wifiRepository.getHistory();
    }

    public boolean removeHistory(int id) {
        return wifiRepository.deleteHistory(id);
    }

}
