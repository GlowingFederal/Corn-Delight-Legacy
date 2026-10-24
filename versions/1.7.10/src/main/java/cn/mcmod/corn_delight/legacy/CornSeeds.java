package cn.mcmod.corn_delight.legacy;
import vectorwing.farmersdelight.legacy.LegacyFood;
import vectorwing.farmersdelight.legacy.api.FoodDefinition;
public final class CornSeeds extends LegacyFood implements net.minecraftforge.common.IPlantable {
 public CornSeeds(FoodDefinition food) { super(food); }
 public net.minecraftforge.common.EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world,int x,int y,int z) { return net.minecraftforge.common.EnumPlantType.Crop; }
 public net.minecraft.block.Block getPlant(net.minecraft.world.IBlockAccess world,int x,int y,int z) { return CornDelight.crop; }
 public int getPlantMetadata(net.minecraft.world.IBlockAccess world,int x,int y,int z) { return 0; }
 @Override public boolean onItemUse(net.minecraft.item.ItemStack stack,net.minecraft.entity.player.EntityPlayer player,net.minecraft.world.World world,int x,int y,int z,int side,float hx,float hy,float hz) {
  return vectorwing.farmersdelight.legacy.CropSeeds.plant(CornDelight.crop,stack,player,world,x,y,z,side);
 }
}
