package io.javabrains.inbox.controllers;

import io.javabrains.inbox.email.Email;
import io.javabrains.inbox.email.EmailRepository;
import io.javabrains.inbox.email.EmailService;
import io.javabrains.inbox.emailList.EmailListItem;
import io.javabrains.inbox.emailList.EmailListItemKey;
import io.javabrains.inbox.emailList.EmailListItemRepository;
import io.javabrains.inbox.folders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class EmailViewController {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderService folderService;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UnreadEmailStatsRepository unreadEmailStatsRepository;

    @Autowired
    private EmailListItemRepository emailListItemRepository;
    @Autowired
    private EmailService emailService;

    @GetMapping(value="/emails/{id}")
    public String emailView(@RequestParam String folder, @PathVariable UUID id, @AuthenticationPrincipal OAuth2User principal, Model model) {

        // check auth
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }


        // Fetch folders
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllByUserId(userId);
        model.addAttribute("userFolders", userFolders);

        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);

        // Fetch mail
        Optional<Email> optionalEmail = emailRepository.findById(id);
        if(optionalEmail.isEmpty()) {
            return "inbox-page";
        }

        Email email = optionalEmail.get();
        String toIds = String.join(",", email.getTo());

        // Check if user is allowed to access the mail
        if(!emailService.hasAccess(email, userId)) {
            return "redirect:/";
        }
        model.addAttribute("email", email);
        model.addAttribute("toIds", toIds);

        EmailListItemKey emailListItemKey = new EmailListItemKey();
        emailListItemKey.setUserId(userId);
        emailListItemKey.setLabel(folder);
        emailListItemKey.setTimeUUID(email.getId());

        Optional<EmailListItem> optionalEmailListItem = emailListItemRepository.findById(emailListItemKey);
        if(optionalEmailListItem.isPresent()) {
            EmailListItem emailListItem = optionalEmailListItem.get();
            if(emailListItem.isUnread()) {
                emailListItem.setUnread(false);
                emailListItemRepository.save(emailListItem);
                unreadEmailStatsRepository.decrementUnreadCount(userId, folder);
            }
        }
        model.addAttribute("stats", folderService.mapCountToLabels(userId));
        model.addAttribute("username", principal.getAttribute("name"));

        return "email-page";
    }
}
