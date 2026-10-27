package cn.mcmod.corn_delight.legacy.compat;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import com.wdcftgg.farmersdelightlegacy.api.recipe.CookingPotRecipeApi;
import com.wdcftgg.farmersdelightlegacy.api.recipe.CuttingBoardRecipeApi;
import com.wdcftgg.farmersdelightlegacy.api.knife.KnifeItemApi;
import com.wdcftgg.farmersdelightlegacy.common.registry.ModCreativeTab;
import com.wdcftgg.farmersdelightlegacy.common.recipe.manager.CampfireCookingRecipeManager;
import cn.mcmod.corn_delight.legacy.CornDelight;
public final class AlternativeDelight implements DelightIntegration {
 public CreativeTabs tab() { return ModCreativeTab.TAB; }
 public Item food(JsonObject food) { return new AddonFood(food); }
 public boolean knife(ItemStack stack) {
  if(stack.isEmpty()) return false;
  for(ItemStack candidate:KnifeItemApi.getHuntingAndHarvestKnifeStacks())
   if(candidate.getItem()==stack.getItem() && (stack.isItemStackDamageable() || candidate.getMetadata()==net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE || candidate.getMetadata()==stack.getMetadata())) return true;
  return false;
 }
 public Block richFarmland() { return ForgeRegistries.BLOCKS.getValue(new ResourceLocation("farmersdelight:rich_soil_farmland")); }
 public void init() {}
 public void postInit() {
  new RecipeCatalog("corn_delight",CornDelight.class).register(new RecipeCatalog.Processing() {
   public boolean cooking(String id,String[] ingredients,ItemStack output,ItemStack container,int time,float xp) {
    if(container.isEmpty() && output.getItem().hasContainerItem(output)) container=output.getItem().getContainerItem(output);
    if(!container.isEmpty()) return CookingPotRecipeApi.registerRecipe(id,ingredients,output,container,time,xp);
    // The API overload cannot express an explicitly containerless meal; suppress its default bowl.
    return com.wdcftgg.farmersdelightlegacy.common.recipe.manager.CookingPotRecipeManager.registerScriptRecipe(id,ingredients,output,ItemStack.EMPTY,time,xp,true);
   }
   public boolean cutting(String id,ItemStack[] inputs,ItemStack[] tools,ItemStack[] outputs,float[] chances) {
    return CuttingBoardRecipeApi.registerRecipe(id,inputs,tools,outputs,chances);
   }
   public boolean grilling(String id,String[] inputs,ItemStack output,int time) {
    return CampfireCookingRecipeManager.registerScriptRecipe(id,inputs,output,time);
   }
  });
 }
}
