package guru.springframework.spring5recipeapp.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.converters.RecipeCommandToRecipe;
import guru.springframework.spring5recipeapp.converters.RecipeToRecipeCommand;
import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.exceptions.NotFoundException;
import guru.springframework.spring5recipeapp.repositories.RecipeRepository;

public class RecipeServiceImplTest {
	
	RecipeServiceImpl recipeService;
	
	@Mock
	RecipeRepository recipeRepository;
	
	@Mock
	RecipeCommandToRecipe recipeCommandToRecipe;
	
	@Mock
	RecipeToRecipeCommand recipeToRecipeCommand;
	
	@Before
	public void SetUp() {
		MockitoAnnotations.initMocks(this);
		recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
	}
	
	@Test
	public void testGetRecipesById() {
		Recipe recipe = new Recipe();
		recipe.setId("1");
		Optional<Recipe> recipeOptional = Optional.of(recipe);
		
		when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
		
		Recipe recipeReturned = recipeService.findById("1");
		
		assertNotNull(recipeReturned);
		verify(recipeRepository, times(1)).findById(anyString());
		verify(recipeRepository, never()).findAll();
	}

	@Test
	public void testGetRecipes() {
		
		Recipe recipe = new Recipe();
		HashSet<Recipe> recipesData = new HashSet<Recipe>();
		recipesData.add(recipe);
		
		when(recipeRepository.findAll()).thenReturn(recipesData);
		
		Set<Recipe> recipes = recipeService.getRecipes();
		assertEquals(1, recipes.size());
		verify(recipeRepository, times(1)).findAll();
	}
	
	@Test(expected = NotFoundException.class)
	public void getRecipeByIdTestNotFound() {
		
		Optional<Recipe> recipeOptional = Optional.empty();
		
		when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
		
		Recipe recipeReturned = recipeService.findById("1");
	}

	@Test
	public void getRecipeCommandByIdtest() {
		Recipe recipe = new Recipe();
		recipe.setId("1");
		Optional<Recipe> recipeOptional = Optional.of(recipe);
		
		when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
		
		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId("1");
		
		when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);
		
		RecipeCommand commandById = recipeService.findCommandById("1");
		
		assertNotNull(commandById);
		verify(recipeRepository, times(1)).findById(anyString());
		verify(recipeRepository, never()).findAll();
	}
	
	@Test
	public void testDeleteById() {
		//given
		String idToDelete = "2";
		
		//when
		recipeService.deleteById(idToDelete);
		
		//no "when" since method has void return type
		
		//then
		verify(recipeRepository, times(1)).deleteById(anyString());
	}
}
