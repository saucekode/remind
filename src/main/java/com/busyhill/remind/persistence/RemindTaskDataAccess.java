package com.busyhill.remind.persistence;

import com.busyhill.remind.data.RemindTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RemindTaskDataAccess extends JpaRepository<RemindTask, Long> {
}
