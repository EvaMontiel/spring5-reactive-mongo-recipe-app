package guru.springframework.spring5recipeapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.services.IngredientService;
import guru.springframework.spring5recipeapp.services.RecipeService;
import guru.springframework.spring5recipeapp.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IngredientController {
	
	private final RecipeService recipeService;
	private final IngredientService ingredientService;
	private final UnitOfMeasureService unitOfMeasureService;

	public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.unitOfMeasureService = unitOfMeasureService;
	}

	@GetMapping("/recipe/{recipeId}/ingredients")
	public String listIngredients(Model model, @PathVariable String recipeId) {
		log.debug("Getting ingredient list for recipe id: " + recipeId);
		model.addAttribute("recipe", recipeService.findCommandById(recipeId));
		return "recipe/ingredient/list";
	}
	
	@GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
	public String getIngredients(Model model, @PathVariable String recipeId, @PathVariable String ingredientId) {
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
		return "recipe/ingredient/show";
	}
	
	@GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
	public String updateIngredient(Model model, @PathVariable String recipeId, @PathVariable String ingredientId) {
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
		model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
		return "recipe/ingredient/ingredientform";
	}
	
	@PostMapping("/recipe/{recipeId}/ingredient")
	public String saveOrUpdate(@ModelAttribute IngredientCommand ingredientCommand) {
		IngredientCommand savedCommand = ingredientService.saveIngredientCommand(ingredientCommand);
		log.debug("saved recipe id: " + savedCommand.getRecipeId());
		log.debug("saved ingredient id: " + savedCommand.getId());
		return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
	}
	
	@GetMapping("/recipe/{recipeId}/ingredient/new")
	public String createIngredient(Model model, @PathVariable String recipeId) {
		
		//make sure we have a good id value
		RecipeCommand recipeCommand = recipeService.findCommandById(recipeId);
		//TODO raise exception if null
		
		IngredientCommand ingredientCommand = new IngredientCommand();
		ingredientCommand.setRecipeId(recipeId);
		model.addAttribute("ingredient", ingredientCommand);
		
		//init uom
		ingredientCommand.setUom(new UnitOfMeasureCommand());
		
		model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
		
		return "recipe/ingredient/ingredientform";
	}
	
	@GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/delete")
	public String deleteIngredient(@PathVariable String recipeId, @PathVariable String ingredientId) {
		log.debug("I'm inside of delete method of controller");
		log.debug("Id of the ingredient to delete: " + ingredientId);
		ingredientService.deleteById(recipeId, ingredientId);
		return "redirect:/recipe/" + recipeId + "/ingredients";
	}
}
