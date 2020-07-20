package com.oasd.backend.service;

import com.oasd.backend.domain.History;
import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.repository.HistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class HistoryService {
    private HistoryRepo historyRepo;

    @Autowired
    public HistoryService(HistoryRepo historyRepo) {
        this.historyRepo = historyRepo;
    }

    public void storeHistory(int imageId) {
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<History> historyList = historyRepo.findHistoryByUserID(user.getId());
        long date = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String time = formatter.format(date);
        for (History history : historyList){
            if (history.getImageId() == imageId) {
                historyRepo.updateHistory(history.getId(), time);
                return;
            }
        }

        if (historyList.size() >= 10){
            // delete
            historyRepo.deleteLastHistory(historyList.get(historyList.size()-1).getId());
        }
        historyRepo.insertHistory(user.getId(), imageId, time);
    }

    public List<Integer> getHistory(int userId) {
        return historyRepo.findImageByUserID(userId);
    }
}
