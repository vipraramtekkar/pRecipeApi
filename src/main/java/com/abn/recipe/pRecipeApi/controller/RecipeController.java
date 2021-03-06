package com.abn.recipe.pRecipeApi.controller;

import com.abn.recipe.pRecipeApi.exception.ResourceNotFoundException;
import com.abn.recipe.pRecipeApi.model.Recipe;
import com.abn.recipe.pRecipeApi.repository.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin("*")
@RestController
@RequestMapping("/v1/recipes")
@Slf4j
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;


    @GetMapping()
    public ResponseEntity<List<Recipe>> getAllRecipes(){
        log.debug("In get recipe");
        List<Recipe> recipes = (List<Recipe>)recipeRepository.findAll();
        return new ResponseEntity<List<Recipe>>(recipes, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe){
        Recipe recipeEntity = recipeRepository.save(recipe);
        return new ResponseEntity<Recipe>(recipeEntity, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<Recipe> updateRecipe(@RequestBody Recipe recipe){

        Optional<Recipe> recipeOpt = recipeRepository.findById(recipe.getId());
        if(recipeOpt.isPresent()){
            if (null != recipe.getDishType()){
                recipeOpt.get().setDishType(recipe.getDishType());
            }
            if (null != recipe.getCookingSteps()){
                recipeOpt.get().setCookingSteps(recipe.getCookingSteps());
            }
            if(!recipe.getIngredientList().isEmpty()){
                recipeOpt.get().setIngredientList(recipe.getIngredientList());
            }
            if (null != recipe.getNoOfPeople()){
                recipeOpt.get().setNoOfPeople(recipe.getNoOfPeople());
            }
            return new ResponseEntity<>(recipeRepository.save(recipeOpt.get()), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteRecipe(@PathVariable("id") Long id){
        Optional<Recipe> recipeOpt = recipeRepository.findById(id);
        recipeOpt.map((recipeEntity) -> {
            recipeRepository.delete(recipeEntity);
            return true;
        }).orElseThrow(() -> new ResourceNotFoundException("No recipe found for recipe Id " + id));
        return new ResponseEntity<Long>(id, HttpStatus.NO_CONTENT);
    }
}
