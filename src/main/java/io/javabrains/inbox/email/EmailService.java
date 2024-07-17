package io.javabrains.inbox.email;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import io.javabrains.inbox.emailList.EmailListItem;
import io.javabrains.inbox.emailList.EmailListItemKey;
import io.javabrains.inbox.emailList.EmailListItemRepository;
import io.javabrains.inbox.folders.UnreadEmailStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    EmailListItemRepository emailListItemRepository;

    @Autowired
    UnreadEmailStatsRepository unreadEmailStatsRepository;

    public void sendEmail(String from, List<String> to, String subject, String body) {
        Email email = new Email();
        email.setFrom(from);
        email.setTo(to);
        email.setSubject(subject);
        email.setBody(body);
        email.setId(Uuids.timeBased());
        emailRepository.save(email);

        to.forEach(toId -> {
            EmailListItem emailListItem = createEmailListItem(to, subject, toId, email, "Inbox");
            emailListItemRepository.save(emailListItem);
            unreadEmailStatsRepository.incrementUnreadCount(toId, "Inbox");
        });

        EmailListItem sentEmailListItem = createEmailListItem(to, subject, from, email, "Sent");
        sentEmailListItem.setUnread(false);
        emailListItemRepository.save(sentEmailListItem);
    }

    public boolean hasAccess(Email email, String userId) {
        return userId.equals(email.getFrom()) || email.getTo().contains(userId);
    }

    public String getReplySubject(String subject) {
        return "Re: " + subject;
    }

    public String getReplyBody(Email email) {
        return "\n\n\n----------------------------------------- \n" +
                "From: " + email.getFrom() + "\n" +
                "To: " + email.getTo() + "\n\n" +
                email.getBody();
    }

    private EmailListItem createEmailListItem(List<String> to, String subject, String itemOwner, Email email, String folder) {
        EmailListItemKey key = new EmailListItemKey();
        key.setTimeUUID(email.getId());
        key.setUserId(itemOwner);
        key.setLabel(folder);

        EmailListItem emailListItem = new EmailListItem();
        emailListItem.setKey(key);
        emailListItem.setTo(to);
        emailListItem.setSubject(subject);
        emailListItem.setUnread(true);

        return emailListItem;
    }
}
