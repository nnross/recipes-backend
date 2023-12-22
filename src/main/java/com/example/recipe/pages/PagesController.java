package com.example.recipe.pages;

import com.example.recipe.response.PersonalPageRes;
import com.example.recipe.response.TodaysPageRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for calls where you get the whole page of data.
 */
@RestController
@RequestMapping("/pages")
public class PagesController {
    @Autowired
    private PagesService pagesService;

    /**
     * API GET call to /pages/get/personal?accountId=(id)
     * will return the personal page for the selected account.
     * Authorization with accountId being same as in context.
     * @param accountId
     *        id of account we want.
     * @return personal page data.
     */
    @PreAuthorize("#accountId == authentication.principal.id")
    @GetMapping("/get/personal")
    public PersonalPageRes getPersonal(@RequestParam int accountId) {
        return pagesService.getPersonal(accountId);
    }

    /**
     * API GET call to /pages/get/todays?accountId=(id)
     * will return the today's page for the selected account.
     * Authorization with accountId being the same as in context.
     * @param accountId
     *        id of account we want.
     * @return today's page data.
     */
    @PreAuthorize("#accountId == authentication.principal.id")
    @GetMapping("/get/todays")
    public TodaysPageRes getTodays(@RequestParam int accountId) {
        return pagesService.getTodays(accountId);
    }
}
