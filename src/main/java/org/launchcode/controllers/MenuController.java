package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping(value="menu")
public class MenuController {

      @Autowired
      private MenuDao menuDao;

      @Autowired
      private CheeseDao cheeseDao;

      // request path: /menu
      @RequestMapping(value="")
      public String index (Model model) {
            model.addAttribute("menus", menuDao.findAll());
            model.addAttribute("title", "Menus");

            return "menu/index";
      }

      // request path: /menu/add
      @RequestMapping(value="add", method = RequestMethod.GET)
      public String add(Model model) {
            model.addAttribute("title", "Add Menu");
            model.addAttribute(new Menu());

            return "menu/add";
      }

      @RequestMapping(value="add", method = RequestMethod.POST)
      public String add(Model model, @ModelAttribute @Valid Menu menu, Errors errors) {
            if (errors.hasErrors()) {
                  model.addAttribute("title", "Add Menu");
                  return "menu/add";
            }

            menuDao.save(menu);

            return "redirect:view/" + menu.getId();
      }

      @RequestMapping(value="view/{menuID}", method = RequestMethod.GET)
      public String viewMenu(Model model, @PathVariable int menuID) {
            Menu menu = menuDao.findOne(menuID);
            model.addAttribute("menu", menu);

            return "menu/view";
      }

      @RequestMapping(value="add-item/{menuID}", method = RequestMethod.GET)
      public String addItem(Model model, @PathVariable int menuID) {
            Menu theMenu = menuDao.findOne(menuID);

            Iterable<Cheese> cheeses = cheeseDao.findAll();
            AddMenuItemForm form = new AddMenuItemForm(theMenu, cheeses);

            model.addAttribute("form", form);
            model.addAttribute("title", "Add item to menu: " + theMenu.getName());

            return "menu/add-item";
      }

      @RequestMapping(value="add-item", method = RequestMethod.POST)
      public String addItem(Model model, @ModelAttribute @Valid AddMenuItemForm form, Errors errors) {
            if (errors.hasErrors()) {
                  model.addAttribute("form", form);
                  model.addAttribute("title", "Add item to menu: " + form.getMenu().getName());
                  return "menu/add-item/" + form.getMenuId();
            }

            Menu theMenu = menuDao.findOne(form.getMenuId());
            Cheese theCheese = cheeseDao.findOne(form.getCheeseId());
            theMenu.addItem(theCheese);

            menuDao.save(theMenu);

            return "redirect:view/" + theMenu.getId();
      }
}
