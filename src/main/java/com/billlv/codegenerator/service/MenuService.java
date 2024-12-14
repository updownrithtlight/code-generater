package com.billlv.codegenerator.service;


import com.billlv.codegenerator.domain.entity.Menu;
import com.billlv.codegenerator.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }

    public Menu createMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public Optional<Menu> updateMenu(Long id, Menu menu) {
        return menuRepository.findById(id).map(existingMenu -> {
            existingMenu.setName(menu.getName());
            existingMenu.setCode(menu.getCode());
            existingMenu.setPath(menu.getPath());
            existingMenu.setIcon(menu.getIcon());
            return menuRepository.save(existingMenu);
        });
    }

    public boolean deleteMenu(Long id) {
        if (menuRepository.existsById(id)) {
            menuRepository.deleteById(id);
            return true;
        }
        return false;
    }
}


