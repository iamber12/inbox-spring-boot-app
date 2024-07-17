package io.javabrains.inbox.folders;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnreadEmailStatsRepository extends CrudRepository<UnreadEmailStats, String> {

    @Query("update unread_email_stats set unreadcount = unreadcount+1 where user_id = ?0 and label = ?1")
    public void incrementUnreadCount(String userId, String label);

    @Query("update unread_email_stats set unreadcount = unreadcount-1 where user_id = ?0 and label = ?1")
    public void decrementUnreadCount(String userId, String label);
}