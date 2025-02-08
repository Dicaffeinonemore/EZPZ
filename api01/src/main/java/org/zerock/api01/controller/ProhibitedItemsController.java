package org.zerock.api01.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.api01.domain.Prohibit;
import org.zerock.api01.service.ProhibitedItemsService;


@RestController
@RequestMapping("/api/prohibit-items")
public class ProhibitedItemsController {

    private final ProhibitedItemsService service;

    public ProhibitedItemsController(ProhibitedItemsService service) {
        this.service = service;
    }

    @GetMapping
    public List<Prohibit> getAllItems() {
        return service.getAllItems();
    }

    @GetMapping("/category")
    public List<Prohibit> getItemsByCategory(@RequestParam String gubun) {
        return service.getItemsByCategory(gubun);
    }
}

