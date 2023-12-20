package com.example.recipe.pages;

import com.example.recipe.recipe.Recipe;
import com.example.recipe.recipe.RecipeService;
import com.example.recipe.response.PersonalPageRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pages")
public class PagesController {
    @Autowired
    private PagesService pagesService;

    /**
     * API GET call to /api/pages/get/personal?accountId=(id)
     * will return the personal page for the selected account.
     * @param accountId
     *        id of account we want.
     * @return personal page data.
     */
    @PreAuthorize("#accountId == authentication.principal.id")
    @GetMapping("/get/personal")
    public PersonalPageRes add(@RequestParam int accountId) {
        return pagesService.getPersonal(accountId);
    }
}
