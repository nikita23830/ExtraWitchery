package com.nikita23830.ewitchery.common.tiles;

import com.emoniph.witchery.client.particle.NaturePowerFX;
import com.nikita23830.ewitchery.common.ModItems;
import com.nikita23830.ewitchery.common.items.ItemBloodArrow;
import com.nikita23830.ewitchery.common.items.ItemDemonEye;
import com.nikita23830.ewitchery.common.items.ItemElfArrow;
import com.nikita23830.ewitchery.common.items.ItemSunArrow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.client.core.helper.RenderHelper;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class TilePedestalElfArrow extends TileEntityContainer implements ISidedInventory, IHud {

    private long startExecute = 0L;
    private int cd = 0;
    private boolean blood = false;
    int cdClientHud = 0;
    String clientHUD = null;

    public TilePedestalElfArrow() {
        super(1);
    }

    public void setBlood() {
        this.blood = true;
    }

    public boolean isBlood() {
        return blood;
    }

    public int onPlace(ItemStack stack, EntityPlayer player) {
        if (stack == null) {
            if (getStackInSlot(0) != null) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, getStackInSlot(0));
                setInventorySlotContents(0, null);
                startExecute = 0L;
                nearestUpdate();
                return -1;
            } else
                return 0;
        } else {
            if (!(stack.getItem() instanceof ItemElfArrow)) {
                if (stack.getItem() instanceof ItemDemonEye) {
                    if (getStackInSlot(0) == null)
                        return 0;
                    if (startExecute == 0L)
                        return 0;
                    LocalDateTime ldt = LocalDateTime.ofInstant(new Date(startExecute).toInstant(), ZoneId.systemDefault()).minusHours(2 + worldObj.rand.nextInt(3)).plusDays(3);
                    if (ldt.isAfter(LocalDateTime.now())) {
                        startExecute = ldt.minusDays(3).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                        nearestUpdate();
                        return 1;
                    }
                }
                return 0;
            }
            ItemStack th = getStackInSlot(0);
            if (th == null) {
                ItemStack clone = stack.copy();
                clone.stackSize = Math.min(stack.stackSize, 8);
                setInventorySlotContents(0, clone);
                startExecute = System.currentTimeMillis();
                nearestUpdate();
                return clone.stackSize;
            } else if (th.stackSize < 8) {
                ItemStack clone = stack.copy();
                clone.stackSize = Math.min(stack.stackSize, (8 - th.stackSize));
                th.stackSize += clone.stackSize;
                setInventorySlotContents(0, th);
                startExecute = System.currentTimeMillis();
                nearestUpdate();
                return clone.stackSize;
            } else
                return 0;
        }
    }

    private void nearestUpdate() {
        List<EntityPlayer> pls = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox((xCoord - 32), 0, (zCoord - 32), (xCoord + 32), 255, (zCoord + 32)));
        for (EntityPlayer pl : pls) {
            if (pl instanceof EntityPlayerMP)
                ((EntityPlayerMP)pl).func_147097_b(this);
        }
    }

    @Override
    public void updateEntityServer() {
        if (startExecute == 0L)
            return;
        if (getStackInSlot(0) == null)
            return;
        if (cd > 0) {
            --cd;
            return;
        }
        cd = 20;
        LocalDateTime ldt = LocalDateTime.ofInstant(new Date(startExecute).toInstant(), ZoneId.systemDefault()).plusDays(3);
        if (ldt.isBefore(LocalDateTime.now())) {
            if (getStackInSlot(0).getItem() instanceof ItemElfArrow) {
                setInventorySlotContents(0, new ItemStack(ModItems.sunArrow, getStackInSlot(0).stackSize));
                startExecute = 0L;
                nearestUpdate();
            } else if (getStackInSlot(0).getItem() instanceof ItemSunArrow && this.blood) {
                setInventorySlotContents(0, new ItemStack(ModItems.bloodArrow, getStackInSlot(0).stackSize));
                startExecute = 0L;
                nearestUpdate();
            }
        }
    }

    @Override
    public void updateEntityClient() {
        if (startExecute == 0L)
            return;
        if (getStackInSlot(0) == null)
            return;
        if (cdClientHud > 0 && clientHUD != null) {
            --cdClientHud;
        } else {
            cdClientHud = 20;
            ZonedDateTime zdt = LocalDateTime.ofInstant(new Date(this.startExecute).toInstant(), ZoneId.systemDefault()).atZone(ZoneId.of("GMT+01:00")).plusDays(3);
            long hours = ChronoUnit.HOURS.between(LocalDateTime.now().atZone(ZoneId.systemDefault()), zdt);
            long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now().atZone(ZoneId.systemDefault()), zdt) % 60L;
            long sec = ChronoUnit.SECONDS.between(LocalDateTime.now().atZone(ZoneId.systemDefault()), zdt) % 60L;
            clientHUD = "§e" + az((int) hours) + ":" + az((int) minutes) + ":" + az((int) sec);
        }
        if (cd > 0) {
            --cd;
            return;
        }
        cd = 5;
        double d0 = this.xCoord + 0.5D;
        double d1 = this.yCoord + 0.5D;
        double d2 = this.zCoord + 0.5D;

        double pathX = (worldObj.rand.nextDouble() * 0.16 - 0.08);
        double pathY = (worldObj.rand.nextDouble() * 0.05 + 0.12);
        double pathZ = (worldObj.rand.nextDouble() * 0.16 - 0.08);
        NaturePowerFX sparkle = new NaturePowerFX(this.worldObj, d0 - pathX, d1 - pathY, d2 - pathZ);
        sparkle.setScale(1.0F);
        sparkle.setGravity(0.2F);
        sparkle.setCanMove(true);
        sparkle.noClip = true;
        sparkle.setVelocity(pathX, pathY, pathZ);
        sparkle.setMaxAge(25 + worldObj.rand.nextInt(10));
        float red = 1.0F;
        float green = 1.0F;
        float blue = 0.0F;
        float maxColorShift = 0.2F;
        float doubleColorShift = maxColorShift * 2.0F;
        float colorshiftR = worldObj.rand.nextFloat() * doubleColorShift - maxColorShift;
        float colorshiftG = worldObj.rand.nextFloat() * doubleColorShift - maxColorShift;
        float colorshiftB = worldObj.rand.nextFloat() * doubleColorShift - maxColorShift;
        sparkle.setRBGColorF(red + colorshiftR, green + colorshiftG, blue + colorshiftB);
        sparkle.setAlphaF(0.1F);
        Minecraft.getMinecraft().effectRenderer.addEffect(sparkle);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setLong("start", startExecute);
        nbt.setBoolean("blood", this.blood);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        startExecute = nbt.getLong("start");
        blood = nbt.getBoolean("blood");
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        if (getStackInSlot(0) != null)
            return false;
        if (this.blood)
            return stack != null && stack.getItem() instanceof ItemSunArrow;
        return stack != null && stack.getItem() instanceof ItemElfArrow && slot == 0;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        if (this.blood)
            return slot == 0 && getStackInSlot(0) != null && getStackInSlot(0).getItem() instanceof ItemBloodArrow;
        return slot == 0 && getStackInSlot(0) != null && getStackInSlot(0).getItem() instanceof ItemSunArrow;
    }

    @Override
    public void renderHUD(Minecraft minecraft, ScaledResolution res) {
        if (this.startExecute == 0L)
            return;
        if (getStackInSlot(0) == null)
            return;
        if (getStackInSlot(0).getItem() instanceof ItemBloodArrow) {
            return;
        }
        if (clientHUD == null)
            return;
        int x = res.getScaledWidth() / 2;
        int y = res.getScaledHeight() / 2 - 8;
        String finish = "До окончания предобразования:";
        int w0 = (minecraft.fontRenderer.getStringWidth(finish) / 2);
        int w1 = (minecraft.fontRenderer.getStringWidth(clientHUD) / 2);
        int w = Math.max(w0, w1);
        RenderHelper.drawGradientRect((x - (w + 5)), (y - 5), 0, (x + (w + 5)), (y + 33), Color.BLACK.getRGB(), Color.BLACK.getRGB());
        minecraft.fontRenderer.drawString(finish, (x - w0), y, Color.WHITE.getRGB());
        minecraft.fontRenderer.drawString(clientHUD, (x - w1), y + 10, Color.WHITE.getRGB());
        minecraft.fontRenderer.drawString("Кол-во стрел: " + getStackInSlot(0).stackSize, (x - (minecraft.fontRenderer.getStringWidth("Кол-во стрел: " + getStackInSlot(0).stackSize) / 2)), y + 20, Color.WHITE.getRGB());
    }

    private String az(int a) {
        return a < 10 ? ("0" + a) : Integer.toString(a);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound data = new NBTTagCompound();
        this.writeToNBT(data);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 2, data);
    }

    @Override
    public void onDataPacket(NetworkManager netManager, S35PacketUpdateTileEntity packet) {
        clientHUD = null;
        readFromNBT(packet.func_148857_g());
    }
}
