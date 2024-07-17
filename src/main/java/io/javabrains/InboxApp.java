package io.javabrains;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import io.javabrains.inbox.email.Email;
import io.javabrains.inbox.email.EmailRepository;
import io.javabrains.inbox.email.EmailService;
import io.javabrains.inbox.emailList.EmailListItem;
import io.javabrains.inbox.emailList.EmailListItemKey;
import io.javabrains.inbox.emailList.EmailListItemRepository;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;
import io.javabrains.inbox.folders.UnreadEmailStatsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.nio.file.Path;
import java.util.Arrays;

@SpringBootApplication
@RestController
public class InboxApp {
    @Autowired
    FolderRepository folderRepository;

    @Autowired
    EmailListItemRepository emailListItemRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UnreadEmailStatsRepository unreadEmailStatsRepository;

    @Autowired
    private EmailService emailService;

    public static void main(String[] args) {
        SpringApplication.run(InboxApp.class, args);
    }

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }

    @PostConstruct
    public void init() {
        folderRepository.save(new Folder("iamber12", "Work", "blue"));
        folderRepository.save(new Folder("iamber12", "Home", "green"));
        folderRepository.save(new Folder("iamber12", "Masters", "yellow"));

        for(int i=0; i<10; i++) {
            emailService.sendEmail("iamber12", Arrays.asList("iamber12", "testing-id1"), "Testing " + i, "Testing body");
        }
    }
}
