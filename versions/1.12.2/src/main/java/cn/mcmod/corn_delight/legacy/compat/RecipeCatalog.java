package cn.mcmod.corn_delight.legacy.compat;
import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

/** Translates only this addon's source catalog; processing stays in FD's native managers. */
public final class RecipeCatalog {
 public interface Processing {
  boolean cooking(String id,String[] ingredients,ItemStack output,ItemStack container,int time,float xp);
  boolean cutting(String id,ItemStack[] inputs,ItemStack[] tools,ItemStack[] outputs,float[] chances);
  boolean grilling(String id,String[] inputs,ItemStack output,int time);
 }
 private final String namespace;
 private final JsonObject recipes,tags;
 public final Map<String,String> deferred=new LinkedHashMap<>();
 public RecipeCatalog(String namespace,Class<?> owner) {
  this.namespace=namespace;
  recipes=read(owner,"/"+namespace+"/source_recipes.json");
  tags=read(owner,"/"+namespace+"/alternative_tags.json");
  JsonObject additions=read(owner,"/"+namespace+"/source_tags.json");
  for(Map.Entry<String,JsonElement> entry:additions.entrySet()) {
   if(!tags.has(entry.getKey())) tags.add(entry.getKey(),entry.getValue());
   else for(JsonElement value:entry.getValue().getAsJsonObject().getAsJsonArray("values")) tags.getAsJsonObject(entry.getKey()).getAsJsonArray("values").add(value);
  }
 }
 private static JsonObject read(Class<?> owner,String path) {
  InputStream stream=owner.getResourceAsStream(path);
  if(stream==null) throw new IllegalStateException("Missing addon catalog: "+path);
  try(Reader reader=new InputStreamReader(stream,StandardCharsets.UTF_8)) { return new Gson().fromJson(reader,JsonObject.class); }
  catch(IOException failure) { throw new IllegalStateException("Cannot read "+path,failure); }
 }
 public static ItemStack stack(String id) {
  // Exact flattened vanilla identities used by these addons; no substitute content.
  if(id.equals("minecraft:pufferfish")) id="minecraft:fish@3";
  else if(id.equals("minecraft:cod")) id="minecraft:fish@0";
  else if(id.equals("minecraft:bone_meal")) id="minecraft:dye@15";
  int marker=id.indexOf('@'),meta=0;
  if(marker>=0) { meta=Integer.parseInt(id.substring(marker+1));id=id.substring(0,marker); }
  Item item=ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
  return item==null || item==net.minecraft.init.Items.AIR ? ItemStack.EMPTY : new ItemStack(item,1,meta);
 }
 private static ItemStack result(JsonElement value) {
  if(value.isJsonPrimitive()) return stack(value.getAsString());
  JsonObject json=value.getAsJsonObject(); ItemStack output=stack(json.get("item").getAsString());
  if(!output.isEmpty() && json.has("count")) output.setCount(json.get("count").getAsInt());
  return output;
 }
 private List<ItemStack> candidates(JsonElement value) {
  List<ItemStack> options=new ArrayList<>();
  if(value.isJsonArray()) for(JsonElement choice:value.getAsJsonArray()) options.addAll(candidates(choice));
  else {
   JsonObject json=value.getAsJsonObject();
   if(json.has("item")) add(options,stack(json.get("item").getAsString()));
   else if(json.has("tag")) options.addAll(tag(json.get("tag").getAsString(),new HashSet<String>()));
   else throw new IllegalArgumentException("Unknown source ingredient: "+value);
  }
  return options;
 }
 private List<ItemStack> tag(String name,Set<String> visited) {
  if(!visited.add(name)) throw new IllegalArgumentException("Cyclic addon tag: "+name);
  List<ItemStack> options=new ArrayList<>();
  if(tags.has(name)) for(JsonElement value:tags.getAsJsonObject(name).getAsJsonArray("values")) {
   String id=value.getAsString();
   if(id.startsWith("#")) options.addAll(tag(id.substring(1),visited));else add(options,stack(id));
  }
  // Public OreDictionary groups remain additive to the canonical source membership.
  String ore=null;
  if(name.startsWith("forge:crops/")) {
   String crop=name.substring(12);ore="crop"+Character.toUpperCase(crop.charAt(0))+crop.substring(1);
  } else if(name.equals("forge:eggs")) ore="listAllegg";
  else if(name.equals("forge:milk")) ore="listAllmilk";
  else if(name.equals("forge:dough")) ore="foodDough";
  else if(name.equals("forge:tools/knives")) ore="toolKnife";
  if(ore!=null) for(ItemStack stack:OreDictionary.getOres(ore)) add(options,stack.copy());
  visited.remove(name);return options;
 }
 private static void add(List<ItemStack> options,ItemStack stack) { if(!stack.isEmpty()) options.add(stack); }
 private List<ItemStack> required(JsonElement value) {
  List<ItemStack> options=candidates(value);
  if(options.isEmpty()) throw new Unavailable("Unavailable ingredient "+value);
  return options;
 }
 private String token(String id,int index,List<ItemStack> options) {
  // Private exact-membership ore groups express alternatives without a Cartesian recipe expansion.
  String ore="fdAddon_"+id.replace(':','_').replace('/','_')+"_"+index;
  for(ItemStack option:options) OreDictionary.registerOre(ore,option);
  return "ore:"+ore;
 }
 public void register(Processing processing) {
  for(Map.Entry<String,JsonElement> entry:recipes.entrySet()) {
   String id=entry.getKey();JsonObject json=entry.getValue().getAsJsonObject();
   try { register(id,json,processing); }
   catch(Unavailable missing) { deferred.put(id,missing.getMessage()); }
  }
  org.apache.logging.log4j.LogManager.getLogger(namespace).info("Deferred addon recipes: {}",deferred);
 }
 private void register(String id,JsonObject json,Processing processing) {
  if(json.has("conditions")) for(JsonElement value:json.getAsJsonArray("conditions")) {
   JsonObject condition=value.getAsJsonObject();
   if(!condition.get("type").getAsString().equals("forge:mod_loaded") || !Loader.isModLoaded(condition.get("modid").getAsString()))
    throw new Unavailable("Unavailable optional recipe condition: "+condition);
  }
  String type=json.get("type").getAsString();
  if(!type.startsWith("minecraft:") && !type.equals("farmersdelight:cooking") && !type.equals("farmersdelight:cutting"))
   throw new Unavailable("No native adapter for "+type);
  // No obtainable stuffed cod exists in vanilla 1.12.2; preserve the existing acquisition gate.
  if(json.toString().contains("\"oceansdelight:stuffed_cod\"")) throw new Unavailable("Unavailable upstream acquisition: oceansdelight:stuffed_cod");
  if(type.equals("farmersdelight:cutting")) {
   List<ItemStack> inputs=new ArrayList<>();
   for(JsonElement value:json.getAsJsonArray("ingredients")) inputs.addAll(required(value));
   List<ItemStack> tools=required(json.get("tool"));
   JsonArray results=json.getAsJsonArray("result");ItemStack[] outputs=new ItemStack[results.size()];float[] chances=new float[outputs.length];
   for(int i=0;i<outputs.length;i++) {
    JsonObject output=results.get(i).getAsJsonObject();outputs[i]=result(output);
    if(outputs[i].isEmpty()) throw new Unavailable("Unavailable cutting output "+output);
    chances[i]=output.has("chance") ? output.get("chance").getAsFloat() : 1F;
   }
   if(!processing.cutting(id,inputs.toArray(new ItemStack[0]),tools.toArray(new ItemStack[0]),outputs,chances)) throw new IllegalStateException("Cutting API rejected "+id);
   return;
  }
  ItemStack output=result(json.get("result"));
  if(output.isEmpty()) throw new Unavailable("Unavailable output "+json.get("result"));
  int time=json.has("cookingtime") ? json.get("cookingtime").getAsInt() : 200;
  float xp=json.has("experience") ? json.get("experience").getAsFloat() : 0F;
  if(type.equals("farmersdelight:cooking")) {
   JsonArray ingredients=json.getAsJsonArray("ingredients");String[] tokens=new String[ingredients.size()];
   // Resolve everything before mutating the public managers or OreDictionary.
   List<List<ItemStack>> options=new ArrayList<>();for(JsonElement value:ingredients) options.add(required(value));
   ItemStack container=json.has("container") ? result(json.get("container")) : ItemStack.EMPTY;
   if(json.has("container") && container.isEmpty()) throw new Unavailable("Unavailable serving container");
   for(int i=0;i<tokens.length;i++) tokens[i]=token(id,i,options.get(i));
   if(!processing.cooking(id,tokens,output,container,time,xp)) throw new IllegalStateException("Cooking API rejected "+id);
  } else if(type.equals("minecraft:crafting_shaped") || type.equals("minecraft:crafting_shapeless")) {
   NonNullList<Ingredient> ingredients=NonNullList.create();IRecipe recipe;
   if(json.has("pattern")) {
    JsonArray rows=json.getAsJsonArray("pattern");int width=rows.get(0).getAsString().length();
    for(JsonElement row:rows) for(char key:row.getAsString().toCharArray()) ingredients.add(key==' ' ? Ingredient.EMPTY : ingredient(required(json.getAsJsonObject("key").get(String.valueOf(key)))));
    recipe=new ShapedRecipes(namespace,width,rows.size(),ingredients,output);
   } else {
    for(JsonElement value:json.getAsJsonArray("ingredients")) ingredients.add(ingredient(required(value)));
    recipe=new ShapelessRecipes(namespace,output,ingredients);
   }
   recipe.setRegistryName(new ResourceLocation(id));ForgeRegistries.RECIPES.register(recipe);
  } else {
   List<ItemStack> inputs=required(json.get("ingredient"));
   if(type.equals("minecraft:campfire_cooking")) {
    if(!processing.grilling(id,new String[]{token(id,0,inputs)},output,time)) throw new IllegalStateException("Grilling manager rejected "+id);
   } else if(type.equals("minecraft:smelting") || type.equals("minecraft:smoking")) {
    for(ItemStack input:inputs) if(FurnaceRecipes.instance().getSmeltingResult(input).isEmpty()) FurnaceRecipes.instance().addSmeltingRecipe(input,output.copy(),xp);
   } else throw new Unavailable("No native adapter for "+type);
  }
 }
 private static Ingredient ingredient(List<ItemStack> stacks) { return Ingredient.fromStacks(stacks.toArray(new ItemStack[0])); }
 private static final class Unavailable extends RuntimeException { Unavailable(String message) { super(message); } }
}
