package io.javabrains.inbox.controllers;

import io.javabrains.inbox.email.EmailService;
import org.springframework.ui.Model;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;
import io.javabrains.inbox.folders.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;


@Controller
public class ComposeController {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderService folderService;

    @Autowired
    private EmailService emailService;

    @GetMapping(value="/compose")
    public String getComposePage(@RequestParam(required = false) String to, @AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }

        // Fetch folders
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllByUserId(userId);
        model.addAttribute("userFolders", userFolders);

        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);

        List<String> toIds = getStringSet(to);

        model.addAttribute("toIds", String.join(", ", toIds));

        return "compose-page";
    }

    private static List<String> getStringSet(String to) {
        if(!StringUtils.hasText(to)) {
            return new ArrayList<>();
        }
        String[] uniqueToIds = to.split(",");
        Set<String> toIds = new HashSet<>(Arrays.asList(uniqueToIds));
        return new ArrayList<>(toIds);
    }

    @PostMapping(value = "/sendEmail")
    public ModelAndView sendEmail(@RequestBody MultiValueMap<String, String> formdata, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return new ModelAndView("redirect:/");
        }

        String from = principal.getAttribute("login");
        List<String> toIds = getStringSet(formdata.getFirst("toIds"));
        String body = formdata.getFirst("body");
        String subject = formdata.getFirst("subject");
        emailService.sendEmail(from, toIds, subject, body);

        return new ModelAndView("redirect:/");
    }
}
