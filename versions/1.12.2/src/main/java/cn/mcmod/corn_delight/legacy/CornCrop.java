package cn.mcmod.corn_delight.legacy;
import java.util.Random;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.NonNullList;
public final class CornCrop extends BlockCrops {
 public static final PropertyBool UPPER=PropertyBool.create("upper");
 public CornCrop() { setDefaultState(blockState.getBaseState().withProperty(AGE,0).withProperty(UPPER,false)); }
 protected BlockStateContainer createBlockState() { return new BlockStateContainer(this,AGE,UPPER); }
 public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(AGE,meta&7).withProperty(UPPER,(meta&8)!=0); }
 public int getMetaFromState(IBlockState state) { return getAge(state)|(state.getValue(UPPER)?8:0); }
 protected Item getSeed() { return CornDelight.ITEMS.get("corn_seeds"); }
 protected Item getCrop() { return CornDelight.ITEMS.get("corn"); }
 protected boolean canSustainBush(IBlockState state) { return state.getBlock()==net.minecraft.init.Blocks.FARMLAND || state.getBlock()==cn.mcmod.corn_delight.legacy.compat.Delight.integration.richFarmland(); }
 public boolean canBlockStay(World world,BlockPos pos,IBlockState state) {
  if(state.getValue(UPPER)) { IBlockState lower=world.getBlockState(pos.down());return lower.getBlock()==this && !lower.getValue(UPPER) && getAge(lower)>=4; }
  return super.canBlockStay(world,pos,state);
 }
 public void updateTick(World world,BlockPos pos,IBlockState state,Random random) {
  if(world.isRemote || !world.isAreaLoaded(pos,1)) return;
  if(!canBlockStay(world,pos,state)) { dropBlockAsItem(world,pos,state,0);world.setBlockToAir(pos);return; }
  if(world.getLightFromNeighbors(pos.up())<9) return;
  int age=getAge(state);float speed=getGrowthChance(this,world,pos);
  if(age<7 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world,pos,state,random.nextInt((int)(25F/speed)+1)==0)) {
   world.setBlockState(pos,state.withProperty(AGE,age+1),2);net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world,pos,state,world.getBlockState(pos));
  }
  if(!state.getValue(UPPER) && age>=4 && pos.getY()<254 && world.isAirBlock(pos.up()) && random.nextInt((int)(25F/speed)+1)==0)
   world.setBlockState(pos.up(),getDefaultState().withProperty(UPPER,true),3);
 }
 public boolean canGrow(World world,BlockPos pos,IBlockState state,boolean client) {
  if(state.getValue(UPPER)) return getAge(state)<7;
  IBlockState upper=world.getBlockState(pos.up());return getAge(state)<7 || upper.getBlock()==this && getAge(upper)<7 || world.isAirBlock(pos.up());
 }
 public void grow(World world,Random random,BlockPos pos,IBlockState state) {
  if(world.isRemote) return;
  int next=getAge(state)+getBonemealAgeIncrease(world);
  world.setBlockState(pos,state.withProperty(AGE,Math.min(7,next)),3);
  if(!state.getValue(UPPER) && next>7 && pos.getY()<254) {
   IBlockState upper=world.getBlockState(pos.up());
   if(upper.getBlock()==this) grow(world,random,pos.up(),upper);
   else if(world.isAirBlock(pos.up())) world.setBlockState(pos.up(),getDefaultState().withProperty(UPPER,true).withProperty(AGE,Math.min(7,next-8)),3);
  }
 }
 public void getDrops(NonNullList<ItemStack> drops,IBlockAccess world,BlockPos pos,IBlockState state,int fortune) {
  Random random=world instanceof World?((World)world).rand:RANDOM;
  drops.add(new ItemStack(getAge(state)==7?getCrop():getSeed()));
  if(getAge(state)==7) { int seeds=1;for(int i=0;i<3+Math.max(0,fortune);i++) if(random.nextFloat()<4F/7F) seeds++;drops.add(new ItemStack(getSeed(),seeds)); }
 }
 public net.minecraft.util.math.AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess world,BlockPos pos) {
  int age=getAge(state),height=state.getValue(UPPER)?new int[]{4,6,8,12,14,14,14,14}[age]:Math.min(16,4*(age+1));
  return new net.minecraft.util.math.AxisAlignedBB(0,0,0,1,height/16D,1);
 }
}
