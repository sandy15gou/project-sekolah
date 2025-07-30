package com.sandy.project.service;

import com.sandy.project.dto.ScheduleDetailDTO;
import java.util.List;

public interface ScheduleService {
    ScheduleDetailDTO findScheduleDetail(String scheduleId);
    List<ScheduleDetailDTO> findAllSchedules();
    void createSchedule(ScheduleDetailDTO dto);
    void updateSchedule(String scheduleId, ScheduleDetailDTO dto);
    void deleteSchedule(String scheduleId);
}

