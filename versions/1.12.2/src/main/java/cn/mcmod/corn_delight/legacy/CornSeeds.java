package cn.mcmod.corn_delight.legacy;
import cn.mcmod.corn_delight.legacy.compat.AddonFood;
import com.google.gson.JsonObject;
public final class CornSeeds extends AddonFood implements net.minecraftforge.common.IPlantable {
 public CornSeeds(JsonObject food) { super(food); }
 public net.minecraftforge.common.EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world,net.minecraft.util.math.BlockPos pos) { return net.minecraftforge.common.EnumPlantType.Crop; }
 public net.minecraft.block.state.IBlockState getPlant(net.minecraft.world.IBlockAccess world,net.minecraft.util.math.BlockPos pos) { return CornDelight.crop.getDefaultState(); }
 @Override public net.minecraft.util.EnumActionResult onItemUse(net.minecraft.entity.player.EntityPlayer player,net.minecraft.world.World world,net.minecraft.util.math.BlockPos pos,net.minecraft.util.EnumHand hand,net.minecraft.util.EnumFacing side,float hx,float hy,float hz) {
  net.minecraft.util.math.BlockPos target=pos.up();
  net.minecraft.item.ItemStack seed=player.getHeldItem(hand);
  if(side!=net.minecraft.util.EnumFacing.UP || !player.canPlayerEdit(target,side,seed) || !world.isAirBlock(target) || !CornDelight.crop.canPlaceBlockAt(world,target)) return net.minecraft.util.EnumActionResult.PASS;
  if(!world.isRemote) {
   world.setBlockState(target,CornDelight.crop.getDefaultState(),3);
   if(!player.capabilities.isCreativeMode) seed.shrink(1);
  }
  return net.minecraft.util.EnumActionResult.SUCCESS;
 }
}
