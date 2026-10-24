package cn.mcmod.corn_delight.legacy;
import vectorwing.farmersdelight.legacy.LegacyFood;
import vectorwing.farmersdelight.legacy.api.FoodDefinition;
public final class CornSeeds extends LegacyFood implements net.minecraftforge.common.IPlantable {
 public CornSeeds(FoodDefinition food) { super(food); }
 public net.minecraftforge.common.EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world,net.minecraft.util.math.BlockPos pos) { return net.minecraftforge.common.EnumPlantType.Crop; }
 public net.minecraft.block.state.IBlockState getPlant(net.minecraft.world.IBlockAccess world,net.minecraft.util.math.BlockPos pos) { return CornDelight.crop.getDefaultState(); }
 @Override public net.minecraft.util.EnumActionResult onItemUse(net.minecraft.entity.player.EntityPlayer player,net.minecraft.world.World world,net.minecraft.util.math.BlockPos pos,net.minecraft.util.EnumHand hand,net.minecraft.util.EnumFacing side,float hx,float hy,float hz) {
  return vectorwing.farmersdelight.legacy.CropSeeds.plant(CornDelight.crop,player.getHeldItem(hand),player,world,pos,side) ? net.minecraft.util.EnumActionResult.SUCCESS : net.minecraft.util.EnumActionResult.PASS;
 }
}
