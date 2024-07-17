package io.javabrains.inbox.email;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import io.javabrains.inbox.emailList.EmailListItem;
import io.javabrains.inbox.emailList.EmailListItemKey;
import io.javabrains.inbox.emailList.EmailListItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    EmailListItemRepository emailListItemRepository;


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
        });

        EmailListItem sentEmailListItem = createEmailListItem(to, subject, from, email, "Sent");
        emailListItemRepository.save(sentEmailListItem);
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
